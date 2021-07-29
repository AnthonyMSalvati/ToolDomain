package tooldomain;

import java.io.InputStreamReader;
import java.sql.*;
import java.util.Date;
import java.util.Scanner;

/**
 * The Application handles te main running the of the programs
 * including allowing the user to pick options to edit and view tools and requests
 *
 * @author: Gianna Borgo </gmb5005@rit.edu>
 */
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
        System.out.println("3. View the tool list \t 6. View DashBoard");
        System.out.println("7. View Statistics");
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
                case (6) -> viewDash();
                case (7) -> viewSats();
                default -> System.out.println("Sorry, that is not a valid option.");
            }
        }
    }
    public void manageTools() throws ClassNotFoundException {

        Class.forName("org.postgresql.Driver");

        System.out.println("------------------------------------------");
        System.out.println("What would you like to do to your catalog?");
        System.out.println("------------------------------------------");
        System.out.println("1. Add Tool \t 2. Delete Tool");
        System.out.println("3. Edit Tool \t 4. Quit");
        System.out.println("5. Manage Categories");

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input = scanner.nextLine();

        switch (Integer.parseInt(input)){
            case (1) -> {
                System.out.println("Please enter your email address");
                String email = scanner.nextLine();
                System.out.println("Please enter your the barcode number for the tool to add");
                int value = scanner.nextInt();
                String query = "INSERT INTO Owner (Email, Barcode)" +
                        "VALUES " +  email+ "," + value + ");";
                try{
                    connection.createStatement().executeQuery(query);

                }
                catch(SQLException e){
                    System.out.println("Error with Adding barcode");
                }

            }
            case (2) -> {
                System.out.println("Please enter your email address");
                String email = scanner.nextLine();
                System.out.println("Please enter your the barcode number for the tool to delete");
                int value = scanner.nextInt();
                String query = "DELETE FROM Owners" +
                        "WHERE email = "+ email +
                        "AND barcode = " + value + ";";
                try{
                    connection.createStatement().executeQuery(query);

                }
                catch(SQLException e){
                    System.out.println("Error with Deleting tool with barcode");
                }

            }
            case (3) -> {
                System.out.println("Please enter your email address");
                String email = scanner.nextLine();
                System.out.println("Please enter your the barcode number for the tool to edit");
                int oldBarcode = scanner.nextInt();
                System.out.println("Please enter what you want to change about the tool ");
                System.out.println("1.Barcode 2.Description 3.Price 4.Name");
                int field = scanner.nextInt();
                switch (field){
                    case (1) -> {
                        System.out.println("Please enter new barcode");
                        int barcode = scanner.nextInt();
                        String query = "UPDATE Owners" +
                                "SET Barcode = " + barcode +
                                "WHERE Barcode = " + oldBarcode +
                                "AND" +" email = "+ email + ";";
                        try{
                            connection.createStatement().executeQuery(query);

                        }
                        catch(SQLException e){
                            System.out.println("Error with Changing Barcode");
                        }
                    }
                    case (2) -> {
                        System.out.println("Please enter new Description");
                        String description = scanner.nextLine();
                        String query = "UPDATE Owners" +
                                "SET Description = " + description +
                                "WHERE Barcode = " + oldBarcode +
                                "AND" +" email = "+ email + ";";
                        try{
                            connection.createStatement().executeQuery(query);

                        }
                        catch(SQLException e){
                            System.out.println("Error with Changing Description");
                        }
                    }
                    case (3) -> {
                        System.out.println("Please enter new Price");
                        int price = scanner.nextInt();
                        String query = "UPDATE Owners" +
                                "SET Price = " + price +
                                "WHERE Barcode = " + oldBarcode +
                                "AND" +" email = "+ email + ";";
                        try{
                            connection.createStatement().executeQuery(query);

                        }
                        catch(SQLException e){
                            System.out.println("Error with Changing Price");
                        }
                    }
                    case (4) -> {
                        System.out.println("Please enter new Name");
                        String name = scanner.nextLine();
                        String query = "UPDATE Owners" +
                                "SET Name = " + name +
                                "WHERE Barcode = " + oldBarcode +
                                "AND" +" email = "+ email + ";";
                        try{
                            connection.createStatement().executeQuery(query);

                        }
                        catch(SQLException e){
                            System.out.println("Error with Changing Name");
                        }
                    }
                }



            }
            case (4) -> {
                System.out.println("System Catalog Down");
                System.out.println("Thank you! Have a wonderful day!");
                System.exit(0);
            }
            case (5) -> {
                System.out.println("Please enter the barcode of the tool you would like to add a category to: ");
                String barcode = scanner.nextLine();
                System.out.println("Please enter the category(ies) to add it to. " +
                        "If entering multiple, please separate with a ',': ");
                String userInput = scanner.nextLine();
                String[] str = userInput.split(",");
                try{
                    this.connection = new DatabaseConnection(
                            "jdbc:postgresql://reddwarf.cs.rit.edu:5432/p32001a",
                            "Hoh2saikaequeic5piut",
                            "p32001a",
                            "true" ).getConnection();
                    Statement statement = this.connection.createStatement();
                    for (String s : str) {
                        String addCategory = String.format("INSERT INTO \"Tool Categories\" VALUES ('%s', '%s')", barcode, s);
                        statement.executeUpdate(addCategory);
                    }
                    connection.close();

                }
                catch(SQLException e){
                    System.out.println("Error with Adding barcode");
                }
            }
        }

    }

    public void searchForTools() throws SQLException, ClassNotFoundException {
        System.out.println("search");
        System.out.println("-----------------------------");
        System.out.println("How would you like to search?");
        System.out.println("-----------------------------");
        System.out.println("1. By Barcode \t 2. By Name");
        System.out.println("3. By Category \t 4. Return to menu");

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

                ToolSearch search = new ToolSearch(input, "Barcode", order, connection);
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
                ToolSearch search = new ToolSearch(input, "Category", order, connection);
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
                ToolSearch search = new ToolSearch(input, "Name", order, connection);
            }
            case (4) -> runApplication();

        }
    }

    public void viewToolList() throws SQLException, ClassNotFoundException {
        java.util.Date today = new Date();



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
        System.out.println("3. Borrowed Tools \t 4. Quit");

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
                PreparedStatement state = connection.prepareStatement("SELECT \"Request\".\"Return By\",\"Request\".\"DateRequired\",\"Request\".\"Barcode\", \"Request\".\"Email\" FROM \"Request\" INNER JOIN \"Borrowed\" ON \"Request\".\"Barcode\" = \"Borrowed\".\"Barcode\" ORDER BY \"Request\".\"DateRequired\"");
                ResultSet result = state.executeQuery();
                while (result.next()){
                    System.out.println( "Date Acquired: "+ result.getDate("DateRequired" )+ " " + "Barcode: " + result.getString("Barcode" ) + " " + "Name: " + result.getString("Email"));
                    if (result.getDate("Return By").after(today) ){
                        System.out.println("WARNING ITEM OVERDUE");
                    }
                }

            }
            case (3) -> {
                System.out.println("--------------");
                System.out.println("Borrowed Tools");
                System.out.println("--------------");
                PreparedStatement state = connection.prepareStatement("SELECT \"Request\".\"Return By\",\"Request\".\"DateRequired\",\"Request\".\"Barcode\", \"Request\".\"Email\" FROM \"Request\" INNER JOIN \"Borrowed\" ON \"Request\".\"Barcode\" = \"Borrowed\".\"Barcode\" ORDER BY \"Request\".\"DateRequired\"");
                ResultSet result = state.executeQuery();
                while (result.next()) {
                    System.out.println("Date Borrowed: " + result.getDate("DateRequired") + " " + "Barcode: " + result.getString("Barcode") + " " + "Name: " + result.getString("Email"));
                }
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
        System.out.println("-------------------------------------");
        System.out.println("What would you like to do to request?");
        System.out.println("-------------------------------------");
        System.out.println("1. Make Request \t 5. View request for your tools");
        System.out.println("2. Cancel Request \t 6. View your request");
        System.out.println("3. Accept Request \t 7. Return a tool");
        System.out.println("4. Decline Request \t 8. Return to main menu");

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input = scanner.nextLine();
        System.out.println("What's your email?");
        String email = scanner.nextLine();

        Request request = new Request(connection);
        switch (Integer.parseInt(input)){
            case (1) -> {
                String s = "y";
                while( s.equals("y") ) {
                    System.out.println("What's the barcode of the tool you wish to borrow?");
                    input = scanner.nextLine();
                    System.out.println("What date do you required the tool by? (mm/dd/yyyy)");
                    String dateRequired = scanner.nextLine();
                    System.out.println("How long do you need the tool for?(numbers only)");
                    int duration = scanner.nextInt();
                    request.MakeRequest(email, input, duration, dateRequired);
                    System.out.println("Request completed");
                    System.out.println("Based on your request here some tools you might be interested in:");
                    request.alsoRec(input);
                    System.out.println("Do you wish to make another request?(y/n)");
                    s = scanner.nextLine().substring(0,1).toLowerCase();
                    }
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
                String returnBy = scanner.nextLine();
                request.AcceptRequest(input, barcode, returnBy);
            }
            case (4) -> {
                System.out.println("What's the username of user who requested the tool?");
                input = scanner.nextLine();
                System.out.println("What's the barcode of the tool the user requested?");
                String barcode = scanner.nextLine();
                request.DeclineRequest(input, barcode);
            }
            case (5) -> request.getRequestForYou(email);
            case (6) -> request.getRequestByYou(email);
            case (7) -> {
                System.out.println("Whats the barcode of the tool you wish to return?");
                input = scanner.nextLine();
                request.ReturnTool(email,input);
            }
            case (8) -> runApplication();

        }
    }

    /**
     * sets up so the user can view their dashboard
     * @throws SQLException: exception for handle sql errors
     * @throws ClassNotFoundException: error for class not found
     */
    public void viewDash() throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        System.out.println("What's your email?");
        String email = scanner.nextLine();
        String input = " ";
        while( !input.substring(0,1).equalsIgnoreCase("y") ) {
            Tools tools = new Tools(connection);
            tools.getNumberAvailableTool(email);
            tools.getNumberBorrowedTools(email);
            tools.getNumberLentTools(email);
            System.out.println("Do you want to go back to the main menu? (y/n)");
            input = scanner.nextLine();
        }
        runApplication();
    }

    /**
     * lest user view the stats of their frequently borrowed and lent tools
     * @throws SQLException: exception for handle sql errors
     * @throws ClassNotFoundException: error for class not found
     */
    public void viewSats() throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        System.out.println("What's your email?");
        String email = scanner.nextLine();
        System.out.println("View Statistics");
        System.out.println("-------------------------------------");
        System.out.println("What statistics would you like to see?");
        System.out.println("-------------------------------------");
        System.out.println("1. Top 10 frequently borrowed tools");
        System.out.println("2. Top 10 frequently lent tools");
        System.out.println("3. Back to main menu");
        String input = scanner.nextLine();

        Tools tools = new Tools(connection);
        switch (Integer.parseInt(input)){
            case (1) -> tools.getFrequentlyBorrowedTools(email);
            case (2) -> tools.getFrequentlyLentTools(email);
            case (3) -> runApplication();
        }
    }



}
