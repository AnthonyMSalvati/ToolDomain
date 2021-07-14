package tooldomain;

import javax.tools.Tool;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Application {
    private Connection connection;

    public Application() throws SQLException, ClassNotFoundException {
        runApplication();
    }

    public void displayMenu(){
        System.out.println("---------------------------------------");
        System.out.println("What would you like to do this session?");
        System.out.println("---------------------------------------");
        System.out.println("1. Manage your tools \t 4. Manage Requests");
        System.out.println("2. Search for a tool \t 5. Quit");
        System.out.println("3. View the tool list");
    }

    public void runApplication() throws SQLException, ClassNotFoundException {
        displayMenu();
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        boolean onApplicationMenu = true;
        while (onApplicationMenu){
            String response = scanner.nextLine();
            switch (Integer.parseInt(response)) {
                case (1) -> {
                    manageTools();
                    onApplicationMenu = false;
                }
                case (2) -> {
                    searchForTools();
                    onApplicationMenu = false;
                }
                case (3) -> {
                    viewToolList();
                    onApplicationMenu = false;
                }
                case (4) -> {
                    manageRequests();
                    onApplicationMenu = false;
                }
                case (5) -> {
                    System.out.println("Thank you! Have a wonderful day!");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Sorry, that is not a valid option.");
                }
            }
        }
    }
    public void manageTools() throws ClassNotFoundException, SQLException {

        Class.forName("org.postgresql.Driver");
        connection = new DatabaseConnection(
                "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32001a",
                "Hoh2saikaequeic5piut",
                "p32001a",
                "true" ).getConnection();

        System.out.println("------------------------------------------");
        System.out.println("What would you like to do to your catalog?");
        System.out.println("------------------------------------------");
        System.out.println("1. Add Tool \t 2. Delete Tool");
        System.out.println("3. Edit Tool \t 4. Quit");

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input = scanner.nextLine();
        String order ="";

        switch (Integer.parseInt(input)){
            case (1) -> {
                System.out.println("Please enter your email address");
                String email = scanner.nextLine();
                System.out.println("Please enter your the barcode number for the tool to add");
                int value = scanner.nextInt();
                String query = "INSERT INTO Owner (Email, Barcode)" +
                        "VALUES " +  email+ "," + value + ");";
                connection.createStatement().executeQuery(query);
            }
            case (2) -> {
                System.out.println("Please enter your email address");
                String email = scanner.nextLine();
                System.out.println("Please enter your the barcode number for the tool to delete");
                int value = scanner.nextInt();
                String query = "DELETE FROM Owners" +
                        "WHERE email = "+ email +
                        "AND barcode = " + value + ";";
                connection.createStatement().executeQuery(query);
            }
            case (3) -> {

            }
            case (4) -> {

            }
        }

    }

    public void searchForTools() throws SQLException, ClassNotFoundException {
        System.out.println("search");
        System.out.println("-----------------------------");
        System.out.println("How would you like to search?");
        System.out.println("-----------------------------");
        System.out.println("1. By Barcode \t 3. By Name");
        System.out.println("2. By Category \t 4. Return to menu");

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input = scanner.nextLine();
        String order ="";

        switch (Integer.parseInt(input)){
            case (1) -> {
                System.out.println("Please enter the barcode of the tool you would like to find: ");
                input = scanner.nextLine();
                boolean hasSpecified = false;
                while (!hasSpecified){
                    System.out.println("Would you like the results in ascending (ASC) or descending (DESC) order?");
                    order = scanner.nextLine();
                    if (!(order.equals("ASC") || order.equals("DESC"))){
                        System.out.println("Please specify either ASC or DESC.");
                    }
                    else hasSpecified = true;
                }

                ToolSearch search = new ToolSearch(input, "Barcode", order);
            }
            case (2) -> {
                System.out.println("Please enter the category you would like to search within: ");
                input = scanner.nextLine();
                boolean hasSpecified = false;
                while (!hasSpecified){
                    System.out.println("Would you like the results in ascending (ASC) or descending (DESC) order?");
                    order = scanner.nextLine();
                    if (!(order.equals("ASC") || order.equals("DESC"))){
                        System.out.println("Please specify either ASC or DESC.");
                    }
                    else hasSpecified = true;
                }
                ToolSearch search = new ToolSearch(input, "Category", order);
            }
            case (3) -> {
                System.out.println("Please enter the name of the tool you would like to find: ");
                input = scanner.nextLine();
                boolean hasSpecified = false;
                while (!hasSpecified){
                    System.out.println("Would you like the results in ascending (ASC) or descending (DESC) order (by name)?");
                    order = scanner.nextLine();
                    if (!(order.equals("ASC") || order.equals("DESC"))){
                        System.out.println("Please specify either ASC or DESC.");
                    }
                    else hasSpecified = true;
                }
                ToolSearch search = new ToolSearch(input, "Name", order);
            }
            case (4) -> {
                runApplication();
            }
        }
    }

    public void viewToolList() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        this.connection =  new DatabaseConnection(
                "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32001a",
                "Hoh2saikaequeic5piut",
                "p32001a",
                "true" ).getConnection();

        System.out.println("------------------------------");
        System.out.println("What would you like to search?");
        System.out.println("------------------------------");
        System.out.println("1. Available Tools \t 2. Lent tools");
        System.out.println("3. Borrowed Tools");

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input = scanner.nextLine();

        switch (Integer.parseInt(input)){

            case (1) -> {
                System.out.println("---------------");
                System.out.println("Available Tools");
                System.out.println("---------------");
                PreparedStatement state = connection.prepareStatement("SELECT * FROM \"Tool\" ORDER BY \"Tool\".\"Name\" ASC");
                ResultSet result = state.executeQuery();
                while (result.next()){
                    System.out.println(result.getString("Name"));
                }

            }
            case (2) -> {
                System.out.println("----------");
                System.out.println("Lent Tools");
                System.out.println("----------");
                PreparedStatement state = connection.prepareStatement("SELECT t.*, CTID " +
                        "FROM public.\"Tool\" t " +
                        "LIMIT 5013");
                ResultSet result = state.executeQuery();
                while (result.next()){
                    System.out.println(result.getString("Name"));
                }

            }
            case (3) -> {
                System.out.println("--------------");
                System.out.println("Borrowed Tools");
                System.out.println("--------------");
                connection.prepareStatement("SELECT t.*, CTID " +
                        "FROM public.\"Tool\" t " +
                        "LIMIT 5013");

            }
            case (4) -> {
                System.out.println("Thank you! Have a wonderful day!");
                System.exit(0);
            }
        }
        connection.close();
    }


    /**
     * Prints out the request options and
     * manages their input
     */
    public void manageRequests() throws SQLException, ClassNotFoundException {
        System.out.println("Manage Request");
        System.out.println("1. Make Request \t 5. View request for your tools");
        System.out.println("2. Cancel Request \t 6. View your request");
        System.out.println("3. Accept Request \t 7. Return a tool");
        System.out.println("4. Decline Request \t 8. Return to main menu");

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input = scanner.nextLine();
        System.out.println("What's your email?");
        String email = scanner.nextLine();

        Request request = new Request(Main.getConnection());
        switch (Integer.parseInt(input)){
            case (1) -> {
                System.out.println("What's the barcode of the tool you wish to borrow?");
                input = scanner.nextLine();
                System.out.println("What date do you required the tool by? (mm/dd/yyyy)");
                String dateRequired = scanner.nextLine();
                System.out.println("How long do you need the tool for?(numbers only)");
                int duration = scanner.nextInt();
                request.MakeRequest(email, input, duration, dateRequired);
            }
            case (2) -> {
                System.out.println("What's the barcode of the tool of the request you wish to cancel?");
                input = scanner.nextLine();
                request.DeleteRequest(email, input);
            }
            case (3) -> {
                System.out.println("What's the username of user who requested the tool?");
                input = scanner.nextLine();
                System.out.println("What's the barcode of the tool the user requested?");
                String barcode = scanner.nextLine();
                System.out.println("What date do you what the tool returned by?(mm/dd/yyyy)");
                String returnby = scanner.nextLine();
                request.AcceptRequest(input, barcode, returnby);
            }
            case (4) -> {
                System.out.println("What's the username of user who requested the tool?");
                input = scanner.nextLine();
                System.out.println("What's the barcode of the tool the user requested?");
                String barcode = scanner.nextLine();
                request.DeclineRequest(input, barcode);
            }
            case (5) -> {
                request.getRequestForYou(email);
            }
            case (6) -> {
                request.getRequestByYou(email);
            }
            case (7) -> {
                System.out.println("Whats the barcode of the tool you wish to return?");
                input = scanner.nextLine();
                request.ReturnTool(email,input);
            }
            case (8) -> {
                runApplication();
            }
        }
    }

}
