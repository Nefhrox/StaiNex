import java.util.List;
import java.util.OptionalInt;

public class StaiNex
{
    public static void main(String[] args)
    {
        // test route
        Route route = new Route(
            "demo-outbound",
            "Demo outbound route",
            List.of(
                new Stop("stop-1", "station"),
                new Stop("stop-2", "square"),
                new Stop("stop-3", "center"),
                new Stop("stop-4", "park"),
                new Stop("stop-5", "university")
            )
        );

        // list of test stops
        List<Stop> route_stops = route.stops();

        // validate launch arguments
        if (args.length > 3 || (args.length >= 1 && args[0].isBlank()) || (args.length >= 2 && !args[1].equals("1") && !args[1].equals("2")) || (args.length == 3 && args[2].isBlank()))
        {
            System.out.println("Usage: java -cp target/classes StaiNex [target_stop_id] [notify_before_stops: 1 or 2] [start_stop_id]");
            return;
        }

        // choose the last stop as the destination, if no other was given to args
        String target_stop_id = "stop-5";

        // use first argument as target stop ID
        if (args.length >= 1)
        {
            target_stop_id = args[0];
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
        if (args.length >= 2)
        {
            notify_before_stops = Integer.parseInt(args[1]);
        }

        int start_index = 0;

        // try to find starting stop
        if (args.length == 3)
        {
            OptionalInt start_position = route.find_stop_index(args[2]);

            if (start_position.isEmpty())
            {
                System.out.println("Starting stop not found in the route: " + args[2]);
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
