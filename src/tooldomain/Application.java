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

    public void manageRequests(){
        System.out.println("Manage Request");
        System.out.println("1. Make Request \t ");
        System.out.println("1. Make Request");
    }

}
