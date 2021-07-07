package userdomain;

import java.util.*;
import java.io.IOException;
import java.io.InputStreamReader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class InputForm {
    public static void main(String args[]) throws IOException  {

        //Set<String> user = new HashSet<String>();
        ArrayList<String> user = new ArrayList();

        String username;
        String password;
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        System.out.println("Please enter your first name ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter your last name ");
        String lastName = scanner.nextLine();
        System.out.println("Please enter your email ");
        String email = scanner.nextLine();
        boolean status = isValidEmailAddress(email);


        while (!status) {
            System.out.println("Please enter valid email ");
            String val = scanner.nextLine();
            do {
                status = isValidEmailAddress(val);
                if (status){
                    break;
                }
            }
                while (status);


            }
        System.out.println("Please enter your username ");
        username = scanner.nextLine();
        System.out.println("Please enter your password ");
        password = scanner.nextLine();



        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String creationDate = dateFormat.format(date);


        user.add(firstName);
        user.add(lastName);
        user.add(username);
        user.add(password);
        user.add(creationDate);

        System.out.print(user);
        scanner.close();
    }
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}
