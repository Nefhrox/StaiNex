import java.util.List;
import java.util.OptionalInt;

public class StaiNex
{
    public static void main(String[] args)
    {
        // test stops in route order
        List<Stop> route_stops = List.of(
            new Stop("stop-1", "station"),
            new Stop("stop-2", "square"),
            new Stop("stop-3", "center"),
            new Stop("stop-4", "park"),
            new Stop("stop-5", "university")
        );

        // validate launch arguments
        if (args.length > 2 || (args.length >= 1 && args[0].isBlank()) || (args.length == 2 && !args[1].equals("1") && !args[1].equals("2")))
        {
            System.out.println("Usage: java -cp target/classes StaiNex [target_stop_id] [notify_before_stops: 1 or 2]");
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
        OptionalInt target_position = StopCounter.find_stop_index(route_stops, target_stop_id);

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
        if (args.length == 2)
        {
            notify_before_stops = Integer.parseInt(args[1]);
        }

        // simulate moving from the first stop to the destination
        for (int current_index = 0; current_index <= target_index; current_index++)
        {
            // display the current stop name
            System.out.println("Current station: " + route_stops.get(current_index).name());
            int remaining_stops = StopCounter.count_remaining_stops(route_stops, current_index, target_index);

            // report arrival at the destination or warn when it is 1 or 2 stops away
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
