import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Note: URL ends with 'bankingsystem' to match your schema
    private static final String URL = "jdbc:mysql://localhost:3306/bankingsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "password"; // CHANGE THIS

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}