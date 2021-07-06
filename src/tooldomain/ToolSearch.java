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

    public ToolSearch(String url, String password, String username, String ssl) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        this.connection = configure(url, password, username, ssl);
        processResult(search(""));
    }

    private Connection configure(String url, String pwd, String username, String ssl) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", pwd);
        properties.setProperty("ssl", "require");

        Connection connection = DriverManager.getConnection(url,properties);
        return connection;
    }

    private ResultSet search(String query) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = null;

        //test statement for now to query database
        result = statement.executeQuery("SELECT * FROM Owner");
        return result;
    }

    private void processResult(ResultSet resultSet) throws SQLException {
        while (resultSet.next())
        {
            String email = resultSet.getString(1);
            /*Date createDate = resultSet.getDate(1);
            String username = resultSet.getString(2);
            String password = resultSet.getString(3);
            String firstName = resultSet.getString(4);
            String lastName = resultSet.getString(5);
            Date lastAccess = resultSet.getDate(6);*/

            System.out.println("email = " + email);
            //System.out.println("Create Date = " + createDate.toString());
            //System.out.println("username = " + username);


        }
        resultSet.close();
        connection.close();
    }
}
