import java.util.List;

public class StopCounter 
{
    // calculate remaining stops from current and target indices
    public static int count_remaining_stops(int current_index, int target_index) 
    {
        if (current_index < 0 || target_index < 0) 
        {
            // dont allow negative indexes
            throw new IllegalArgumentException("Stop indices must be non-negative");
        }

        if (target_index < current_index) 
        {
            // reject a target behind the current position
            throw new IllegalArgumentException("Target stop is behind the current stop");
        }

        // if everything is right return number of stops to target stop
        return target_index - current_index;
    }

    // validate indices against the route list before calculating remaining stops
    public static int count_remaining_stops(List<Stop> route_stops, int current_index, int target_index)
    {
        // reject a null route list
        if (route_stops == null)
        {
            throw new IllegalArgumentException("Route stops must not be null");
        }

        // reject indices at or above the list size. the two-argument method checks negative indices
        if (current_index >= route_stops.size() || target_index >= route_stops.size())
        {
            throw new IllegalArgumentException("Stop indices must be within the route");
        }

        return count_remaining_stops(current_index, target_index);
    }
}
