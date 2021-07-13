package tooldomain;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;


public class ToolSearch {

    private Connection connection;
    private String searchType;

    public ToolSearch(String toolToSearch, String typeOfSearch) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        this.connection = connection = new DatabaseConnection(
                "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32001a",
                "Hoh2saikaequeic5piut",
                "p32001a",
                "true" ).getConnection();
        this.searchType = typeOfSearch;

        processResult(search(toolToSearch));
        connection.close();
    }

    private ResultSet search(String query) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = null;

        String search = String.format("SELECT * FROM \"Tool\" LEFT JOIN \"Tool Categories\" ON \'Tool.Barcode\' = \'\"Tool Categories\".Barcode\' WHERE \"%s\" = \'%s\'", searchType, query);
        System.out.println(search);
        result = statement.executeQuery(search);
        return result;
    }

    private void processResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData meta = resultSet.getMetaData();
        while (resultSet.next())
        {
            for (int i = 1; i < meta.getColumnCount(); i++){
                if (i > 1) { System.out.print("\t");}
                String columnVal = resultSet.getString(i);
                System.out.print(columnVal + " " + meta.getColumnName(i));
            }
            System.out.println("");
        }
        resultSet.close();
        connection.close();
    }
}
