package my.edu.utar;

import java.util.*;
import java.sql.*;

public class User{
	private final Scanner scanner =new Scanner(System.in);
	//private Connection connection;
	private AppManager app=new AppManager();
    private String username;
    private String phoneNum;
    private String password;
    private String email;
    private String fullName;
    
    
public void signUpPhase(Connection connection) {

        System.out.println("\n=============================");
		System.out.println("     Account Registration    ");
		System.out.println("=============================");
   
		    System.out.print("Enter your full name (e.g., MACC DING): ");
		    fullName = scanner.nextLine();
		    if (!isValidFullName(fullName)) {
		    	System.out.println("\n==============================================");
		        System.out.println("  Error Message: Invalid full name format     ");
		        System.out.println("==============================================");
		        System.out.println("Please try again ...");
		        signUpPhase(connection);
		    }

		    System.out.print("Enter your phone number (e.g., 0102223333): ");
		    phoneNum = scanner.nextLine();
		    if (!isValidPhoneNumber(phoneNum)) {
		    	System.out.println("\n=================================================");
		        System.out.println("      Error Message: Invalid phone number        ");
		        System.out.println("                                                 ");
		        System.out.println("  Contact number must consist 10 to 15 digits.   ");
		        System.out.println("=================================================");
		        System.out.println("Please try again ...");
		        signUpPhase(connection);
		    }

		    System.out.print("Enter your email address (e.g., maccding@outlook.com): ");
		    email = scanner.nextLine();
		    if (!isValidEmail(email)) {
		    	System.out.println("\n==================================================");
		        System.out.println("  Error Message: Invalid email addrerss format    ");
		        System.out.println("                                                  ");
		        System.out.println("  Email address must follow the standard format   ");
		        System.out.println("==================================================");
		        System.out.println("Please try again ...");
		        signUpPhase(connection);
		    }

		    System.out.print("Enter your username (e.g., macc2020): ");
		    username = scanner.nextLine();
		    if (!isValidUsername(username)) {
		    	System.out.println("\n=================================================================================");
		        System.out.println( "                       Error Message:  Invalid username format                  "); 
		        System.out.println(" Username must consist of alphanumeric characters and have at least 5 characters.");
		        System.out.println("=================================================================================");

		        System.out.println("Please try again ...");
		        signUpPhase(connection);
		    }

		    System.out.print("Enter your password: ");
		    password = scanner.nextLine();

		    String query = "INSERT INTO user_account ( username, fullName, password, contactNum, email, memberLevel) VALUES (?, ?, ?, ?, ?, ?)";
		    try (PreparedStatement statement = connection.prepareStatement(query)) {

		        statement.setString(1, username);
		        statement.setString(2, fullName);
		        statement.setString(3, password);
		        statement.setString(4, phoneNum);
		        statement.setString(5, email);
		        statement.setString(6, "member");

		        int rowsInserted = statement.executeUpdate();

		        if (rowsInserted > 0) {
		            System.out.println("\n=================================================");
		            System.out.println("            Confirmation Message:                ");
		            System.out.println("                                                 ");
		            System.out.println("        Account Successfully Created             ");
		            System.out.println("   You can now log in using your credentials     ");
		            System.out.println("=================================================");
		            loginPhase(connection);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        System.out.println("\n=============================================");
		        System.out.println("    Error Message: Account Creation Failed   ");
		        System.out.println("=============================================");
		        System.out.println("Please try again ...");
		        app.displayMenu();
		    }
}
     // Login
        public void loginPhase(Connection connection) {
            System.out.println("\n=============================");
            System.out.println("           Login             ");
            System.out.println("=============================");
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            System.out.print("Enter password: ");
            password = scanner.nextLine();

            String query = "SELECT * FROM user_account WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("\nLogin successful.............\n");
                    app.displayMenu();
                } else {
                    System.out.println("\n===================================");
                    System.out.println("   Invalid username or password    ");
                    System.out.println("   Please try again with valid     ");
                    System.out.println("           credentials             ");
                    System.out.println("===================================");
                    System.out.println("Please try again.............");
                    app.welcomePage();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
public boolean isValidFullName(String fullName) {
    return fullName.matches("[A-Za-z]+(\\s[A-Za-z]+){1,}");
}

public boolean isValidPhoneNumber(String phoneNum) {
    return phoneNum.matches("\\d{10,15}");
}

public boolean isValidEmail(String email) {
    return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
}

public boolean isValidUsername(String username) {
    return username.matches("[a-zA-Z0-9]{5,}");
}
}
