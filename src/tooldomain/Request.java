package tooldomain;

import java.sql.*;
import java.util.*;
import java.lang.*;

/**
 * The Request class holds all the methods for the user
 * to make,accept,decline and remove a request
 * @author: Gianna Borgo </gmb5005@rit.edu>
 */
public class Request {

    private Connection connection;

    /**
     * Constructor for request class gets the connection t the database
     * @param connection: to connect to database
     */
    public Request( Connection connection) {
        this.connection = connection;
    }

    /**
     * Used for when a user makes an request
     * @param email: email of person making request
     * @param barcode: barcode of tool to be rented
     * @param duration: the amount of time the person wants the tool for
     * @param dateRequired: the date the person requires the tool on
     * @throws SQLException: exception in case sql errors
     */
    public void MakeRequest( String email, String barcode, int duration, String dateRequired ) throws SQLException {
        Statement statement = this.connection.createStatement();
        //makes sure tool is shareable and exists
        try {
            statement.executeQuery("select * from \"Tool\" where \"Barcode\" = '"+ barcode + "' AND \"Shareable\" = 'true'");
            //makes sure the tool isn't already borrowed
            try {
                statement.executeQuery("select * from \"Borrowed\" where \\\"Barcode\\\" = '\"+ barcode + \"'");
                System.out.println("Tool is currently being borrowed request again at a later date");
            }catch (SQLException e) {
                try {
                    statement.execute("INSERT into \"Request\" (\"Email\", \"Barcode\", \"Duration\", \"DateRequired\") " +
                            "values ( '" + email + "','" + barcode + "'," + duration + ",'" + dateRequired + "')");
                }catch (SQLException i){
                    System.out.println("You have already requested that tool");
                }

            }
        }catch ( SQLException e){
            System.out.println("The Tool You Have Requested isn't Shareable or doesn't exist");
        }
        statement.close();
    }

    /**
     * Changes status of request to accepted
     * @param email: email of user who made request
     * @param barcode: barcode of tool requested
     * @throws SQLException:exception in case sql errors
     */
    public void AcceptRequest( String email, String barcode, String returnBy ) throws SQLException {
        Statement statement = this.connection.createStatement();
        System.out.println("Processing accept request...");
        AcceptDecline( email, barcode, returnBy);
        try {
            statement.execute("UPDATE \"Request\" set \"Status\" = 'Accepted' where \"Email\" = '"
                    + email + "' AND \"Barcode\" = '" + barcode + "'");
            statement.execute("UPDATE \"Request\" set \"ReturnBy\" = '" + returnBy + "' where \"Email\" = '"
                    + email + "' AND \"Barcode\" = '" + barcode + "'");
            statement.execute("INSERT into \"Borrowed\" (\"Email\", \"Barcode\") " +
                    "values ( '" + email + "','" + barcode + "')");
        }catch (SQLException e) {
            System.out.println("Error accepting request");
        }
        statement.close();
    }

    /**
     * Helper method for accepting job offer declines all offer that
     * require tool before return date of accepted offer
     * @param email: email of user who made request
     * @param barcode: barcode of tool requested
     * @throws SQLException:exception in case sql errors
     */
    public  void AcceptDecline( String email, String barcode, String returnBy ) throws SQLException{
        Statement statement = this.connection.createStatement();
        System.out.println("Declining all conflicting request...");
        try {
            ResultSet result = statement.executeQuery("Select * from \"Request\" where \"Email\" = '"
                    + email + "' AND \"Barcode\" = '" + barcode + "'");
            if (result.next()) {
                statement.execute("UPDATE \"Request\" set \"Status\" = 'Declined' where \"DateRequired\" < '"
                        + returnBy + "' And \"Status\" = 'Pending' And \"Barcode\" = '" + barcode + "'");
            }
        }catch (SQLException e){
            System.out.println("No conflicting request found");
        }
        statement.close();
    }

    /**
     * Changes status of request to declined
     * @param email: email of user who made request
     * @param barcode: barcode of tool requested
     * @throws SQLException:exception in case sql errors
     */
    public void DeclineRequest( String email, String barcode ) throws SQLException {
        Statement statement = this.connection.createStatement();
        try {
            statement.executeQuery("select * from \"Request\" where \"Barcode\" = '"+ barcode + "' And \"email\" = '" + email + "'");
            statement.execute("UPDATE \"Request\" set \"Status\" = 'Declined' where \"Email\" = '" + email + "' AND \"Barcode\" = '" + barcode + "'");
        }catch (SQLException e){
            System.out.println("ERROR: No request with email and barcode combination");
        }
        statement.close();
    }

    /**
     * return tool to owner after sharing is completed
     * @param email: email of person borrowing the tool
     * @param barcode: barcode of the tool
     * @throws SQLException: exception in case sql errors
     */
    public void ReturnTool(String email, String barcode ) throws  SQLException{
        Statement statement = this.connection.createStatement();
        try {
            statement.execute("Delete from \"Borrow\" where \"Email\" = '" + email + "' AND \"Barcode\" = '" + barcode + "'");
        }catch (SQLException e){
            System.out.println("ERROR: returning tool");
        }
        statement.close();
    }

    /**
     * Removes a request from the table
     * @param email: email of user who made the request
     * @param barcode: Barcode of tool they requested
     * @throws SQLException: exception in case sql errors
     */
    public void DeleteRequest(String email, String barcode) throws SQLException{
        Statement statement = this.connection.createStatement();
        try {
            statement.execute("Delete from \"Request\" where \"Email\" = '" + email + "' AND \"Barcode\" = '" + barcode + "'");
        }catch (SQLException e){
            System.out.println("ERROR: deleting tool request");
        }
        statement.close();
    }

    /**
     * Gets all request for tools made by user
     * @param email: user email
     * @throws SQLException: exception in case sql errors
     */
    public void getRequestByYou( String email ) throws SQLException {
        Statement statement = this.connection.createStatement();
        try {
            ResultSet result = statement.executeQuery("SELECT * from \"Request\" where \"Email\" = '" + email + "'");
            resultPrint(result);
        }catch (SQLException e){
            System.out.println("No results found with your email");
        }
        statement.close();
    }

    /**
     * Gets all request for tools owned by user
     * @param email: user email
     * @throws SQLException: exception in case sql errors
     */
    public void getRequestForYou( String email ) throws SQLException {
        Statement statement = this.connection.createStatement();
        try{
            ResultSet result = statement.executeQuery("SELECT * from \"Request\" where \"Barcode\" =" +
                    "( SELECT \"Barcode\" from \"Owner\" where \"Owner\".\"Email\" = '" + email + "')");
            resultPrint(result);
        }catch (SQLException e){
            System.out.println("No request for your tools");
        }
        statement.close();
    }

    /**
     * Prints out results in understandable format
     * @param resultSet: the result return from query
     * @throws SQLException: exception in case sql errors
     */
    private void resultPrint( ResultSet resultSet ) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print( rsmd.getColumnName(i) + ": " + columnValue);
            }
            System.out.println();

        }
    }

    /**
     * Helps by getting the tools borrowed by other people
     * who borrowed the same tool
     * @param result: the emails of people who borrowed the same tool
     * @param tools: empty map of tool
     * @param barcode: barcode of tool borrowed first
     * @return: map of all the tools also borrowed
     * @throws SQLException: exception in case sql errors
     */
    public HashMap< String, Integer > getToolRec( ResultSet result, HashMap< String, Integer > tools, String barcode ) throws SQLException{
        Statement statement = this.connection.createStatement();
        ArrayList<String> emails = new ArrayList<>();
        while ( result.next() ){
            emails.add(result.getString(1));
        }
        for( String e: emails){
            result = statement.executeQuery("SELECT \"Barcode\" from \"Request\" where \"Email\" = '"
                    + e + "' AND \"Barcode\" != '" + barcode + "'");
            while (result.next()){
                String s = result.getString(1);
                if( tools != null && tools.containsKey(s)){
                    tools.replace(s, tools.get(s)+1);
                }
                else if(s != null ){
                    try {
                        tools.put(s, 1);
                    }catch ( NullPointerException ex){
                        System.out.println("error getting tools");
                    }

                }
            }
        }
        statement.close();
        return tools;
    }


    /**
     * Sorts the map by highest value to lowest
     * @param hm: map of values
     * @return: map with all values sorted
     */
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list
                = new LinkedList<>(
                hm.entrySet());

        // Sort the list using lambda expression
        list.sort((i1,
                   i2) -> i2.getValue().compareTo(i1.getValue()));

        // put data from sorted list to hashmap
        LinkedHashMap<String, Integer> temp
                = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    /**
     * This function recommends the top ten other tools
     * borrowed by people who also borrowed the same tool
     * @param barcode: barcode of tool just borrowed
     * @throws SQLException: exception in case sql errors
     */
    public void alsoRec( String barcode ) throws SQLException{
        Statement statement = this.connection.createStatement();
        HashMap< String, Integer > tools = new HashMap<>();
        ResultSet result = statement.executeQuery("SELECT \"Email\" from \"Request\" where \"Barcode\" = '" + barcode + "'");
        tools = getToolRec(result, tools, barcode);
        tools = sortByValue(tools);
        int x = 0;
        for( String s: tools.keySet() ){
            if( x < 10 ){
                result = statement.executeQuery("SELECT * from \"Tool\" where \"Barcode\" = '" + s + "'");
                resultPrint(result);
                x++;
            }
        }
        statement.close();
    }


}
