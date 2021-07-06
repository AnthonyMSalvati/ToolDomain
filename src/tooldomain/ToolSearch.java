package tooldomain;
import javax.xml.transform.Result;
import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class ToolSearch {

    private Connection connection;

    public ToolSearch(){

    }

    public ToolSearch(String url, String password, String username, String ssl) throws SQLException {
        this.connection = configure(url, password, username, ssl);
        processResult(search(""));
    }

    private Connection configure(String url, String pwd, String username, String ssl) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", pwd);
        properties.setProperty("ssl", ssl);

        Connection connection = DriverManager.getConnection(url,properties);
        return connection;
    }

    private ResultSet search(String query) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = null;

        //test statement for now to query database
        result = statement.executeQuery("SELECT * FROM USER");
        return result;
    }

    private void processResult(ResultSet resultSet) throws SQLException {
        while (resultSet.next())
        {
            String email = resultSet.getString("Email");
            Date createDate = resultSet.getDate("Creation Date");
            String username = resultSet.getString("Username");
            String password = resultSet.getString("Password");
            String firstName = resultSet.getString("First Name");
            String lastName = resultSet.getString("Last Name");
            Date lastAccess = resultSet.getDate("Last Access Date");

            System.out.println("email = " + email);
            System.out.println("Create Date = " + createDate.toString());
            System.out.println("username = " + username);


        }
        resultSet.close();
        connection.close();
    }
}
