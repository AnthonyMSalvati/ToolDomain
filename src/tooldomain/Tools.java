package tooldomain;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * The Tool class handles function to get user's tool data
 * such as number of tools lent/borrowed/available and
 * the frequent tools borrowed/lent
 * @author: Gianna Borgo </gmb5005@rit.edu>
 */
public class Tools {
    private Connection connection;

    /**
     * Constructor for Tools class gets the connection to the database
     * @param connection: to connect to database
     */
    public Tools( Connection connection) throws SQLException{
        this.connection = connection;
    }

    /**
     * adds tool to database
     * @param email:user emain
     * @param barcode: tools barcode
     * @param description: tool description
     * @param share: is Sharable?
     * @param price: tool's price
     * @param name: tool's name
     * @param date: tools creation date
     * @throws SQLException
     */
    public void addTool(String email, String barcode, String description, boolean share, String price, String name, java.sql.Date date ) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.execute("INSERT INTO \"Owner\" (\"Email\", \"Barcode\")  values ( '" + email + "', '" + barcode + "')");
            statement.execute("INSERT INTO \"Tool\" (\"Barcode\", \"Description\", \"Shareable\", \"Price\", " +
                    "\"Name\", \"PurchaseDate\") values ('" + barcode + "', '" + description + "', '" +
                    share + "', '" + price + "', '" + name + "', '" + date + "')");

        }catch ( SQLException e){
            System.out.println("Problem adding tool");
        }
        statement.close();

    }

    /**
     * delete tool from database
     * @param barcode: barcode of tool
     * @throws SQLException: for sql errors
     */
    public void deleteTool( String barcode ) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.execute("DELETE from \"Tool\" where \"Barcode\" = '" + barcode + "'");
        }catch (SQLException e){
            System.out.println("error with deleting tool");
        }
        statement.close();
    }



    /**
     * Gets the number of lent tools
     * @param email: email of user
     * @throws SQLException: exception in case sql errors
     */
    public void getNumberLentTools( String email ) throws SQLException{
        Statement statement = this.connection.createStatement();
        ResultSet results;
        try {
            results = statement.executeQuery("SELECT * from \"Borrowed\" where \"Barcode\" IN " +
                    "( SELECT \"Barcode\" from \"Owner\" where \"Owner\".\"Email\" = '" + email + "')");
            int num = getNumResults(results);
            System.out.println("Number of Lent Tools: " + num);
        }catch ( SQLException e ){
            System.out.println("You have no lent tools");
        }
    }

    /**
     * Gets the number of available tools
     * @param email: email of user
     * @throws SQLException: exception in case sql errors
     */
    public void getNumberAvailableTool( String email ) throws SQLException{
        Statement statement = this.connection.createStatement();
        ResultSet results;
        try{
            results = statement.executeQuery("SELECT * from \"Tool\" where \"Barcode\" IN (SELECT \"Owner\".\"Barcode\" " +
                    "from \"Owner\" where \"Email\" = '" + email + "' AND \"Owner\".\"Barcode\" " +
                    "not in (SELECT  \"Barcode\" from \"Borrowed\"))");
            int num = getNumResults(results);
            System.out.println("Number of Available Tools: " + num );
        }catch ( SQLException e ){
            System.out.println("You have no available tools");
        }
        statement.close();
    }

    /**
     * gets the number of borrowed tools by the user
     * @param email: email of user
     * @throws SQLException: exception in case sql errors
     */
    public void  getNumberBorrowedTools( String email) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet results;
        try {
            results = statement.executeQuery("SELECT * from \"Tool\" where \"Barcode\" " +
                    "IN (SELECT \"Barcode\" from \"Borrowed\" where \"Email\" = '" + email + "')");
            int num = getNumResults(results);
            System.out.println("Number of Borrowed Tools: " + num );

        }catch ( SQLException e ){
            System.out.println("No tools currently borrowed.");
        }
        statement.close();
    }

    /**
     * gets the frequently lent tools by a user
     * only goes up to 10
     * @param email: email of user
     * @throws SQLException: exception in case sql errors
     */
    public void getFrequentlyLentTools( String email ) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet results;
        HashMap< String, Integer > tools = new LinkedHashMap<>();
        results = statement.executeQuery("SELECT \"Duration\", \"Barcode\" from \"Request\" where \"Barcode\" IN " +
                "( SELECT \"Barcode\" from \"Owner\" where \"Owner\".\"Email\" " +
                "= '" + email + "' AND \"Status\" = 'Accepted' )");
        int numTools = resultsAmount(results);
        tools = simplifyResults(results, tools);
        tools = Request.sortByValue(tools);
        int x = 0;
        double totTimelent = 0;
        System.out.println("Top 10 Frequently Lent Tools");
        if( !tools.isEmpty()) {
            for (String s : tools.keySet()) {
                if (x < 10) {
                    results = statement.executeQuery("Select * from \"Tool\" where \"Barcode\" = '" + s + "'");
                    Request.resultPrint(results);
                    totTimelent += tools.get(s);
                    x++;
                }
            }
        }else{
            System.out.println("You have no lent tools");
        }
        String pattern = "#.###";
        DecimalFormat decimalFormat =  new DecimalFormat(pattern);
        double avgTimeLent = totTimelent / numTools;
        System.out.println("The average time a tool is lent is " + decimalFormat.format(avgTimeLent) + " days");
        statement.close();
    }


    /**
     * Gets the most frequently borrowed tools from a user
     * only goes up to 10 and the average time lent of the tools
     * @param email: email of user
     * @throws SQLException: exception in case sql errors
     */
    public void getFrequentlyBorrowedTools( String email ) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet results;
        HashMap< String, Integer > tools = new LinkedHashMap<>();
        try {
            results = statement.executeQuery("Select \"Duration\", \"Barcode\" from \"Request\" where " +
                    "\"Email\" = '" + email + "' AND \"Status\" = 'Accepted'");
            simplifyResults(results, tools);
            tools = Request.sortByValue(tools);
            int x = 0;
            System.out.println("Top 10 Frequently Borrowed Tools");
            if( !tools.isEmpty()) {
                for (String s : tools.keySet()) {
                    if (x < 10) {
                        results = statement.executeQuery("Select * from \"Tool\" where \"Barcode\" = '" + s + "'");
                        Request.resultPrint(results);
                        x++;
                    }
                }
            }else{
                System.out.println("You have no borrowed tools");
            }
        }catch ( SQLException e ){
            System.out.println("You have no borrowed tools");
        }
        statement.close();
    }

    /**
     * Fixes the results of the query by putting it into map
     * such as getting rid of duplicates and adding the durations
     * together for the value while tool barcode is key
     * Also counts the number of tools used in top 10 so total number
     * of tools is counted including duplicates
     * @param result: the resultSet of query containing all tools
     * @param tools: The map to put the edited results
     * @return: map of tools
     * @throws SQLException: exception in case sql errors
     */
    public HashMap< String, Integer > simplifyResults( ResultSet result, HashMap< String, Integer > tools ) throws SQLException{
        Statement statement = this.connection.createStatement();
        int x = 0;
        while ( result.next() ){
                String s = result.getString(2);
                if( tools != null && tools.containsKey(s)){
                    tools.replace(s, tools.get(s) + result.getInt(1));
                }
                else if(s != null ){
                    tools.put(s, result.getInt(1));
                    x++;
                }
        }
        statement.close();
        return tools;
    }

    /**
     * gets the number of tools used in top 10 to
     * count for more than one of the same tool
     * @param result: the resultSet of query containing all tools]
     * @return: total number of tools for top 10
     * @throws SQLException: exception in case sql errors
     */
    public int resultsAmount( ResultSet result) throws SQLException{
        Statement statement = this.connection.createStatement();
        HashMap<String, Integer> tools = new LinkedHashMap<>();
        int x = 0;
        int totNumTools = 0;
        while ( result.next() ){
            String s = result.getString(2);
            if( x < 10 ){
                totNumTools++;
            }
            if( tools != null && !tools.containsKey(s)){
                x++;
                tools.put(s,1);
            }
        }
        statement.close();
        return totNumTools;
    }


    /**
     * gets the number of results from query
     * @param resultSet: the result return from query
     * @throws SQLException: exception in case sql errors
     */
    public int getNumResults( ResultSet resultSet ) throws SQLException {
        int num = 0;
        while (resultSet.next()) {
            num++;
        }
        return num;
    }
}
