package tooldomain;

import java.sql.*;
import java.util.Properties;

/**
 * The Request class holds all the methods for the user
 * to make,accept,decline and remove a request
 * @author: Gianna Borgo </gmb5005@rit.edu>
 */
public class Request {

    private Connection connection;

    public Request(String url, String password, String username, String ssl) throws SQLException, ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
        this.connection = configure(url, password, username, ssl);
        DeleteRequest("test125@rit.edu", "145");
    }

    /**
     * connects to the database server
     * @param url: server url
     * @param pwd: password
     * @param username: the user's username
     * @param ssl
     * @return connection
     * @throws SQLException
     */
    private Connection configure(String url, String pwd, String username, String ssl) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", pwd);
        properties.setProperty("ssl", "require *");

        Connection connection = DriverManager.getConnection(url,properties);
        return connection;
    }

    /**
     * Used for when a user makes an request
     * @param email: email of person making request
     * @param barcode: barcode of tool to be rented
     * @param duration: the amount of time the person wants the tool for
     * @param dateRequired: the date the person requires the tool on
     * @throws SQLException
     */
    public void MakeRequest( String email, String barcode, int duration, String dateRequired ) throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.executeQuery("INSERT into \"Request\" (\"Email\", \"Barcode\", \"Duration\", \"DateRequired\") " +
                "values ( '" + email + "','"+ barcode + "'," + duration + ",'" + dateRequired + "')");
    }

    /**
     * Changes status of request to accepted
     * @param email: email of user who made request
     * @param barcode: barcode of tool requested
     * @throws SQLException
     */
    public void AcceptRequest( String email, String barcode ) throws SQLException {
        Statement statement = this.connection.createStatement();
        AcceptDecline( email, barcode);
        statement.execute("UPDATE \"Request\" set \"Status\" = 'Accepted' where \"Email\" = '"
                + email + "' AND \"Barcode\" = '" + barcode + "'");
        statement.execute("UPDATE \"Owner\" set \"Email\" = '" + email + "' where \"Barcode\" = '" + barcode + "'");
    }

    /**
     * Helper method for accepting job offer declines all offer that
     * require tool before return date of accepted offer
     * @param email: email of user who made request
     * @param barcode: barcode of tool requested
     * @throws SQLException
     */
    public  void AcceptDecline( String email, String barcode ) throws SQLException{
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery("Select * from \"Request\" where \"Email\" = '"
                + email + "' AND \"Barcode\" = '" + barcode + "'");
        if(result.next()){
            String date = result.getString("DateRequired");
            int dur = Integer.parseInt(result.getString("Duration"));
            dur += Integer.parseInt(date.substring(9));
            String returnDate = date.substring(0,9) + dur;
            statement.execute("UPDATE \"Request\" set \"Status\" = 'Declined' where \"DateRequired\" < '"
                    + returnDate +"' And \"Status\" = 'Pending' And \"Barcode\" = '" + barcode + "'");
        }
    }

    /**
     * Changes status of request to declined
     * @param email: email of user who made request
     * @param barcode: barcode of tool requested
     * @throws SQLException
     */
    public void DeclineRequest( String email, String barcode ) throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.execute("UPDATE \"Request\" set \"Status\" = 'Declined' where \"Email\" = '" + email + "' AND \"Barcode\" = '" + barcode + "'");
    }

    /**
     * return tool to owner after sharing is completed
     * @param email: email of the tools original owner
     * @param barcode: barcode of the tool
     * @throws SQLException
     */
    public void ReturnTool(String email, String barcode ) throws  SQLException{
        Statement statement = this.connection.createStatement();
        statement.execute("Update \"Owner\" set \"Email\" = '" + email + "' where \"Barcode\" = '" + barcode + "'");
    }

    /**
     * Removes a request from the table
     * @param email: email of user who made the request
     * @param barcode: Barcode of tool they requested
     * @throws SQLException
     */
    public void DeleteRequest(String email, String barcode) throws SQLException{
        Statement statement = this.connection.createStatement();
        statement.execute("Delete from \"Request\" where \"Email\" = '" + email + "' AND \"Barcode\" = '" + barcode + "'");
    }

}
