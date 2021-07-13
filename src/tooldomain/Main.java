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
    private static boolean userLoggedIn;

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
        while (!userLoggedIn){
            int passwordAttempts = 0;
            boolean passwordVerified = false;
            while (!(userInput.equals("1") || userInput.equals("2"))){
                System.out.println("Unexpected input. Please input either 1 or 2.");
                userInput = scanner.nextLine();
            }
            if (userInput.equals("1")){
                System.out.println("Please enter your username: ");
                String userName = scanner.nextLine();
                String query = String.format("SELECT * FROM \"User\" WHERE \"Username\" = \'%s\'", userName);
                ResultSet result = connection.createStatement().executeQuery(query);
                result.next();
                if (!result.getString("Username").equals(userName))
                {
                    System.out.println("Username not found. Please press 2 to create an account before continuing.");
                    continue;
                }
                System.out.println("Please enter your password: ");
                String password = scanner.nextLine();
                while (passwordAttempts < 3 && !passwordVerified){
                    if (password.equals(result.getString("Password"))){
                        passwordVerified = true;
                        userLoggedIn = true;
                        System.out.println(String.format("Login Successful! Welcome %s", userName));
                        //continue;
                    }
                    else {
                        passwordAttempts++;
                        String response = String.format("Incorrect password, %d tries remaining", 3-passwordAttempts);
                        System.out.println(response);
                        password = scanner.nextLine();
                    }
                }
                if (passwordAttempts == 3 && userLoggedIn == false){
                    System.out.println("Maxmimum login attempts reached. The program will now exit.");
                    System.exit(1);
                }

            }
            else if (userInput.equals("2")){

            }
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
