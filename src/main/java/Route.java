import java.util.List;
import java.util.OptionalInt;

// route data with stops in travel order
public record Route(String id, String name, List<Stop> stops)
{
    public Route
    {
        // validate all constructor arguments
        if (id == null || id.isBlank())
        {
            throw new IllegalArgumentException("Route ID must not be blank");
        }

        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Route name must not be blank");
        }

        if (stops == null || stops.isEmpty())
        {
            throw new IllegalArgumentException("Route stops must not be null or empty");
        }

        // store non-changeble copy of provided stop list
        stops = List.copyOf(stops);
    }

    // method to fing stop position in list of stops
    public OptionalInt find_stop_index(String stop_id)
    {
        // reject null and blank
        if (stop_id == null || stop_id.isBlank())
        {
            throw new IllegalArgumentException("Stop ID must not be blank");
        }

        // find position of ID in list
        for (int stop_index = 0; stop_index < stops.size(); stop_index++)
        {
            if (stops.get(stop_index).id().equals(stop_id))
            {
                return OptionalInt.of(stop_index);
            }
        }

        // if nothing found return empty OptionalInt
        return OptionalInt.empty();
    }
}
