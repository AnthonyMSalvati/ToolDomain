package tooldomain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class Main {

    private static Connection connection;


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //Request r = new Request("jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32001a", "Hoh2saikaequeic5piut", "p32001a", "true");
        Class.forName("org.postgresql.Driver");
        connection = new DatabaseConnection(
                "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32001a",
                "Hoh2saikaequeic5piut",
                "p32001a",
                "true" ).getConnection();

        displayInitialMessage();

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String userInput = scanner.nextLine();

        if (userInput.equals("1")){
            System.out.println("Please enter your username: ");
            String userName = scanner.nextLine();
            String query = String.format("SELECT * FROM \"User\" WHERE \"Username\" = \'%s\'", userName);
            ResultSet result = connection.createStatement().executeQuery(query);
            System.out.println(result.getString("Username"));
        }
        else if (userInput.equals("2")){

        }
    }

    public static void displayInitialMessage() {
        System.out.println("=================================");
        System.out.println("= Welcome to the tool database! =");
        System.out.println("=================================");
        System.out.println("If you have an account already, enter 1 and press return.");
        System.out.println("If you do not already have an account, enter 2 and press return.");
    }


}
