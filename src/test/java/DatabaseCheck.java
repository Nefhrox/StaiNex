import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DatabaseCheck
{
    public static void main(String[] args)
    {
        // test stops for database check
        Stop first_stop = new Stop("db-check-stop-1", "First check stop");
        Stop second_stop = new Stop("db-check-stop-2", "Second check stop");

        // route with repeated stop to check order and duplicates
        Route route = new Route(
            "db-check-route",
            "Database check route",
            List.of(second_stop, first_stop, second_stop)
        );

        // open connection using environment settings
        try (Connection connection = Database.open_connection())
        {
            // use manual transaction to roll back test data
            connection.setAutoCommit(false);

            try
            {
                // add stops before route links because of foreign keys
                StopRepository.add_stop(connection, first_stop);
                StopRepository.add_stop(connection, second_stop);
                RouteRepository.add_route_info(connection, route);

                // insert route stops from last to first to check reading order
                for (int stop_index = route.stops().size() - 1; stop_index >= 0; stop_index--)
                {
                    RouteRepository.add_route_stop(
                        connection,
                        route.id(),
                        stop_index,
                        route.stops().get(stop_index).id()
                    );
                }

                // read route stops from DB in stop index order
                List<Stop> loaded_stops = RouteRepository.find_route_stops(connection, route.id());

                // stop if order or repeated stops were not preserved
                if (!loaded_stops.equals(route.stops()))
                {
                    throw new IllegalStateException("Route stop order or repeated stops were not preserved");
                }
            }
            finally
            {
                // always roll back test changes
                connection.rollback();
            }
        }
        catch (SQLException exception)
        {
            // print SQL error code and exit with failure
            System.err.println("Database check failed. SQLSTATE: " + exception.getSQLState());
            System.exit(1);
        }
        catch (IllegalStateException exception)
        {
            // print error message and exit with failure
            System.err.println(exception.getMessage());
            System.exit(1);
        }

        // print success only after connection is closed
        System.out.println("Database check passed: route order and repeated stops preserved. Test data rolled back.");
    }
}
