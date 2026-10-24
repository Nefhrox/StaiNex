public class StopCounter 
{
    //give to function current stop and target stop
    public static int count_remaining_stops(int current_index, int target_index) 
    {
        if (current_index < 0 || target_index < 0) 
        {
            // dont allow negative indexes
            throw new IllegalArgumentException("Stop indices must be non-negative");
        }

        if (target_index < current_index) 
        {
            // distance to target stop cant be negative
            throw new IllegalArgumentException("Target stop is behind the current stop");
        }

        // if everything is right return number of stops to target stop
        return target_index - current_index;
    }
}