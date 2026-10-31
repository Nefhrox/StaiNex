import java.util.List;
import java.util.OptionalInt;

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

    // method to fing stop position in list of stops
    public static OptionalInt find_stop_index(List<Stop> route_stops, String stop_id)
    {
        // reject null and blank 
        if (route_stops == null)
        {
            throw new IllegalArgumentException("Route stops must not be null");
        }

        if (stop_id == null || stop_id.isBlank())
        {
            throw new IllegalArgumentException("Stop ID must not be blank");
        }

        // find position of ID in list
        for (int stop_index = 0; stop_index < route_stops.size(); stop_index++)
        {
            if (route_stops.get(stop_index).id().equals(stop_id))
            {
                return OptionalInt.of(stop_index);
            }
        }

        // if nothing found return empty object
        return OptionalInt.empty();
    }
}
