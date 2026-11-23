import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class StaiNex
{
    public static Optional<Route> load_route(String route_id) throws SQLException
    {
        try (Connection connection = Database.open_connection())
        {
        // load route from DB by ID 
            return RouteRepository.find_route(connection, route_id);
        }
    }

    public static void main(String[] args)
    {
        // validate launch arguments
        if (args.length < 1 || args.length > 4 || args[0].isBlank() || (args.length >= 3 && !args[2].equals("1") && !args[2].equals("2")) || (args.length == 4 && args[3].isBlank()))
        {
            System.out.println("Usage: java -cp 'target/classes:target/dependency/*' StaiNex <route_id> [target_stop_id] [notify_before_stops: 1 or 2] [start_stop_id]");
            return;
        }

        Optional<Route> loaded_route;

        try
        {
            loaded_route = load_route(args[0]);
        }
        catch (SQLException exception)
        {
            System.err.println("Failed to load route. SQLSTATE: " + exception.getSQLState());
            return;
        }
        catch (IllegalStateException exception)
        {
            System.err.println(exception.getMessage());
            return;
        }

        if (loaded_route.isEmpty())
        {
            System.out.println("Route not found in DB: " + args[0]);
            return;
        }

        // use loaded route
        Route route = loaded_route.get();

        // stops from loaded route
        List<Stop> route_stops = route.stops();

        // choose the last stop as the destination, if no other was given to args
        String target_stop_id = route_stops.get(route_stops.size() - 1).id();

        if (args.length >= 2)
        {
            target_stop_id = args[1];
        }

        // find index of target stop in route
        OptionalInt target_position = route.find_stop_index(target_stop_id);

        // if route doesnt have target stop
        if (target_position.isEmpty())
        {
            System.out.println("Target stop not found in the route: " + target_stop_id);
            return;
        }

        // get index of target stop
        int target_index = target_position.getAsInt();

        int notify_before_stops = 2;

        // read how many stops before the destination to start notifying
        if (args.length >= 3)
        {
            notify_before_stops = Integer.parseInt(args[2]);
        }

        int start_index = 0;

        // try to find starting stop
        if (args.length == 4)
        {
            OptionalInt start_position = route.find_stop_index(args[3]);

            if (start_position.isEmpty())
            {
                System.out.println("Starting stop not found in the route: " + args[3]);
                return;
            }

            // if argument is in the route get its index
            start_index = start_position.getAsInt();
        }

        // dont allow destination behind starting stop
        if (start_index > target_index)
        {
            System.out.println("Target stop is behind the starting stop.");
            return;
        }

        // simulate moving from the selected starting stop to the destination
        for (int current_index = start_index; current_index <= target_index; current_index++)
        {
            // display the current stop name
            System.out.println("Current station: " + route_stops.get(current_index).name());
            int remaining_stops = StopCounter.count_remaining_stops(route_stops, current_index, target_index);

            // report arrival or warn within the configured notification range
            if (current_index == target_index)
            {
                System.out.println("You arrived to target station");
            }
            else if (remaining_stops <= notify_before_stops && TripNotifier.should_notify(route_stops, current_index, target_index))
            {
                System.out.println("Stops remaining: " + remaining_stops + ". Get ready to exit.");
            }
        }
    }
}
