package tooldomain;

import javax.tools.Tool;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Application {

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
    public void manageTools(){
        System.out.println("manage");
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

        switch (Integer.parseInt(input)){
            case (1) -> {
                System.out.println("Please enter the barcode of the tool you would like to find: ");
                input = scanner.nextLine();
                ToolSearch search = new ToolSearch(input, "Barcode");
            }
            case (2) -> {
                System.out.println("Please enter the category you would like to search within: ");
                input = scanner.nextLine();
                ToolSearch search = new ToolSearch(input, "Category");
            }
            case (3) -> {
                System.out.println("Please enter the name of the tool you would like to find: ");
                input = scanner.nextLine();
                ToolSearch search = new ToolSearch(input, "Name");
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
