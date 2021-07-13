package tooldomain;
import javax.xml.transform.Result;
import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {

    private Connection connection;

    public DatabaseConnection(String url, String password, String username, String ssl) throws SQLException, ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
        connect(url, password, username, ssl);
    }

    private void connect(String url, String pwd, String username, String ssl) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", pwd);
        properties.setProperty("ssl", "require");

        this.connection = DriverManager.getConnection(url,properties);
    }

    public Connection getConnection(){
        return this.connection;
    }
}
