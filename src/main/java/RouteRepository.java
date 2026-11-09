import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RouteRepository
{
    // add route ID and name to DB
    public static int add_route_info(Connection connection, Route route) throws SQLException
    {
        // dont allow null route
        if (route == null)
        {
            throw new IllegalArgumentException("Route must not be null");
        }

        // SQL command to insert route info
        String sql = "INSERT INTO routes (id, name) VALUES (?, ?)";

        // prepare command using existing connection
        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            // bind route ID and name to SQL parameters
            statement.setString(1, route.id());
            statement.setString(2, route.name());

            // execute insert and return number of added rows
            return statement.executeUpdate();
        }
    }

    // add existing stop at given position in existing route
    public static int add_route_stop(Connection connection, String route_id, int stop_index, String stop_id) throws SQLException
    {
        // reject null empty or whitespace only route ID
        if (route_id == null || route_id.isBlank())
        {
            throw new IllegalArgumentException("Route ID must not be blank");
        }

        // dont allow negative stop index
        if (stop_index < 0)
        {
            throw new IllegalArgumentException("Stop index must be non-negative");
        }

        // reject null empty or whitespace only stop ID
        if (stop_id == null || stop_id.isBlank())
        {
            throw new IllegalArgumentException("Stop ID must not be blank");
        }

        // SQL command to link route position and stop
        String sql = "INSERT INTO route_stops (route_id, stop_index, stop_id) VALUES (?, ?, ?)";

        // prepare command using existing connection
        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            // bind route ID stop index and stop ID to SQL parameters
            statement.setString(1, route_id);
            statement.setInt(2, stop_index);
            statement.setString(3, stop_id);

            // execute insert and return number of added rows
            return statement.executeUpdate();
        }
    }
}
