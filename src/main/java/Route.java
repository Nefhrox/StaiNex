import java.util.List;

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
}
