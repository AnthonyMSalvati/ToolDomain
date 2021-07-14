package tooldomain;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                    break;
                }
                case (2) -> {
                    searchForTools();
                    onApplicationMenu = false;
                    break;
                }
                case (3) -> {
                    viewToolList();
                    onApplicationMenu = false;
                    break;
                }
                case (4) -> {
                    manageRequests();
                    onApplicationMenu = false;
                    break;
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

    public void manageTools(

    ) throws SQLException, ClassNotFoundException {
        this.connection = connection = new DatabaseConnection(
                "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32001a",
                "Hoh2saikaequeic5piut",
                "p32001a",
                "true" ).getConnection();
        Statement statement = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet result = null;
        System.out.println("-----------------------------");
        System.out.println("What do you want to do to your catalog");
        System.out.println("-----------------------------");
        System.out.println("1. Add tool to catalog 2. Remove tool from catalog");
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input = scanner.nextLine();
        switch (Integer.parseInt(input)){
            case (1) -> {
                System.out.println("Input your email address");
                String email = scanner.nextLine();
                System.out.println("Input barcode number for tool to add");
                int val = scanner.nextInt();
                String newVal = Integer.toString(val);
                String query = "INSERT INTO Owner (Email, BarCode) VALUES (" + email + ',' + newVal +" );";
                result = statement.executeQuery(query);
            }
            case (2) -> {
                System.out.println("Input your email address");
                String email = scanner.nextLine();
                System.out.println("Input barcode number for tool to remove");
                int val = scanner.nextInt();
                String newVal = Integer.toString(val);
                String newQuery = "IF EXISTS(SELECT * FROM Your_table WHERE email = "+ email +")\n" +
                        "BEGIN\n" +
                        "   DELETE FROM Owner WHERE Barcode= " + newVal + "\n" +
                        "END";
                result = statement.executeQuery(newQuery);
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

    public void viewToolList(){
        System.out.println("list");
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
                request.AcceptRequest(input, barcode);
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
