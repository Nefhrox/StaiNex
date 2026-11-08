import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RouteRepository
{
    public static int add_route_info(Connection connection, Route route) throws SQLException
    {
        if (route == null)
        {
            throw new IllegalArgumentException("Route must not be null");
        }

        String sql = "INSERT INTO routes (id, name) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, route.id());
            statement.setString(2, route.name());

            return statement.executeUpdate();
        }
    }
}
