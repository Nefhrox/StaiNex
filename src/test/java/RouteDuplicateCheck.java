import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// class to check if duplicates are rejected
public class RouteDuplicateCheck
{
    public static void main(String[] args)
    {
        // test data
        Stop first_stop = new Stop("duplicate-check-stop-1", "Duplicate check first stop");
        Stop second_stop = new Stop("duplicate-check-stop-2", "Duplicate check second stop");
        Stop new_stop = new Stop("duplicate-check-stop-3", "Duplicate check new stop");

        Route saved_route = new Route(
            "duplicate-check-route",
            "Duplicate check route",
            List.of(first_stop, second_stop)
        );

        Route duplicate_route = new Route(
            "duplicate-check-route",
            "Duplicate check route changed",
            List.of(first_stop, new_stop)
        );

        // new connection to DB
        try (Connection connection = Database.open_connection())
        {
            try
            {
                // save route in DB
                RouteRepository.save_route(connection, saved_route);

                String duplicate_state = null;

                // try to save route with same ID, expect sql error
                try
                {
                    RouteRepository.save_route(connection, duplicate_route);
                }
                catch (SQLException exception)
                {
                    duplicate_state = exception.getSQLState();
                }

                // if sql error wasnt triggered throw exception that means that sql accepted duplicate
                if (!"23505".equals(duplicate_state))
                {
                    throw new IllegalStateException("Duplicate route must be rejected with 23505 but got " + duplicate_state);
                }

                // check if "new stop" was added before conflict 
                boolean new_stop_stored = false;

                try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT 1 FROM stops WHERE id = ?"))
                {
                    statement.setString(1, new_stop.id());

                    try (ResultSet result_set = statement.executeQuery())
                    {
                        new_stop_stored = result_set.next();
                    }
                }

                // if "new stop" is saved then rollback didnt work properly
                if (new_stop_stored)
                {
                    throw new IllegalStateException("Failed save must be rolled back but new stop was stored");
                }

                Optional<Route> loaded_route = RouteRepository.find_route(connection, saved_route.id());

                if (loaded_route.isEmpty() || !loaded_route.get().equals(saved_route))
                {
                    throw new IllegalStateException("Original route was changed by failed save");
                }
            }

            // // delete all data(route links, route, stops) from DB 
            finally
            {
                try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM route_stops WHERE route_id = ?"))
                {
                    statement.setString(1, saved_route.id());
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM routes WHERE id = ?"))
                {
                    statement.setString(1, saved_route.id());
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM stops WHERE id = ?"))
                {
                    for (Stop stop : List.of(first_stop, second_stop, new_stop))
                    {
                        statement.setString(1, stop.id());
                        statement.executeUpdate();
                    }
                }
            }
        }
        // if we encountered sql error write it
        catch (SQLException exception)
        {
            System.err.println("Route duplicate check failed. SQLSTATE: " + exception.getSQLState());
            System.exit(1);
        }
        catch (IllegalStateException exception)
        {
            System.err.println(exception.getMessage());
            System.exit(1);
        }

        // if no exceptions then test has passed
        System.out.println("Route duplicate check passed: duplicate route rejected and failed save rolled back.");
    }
}
