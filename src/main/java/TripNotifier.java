import java.util.List;

public class TripNotifier
{
    // check if we should notify user
    public static boolean should_notify(List<Stop> route_stops, int current_index, int target_index)
    {
        // count remaining stops
        int remaining_stops = StopCounter.count_remaining_stops(route_stops, current_index, target_index);

        // check if we should remind user if there are 1 or 2 stops before target stop
        return remaining_stops >= 1 && remaining_stops <= 2;
    }
}
