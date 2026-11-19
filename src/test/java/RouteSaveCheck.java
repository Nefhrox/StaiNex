import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


// test class to check if DB works as planned
public class RouteSaveCheck
{
    public static void main(String[] args)
    {
        // test data
        Stop first_stop = new Stop("save-check-stop-1", "Save check first stop");
        Stop second_stop = new Stop("save-check-stop-2", "Save check second stop");
        Route route = new Route(
            "save-check-route",
            "Save check route",
            List.of(first_stop, second_stop, first_stop)
        );

        // open new connection to DB
        try (Connection connection = Database.open_connection())
        {
            try
            {
                // save test route
                RouteRepository.save_route(connection, route);

                // load route in DB
                Optional<Route> loaded_route = RouteRepository.find_route(connection, route.id());

                // throw exception if test route is empty or has wrong data
                if (loaded_route.isEmpty() || !loaded_route.get().equals(route))
                {
                    throw new IllegalStateException("Saved route was not loaded back unchanged");
                }

                // intentionally try to find non existing route
                Optional<Route> missing_route = RouteRepository.find_route(connection, "save-check-missing-route");

                // if missing route is in DB throw exception
                if (missing_route.isPresent())
                {
                    throw new IllegalStateException("Unknown route must not be found");
                }
            }

            finally
            {
                // delete all data(route links, route, stops) from DB 

                try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM route_stops WHERE route_id = ?"))
                {
                    statement.setString(1, route.id());
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM routes WHERE id = ?"))
                {
                    statement.setString(1, route.id());
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM stops WHERE id = ?"))
                {
                    for (Stop stop : route.stops())
                    {
                        statement.setString(1, stop.id());
                        statement.executeUpdate();
                    }
                }
            }
        }
        // if we encountered sql error write about it
        catch (SQLException exception)
        {
            System.err.println("Route save check failed. SQLSTATE: " + exception.getSQLState());
            System.exit(1);
        }
        catch (IllegalStateException exception)
        {
            System.err.println(exception.getMessage());
            System.exit(1);
        }

        // no exception means test passed successfully
        System.out.println("Route save check passed: saved route loaded back unchanged. Test data removed.");
    }
}
