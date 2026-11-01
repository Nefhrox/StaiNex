public class TripNotifier
{
    // check if we should notify user
    public static boolean should_notify(int remaining_stops)
    {
        // reject negative number of stops
        if (remaining_stops < 0)
        {
            throw new IllegalArgumentException("Remaining stops must be non-negative");
        }

        // remind user if there are 1 or 2 stops before target stop
        return remaining_stops >= 1 && remaining_stops <= 2;
    }
}
