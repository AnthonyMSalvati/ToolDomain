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
        Class.forName("org.postgresql.Driver");
        connection = new DatabaseConnection(
                "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32001a",
                "Hoh2saikaequeic5piut",
                "p32001a",
                "true" ).getConnection();

        displayInitialMessage();
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        while (!userLoggedIn){
            int passwordAttempts = 0;
            boolean passwordVerified = false;
            String userInput = scanner.nextLine();

            while (!(userInput.equals("1") || userInput.equals("2"))){
                System.out.println("Unexpected input. Please input either 1 or 2.");
                userInput = scanner.nextLine();
            }
            if (userInput.equals("1")){
                System.out.println("Please enter your username: ");
                String userName = scanner.nextLine();
                String query = String.format("SELECT * FROM \"User\" WHERE \"Username\" = \'%s\'", userName);
                ResultSet result = connection.createStatement().executeQuery(query);

                if (!result.next())
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
                if(registerUser()){
                    userLoggedIn = true;
                }
            }
        }
        Application application = new Application();
        connection.close();


    }

    public static void displayInitialMessage() {
        System.out.println("=================================");
        System.out.println("= Welcome to the tool database! =");
        System.out.println("=================================");
        System.out.println("If you have an account already, enter 1 and press return.");
        System.out.println("If you do not already have an account, enter 2 and press return.");
    }

    public static boolean registerUser() throws SQLException {

        // default ERROR values if they somehow aren't set
        String email = "ERROR";
        String username = "ERROR";
        String password = "ERROR";
        String firstName = "ERROR";
        String lastName = "ERROR";

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        boolean emailUnique = false;
        while (!emailUnique){
            System.out.println("Registering is easy! Enter your email address:");
            email = scanner.nextLine(); //@TODO: verify string is valid-looking
            System.out.println(String.format("Is this correct? (Y/N) %s", email));
            String response = scanner.nextLine();
            String query = String.format("SELECT * FROM \"User\" WHERE \"Email\" = \'%s\'", email);
            ResultSet result = connection.createStatement().executeQuery(query);
            if (result.next()){ // returns false when email not found
                System.out.println("This email is already taken, please try another.");
            } else {
                emailUnique = true;
            }

            while (!(response.equals("Y") || response.equals("y"))){
                System.out.println("Please re-enter your email address");
                email = scanner.nextLine();
                System.out.println(String.format("Is this correct? (Y/N) %s", email));
                response = scanner.nextLine();
            }
        }


        System.out.println("Please enter a username. This is what you will use to log in: ");
        username = scanner.nextLine();
        boolean usernameUnique = false;
        while (!usernameUnique){
            String query = String.format("SELECT * FROM \"User\" WHERE \"Username\" = \'%s\'", username);
            ResultSet result = connection.createStatement().executeQuery(query);
            result.next();
            if (result.next()){ // same logic as email checking
                System.out.println("Username already taken, please try again.");
                username = scanner.nextLine();
            } else {
                usernameUnique = true;
            }
        }

        boolean passwordVerified = false;
        while (!passwordVerified){
            System.out.println("Please create a memorable password: ");
            password = scanner.nextLine();
            System.out.println("Please re-enter your password: ");
            if (!password.equals(scanner.nextLine())){
                System.out.println("Passwords did not match, please try again.");
            } else {
                passwordVerified = true;
            }
        }

        System.out.println("Almost done! Please enter your first name: ");
        firstName = scanner.nextLine();
        System.out.println("Last step! Please enter your last name: ");
        lastName = scanner.nextLine();
        long millis = System.currentTimeMillis();
        java.sql.Date createDate = new java.sql.Date(millis);
        //Date time = Calendar.getInstance().getTime();
        //DateFormat dateFormat = new SimpleDateFormat("yyy-mm-dd");
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO \"User\" values(?,?,?,?,?,?,?)");
        stmt.setString(1, email);
        stmt.setDate(2, createDate);
        stmt.setString(3,username);
        stmt.setString(4, password);
        stmt.setString(5, firstName);
        stmt.setString(6, lastName);
        stmt.setDate(7, createDate);

        if (stmt.executeUpdate() >=0 ){
            return true;
        } else {
            return false;
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
