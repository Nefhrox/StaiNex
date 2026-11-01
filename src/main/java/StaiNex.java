import java.util.List;

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

        // choose the last stop as the destination
        int target_index = route_stops.size() - 1;

        // simulate moving from the first stop to the destination
        for (int current_index = 0; current_index <= target_index; current_index++)
        {
            // display the current stop name
            System.out.println("Current station: " + route_stops.get(current_index).name());

            // report arrival at the destination or warn when it is 1 or 2 stops away
            if (current_index == target_index)
            {
                System.out.println("You arrived to target station");
            }
            else if (TripNotifier.should_notify(route_stops, current_index, target_index))
            {
                System.out.println("There are 1-2 station to your destination. Get ready to exit.");
            }
        }
    }
}
