package tooldomain;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;


public class ToolSearch {

    private Connection connection;
    private String searchType;
    private String ordering;

    public ToolSearch(String toolToSearch, String typeOfSearch, String resultOrdering) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        this.connection = connection = new DatabaseConnection(
                "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32001a",
                "Hoh2saikaequeic5piut",
                "p32001a",
                "true" ).getConnection();
        this.searchType = typeOfSearch;
        this.ordering = resultOrdering;
        processResult(search(toolToSearch));
        connection.close();
    }

    private ResultSet search(String query) throws SQLException {
        Statement statement = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet result = null;

        String search = String.format("SELECT \"Tool\".\"Barcode\", \"Tool\".\"Name\" FROM \"Tool\" " +
                        "LEFT JOIN \"Tool Categories\" " + "ON \"Tool\".\"Barcode\" = \"Tool Categories\".\"Barcode\" " +
                        "WHERE \"%s\" = \'%s\' ORDER BY \"Tool\".\"Name\" ASC", searchType, query);
        System.out.println(search);
        result = statement.executeQuery(search);
        return result;
    }

    private void processResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData meta = resultSet.getMetaData();
        //System.out.println(resultSet.next());
        if (resultSet.next()){
            for (int i = 1; i <= meta.getColumnCount(); i++){
                if (i > 1) { System.out.print("");}
                System.out.printf("%-10s", meta.getColumnName(i));
            }
            System.out.println("");
            resultSet.previous();
        }
        while (resultSet.next())
        {
            for (int i = 1; i <= meta.getColumnCount(); i++){
                String columnVal = resultSet.getString(i);
                System.out.printf("%-10s", columnVal);
            }
            System.out.println("");
        }
        resultSet.close();
        connection.close();
    }
}
