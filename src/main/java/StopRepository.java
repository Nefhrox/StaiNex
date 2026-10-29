import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StopRepository 
{
    // get stops list from method find_all that connects to DB 
    public static List<Stop> find_all(Connection connection) throws SQLException 
    {
        // list for stops that program gets from DB
        List<Stop> stops = new ArrayList<>();

        // future SQL command
        String sql = "SELECT id, name FROM stops ORDER BY id";

        try 
        (
            // object that connects to DB
            PreparedStatement statement = connection.prepareStatement(sql);
            // SQL command execution
            ResultSet result_set = statement.executeQuery()
        ) 
        {
            while (result_set.next()) 
            {
                Stop stop = new Stop(
                    result_set.getString("id"),
                    result_set.getString("name")
                );

                // add new stop to list
                stops.add(stop);
            }
        }

        return stops;
    }



    // add new stop to DB
    public static int add_stop(Connection connection, Stop stop) throws SQLException
    {
        // dont allow null stop
        if (stop == null)
        {
            throw new IllegalArgumentException("Stop must not be null");
        }

        // command for DB
        String sql = "INSERT INTO stops (id, name) VALUES (?, ?)";

        // connect to DB and execute SQL command through existing connection
        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            // for stop first value is id and second is name
            statement.setString(1, stop.id());
            statement.setString(2, stop.name());

            // dont create routes yet
            return statement.executeUpdate();
        }
    }



    // find stop by its ID
    public static Optional<Stop> find_by_id(Connection connection, String stop_id) throws SQLException
    {
        // dont allow nullable or empty ID
        if (stop_id == null || stop_id.isBlank())
        {
            throw new IllegalArgumentException("Stop ID must not be blank");
        }

        // sql command
        String sql = "SELECT id, name FROM stops WHERE id = ?";

        // prepare sql command for existing connection
        try (PreparedStatement statement = connection.prepareStatement(sql))
        {
            // bind request stop ID to the query
            statement.setString(1, stop_id);

            // execute search
            try (ResultSet result_set = statement.executeQuery())
            {
                if (result_set.next())
                {
                    // reading needed stop and creating stop object
                    Stop stop = new Stop(
                        result_set.getString("id"),
                        result_set.getString("name")
                    );

                    // return found stop
                    return Optional.of(stop);
                }

                // return empty object if no stop is found
                return Optional.empty();
            }
        }
    }   
}