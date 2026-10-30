import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database
{
    // open connection to DB 
    public static Connection open_connection() throws SQLException
    {
        // read connection settings from environment variables
        String db_url = System.getenv("STAINEX_DB_URL");
        String db_user = System.getenv("STAINEX_DB_USER");
        String db_password = System.getenv("STAINEX_DB_PASSWORD");

        // reject missing connection settings
        if (db_url == null || db_user == null || db_password == null)
        {
            throw new IllegalStateException("Database environment variables are not set");
        }

        // return opened connection
        return DriverManager.getConnection(db_url, db_user, db_password);
    }
}