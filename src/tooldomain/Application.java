package tooldomain;

public class Application {

    public Application(){
        displayMenu();
    }

    public void displayMenu(){
        System.out.println("---------------------------------------");
        System.out.println("What would you like to do this session?");
        System.out.println("---------------------------------------");
        System.out.println("1. Manage your tools \t 3. View the tool list");
        System.out.println("2. Search for a tool \t 4. Manage Requests");

    }
}
