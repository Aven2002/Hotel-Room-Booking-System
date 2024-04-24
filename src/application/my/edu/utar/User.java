package my.edu.utar;

import java.util.*;
import java.sql.*;

public class User{
	private final Scanner scanner =new Scanner(System.in);
	private Connection connection;
	private int userID;
    private String username;
    private String phoneNum;
    private String password;
    private String email;
    private String fullName;
    
    public static void main(String[]args) throws SQLException {
    	User user=new User();
    	//user.welcomePage();
    	user.markExclusiveRewardAsRedeemed(3);
    }
    public User() {
    	initializeConnection();
    }
    public void welcomePage() throws SQLException {
        System.out.println("\n@===================================@");
        System.out.println("| Welcome to Hotel Booking System   |");
        System.out.println("|===================================|");
        System.out.println("|       1. Log in                   |");
        System.out.println("|       2. Sign up                  |");
        System.out.println("|       3. Continue as guest        |");
        System.out.println("|       4. Exit                     |");
        System.out.println("@===================================@");
        int choice;
        try {
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    loginPhase();
                    break;
                case 2:
                   signUpPhase();
                    break;
                case 3:
                    displayMenu();
                    break;
                case 4:
                	goodbyeMessage();
                	return;
			default:
                    System.out.println("\n==========================================");
                    System.out.println("     Error Message: Invalid Selection     ");
                    System.out.println("==========================================");
                    System.out.println("Please try again ...");
                    welcomePage();
            }
        } catch (InputMismatchException e) {
            System.out.println("\n==========================================");
            System.out.println("    Error Message: Invalid Input Format   ");
            System.out.println("==========================================");
            System.out.println("Please enter a valid integer choice (1-4) ...");
            scanner.nextLine();
            welcomePage();
        }
    }

public void goodbyeMessage() {
    System.out.println("\n===============================");
    System.out.println("                               ");
    System.out.println("   Goodbye! Have a nice day!   ");
    System.out.println("                               ");
    System.out.println("===============================");
}
public void signUpPhase() throws SQLException {

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
		        signUpPhase();
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
		        signUpPhase();
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
		        signUpPhase();
		    }

		    System.out.print("Enter your username (e.g., macc2020): ");
		    username = scanner.nextLine();
		    if (!isValidUsername(username)) {
		    	System.out.println("\n=================================================================================");
		        System.out.println( "                       Error Message:  Invalid username format                  "); 
		        System.out.println(" Username must consist of alphanumeric characters and have at least 5 characters.");
		        System.out.println("=================================================================================");

		        System.out.println("Please try again ...");
		        signUpPhase();
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
		        statement.setString(6, "Member");

		        int rowsInserted = statement.executeUpdate();

		        if (rowsInserted > 0) {
		            System.out.println("\n=================================================");
		            System.out.println("            Confirmation Message:                ");
		            System.out.println("                                                 ");
		            System.out.println("        Account Successfully Created             ");
		            System.out.println("   You can now log in using your credentials     ");
		            System.out.println("=================================================");
		            loginPhase();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        System.out.println("\n=============================================");
		        System.out.println("    Error Message: Account Creation Failed   ");
		        System.out.println("=============================================");
		        System.out.println("Please try again ...");
		        displayMenu();
		    }
}
     // Login
        public void loginPhase() {
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
                	int userID = resultSet.getInt("userID");
                    System.out.println("\nLogin successful.............\n");
                    setUserID(userID);
                    displayMenu();
                } else {
                    System.out.println("\n===================================");
                    System.out.println("   Invalid username or password    ");
                    System.out.println("   Please try again with valid     ");
                    System.out.println("           credentials             ");
                    System.out.println("===================================");
                    System.out.println("Please try again.............");
                    welcomePage();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Menu
        public void displayMenu() throws SQLException {
                System.out.println("\n@=============================@");
                System.out.println("|         Main Menu           |");
                System.out.println("|=============================|");
                System.out.println("|  1. Check Avaiable Room     |");
                System.out.println("|  2. Manage Booking          |");
                System.out.println("|  3. Trace Waiting Status    |");
                System.out.println("|  4. Back                    |");
                System.out.println("@=============================@");

            try {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                processChoice(choice);
            } catch (InputMismatchException e) {
                System.out.println("");
                System.out.println("\n=============================================");
                System.out.println("    Error Message: Invalid Input Format   ");
                System.out.println("==============================================");
                System.out.println("Please try again ...\n");
                scanner.nextLine();
                displayMenu();
            }
        }

        public void processChoice(int choice) throws SQLException {
                switch (choice) {
                    case 1:
                       Room room=new Room();
                       String memberLevel=getMemberLevel(userID);
                       String room_type=room.getRoomTypeDependMember(memberLevel);
                       room.displayAvailableRooms(room_type);
                       displayMenu();
                        break;
                    case 2:
                       Booking booking=new Booking(userID);
                        booking.bookingMenu();
                        break;
                    case 3:
                        WaitingList wait_list=new WaitingList();
                        wait_list.getWaiting(getUserID());
                        break;
                    case 4:
                        welcomePage();
                        break;
                    default:
                        System.out.println("");
                        System.out.println("\n=========================================");
                        System.out.println("    Error Message: Invalid Selection     ");
                        System.out.println("=========================================");
                        System.out.println("Please try again ...\n");
                        scanner.nextLine();
                        displayMenu();
                }
            }
        
         //Establish connection
        public Connection initializeConnection() {
            try {
                connection = dbConnector.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
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

public int getUserID() {
	return userID;
}
public String getUsername(int userID) {
    String username = null;
    String query = "SELECT username FROM user_account WHERE userID = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, userID);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            username = resultSet.getString("username");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return username;
}

public void setUserID(int userID) {
	this.userID=userID;
}

public String getMemberLevel(int userID) {
	String memberLevel="non- member";
	String query = "SELECT memberLevel FROM user_account WHERE userID=?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, userID);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
        	 memberLevel = resultSet.getString("memberLevel");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return memberLevel;
}

public void markExclusiveRewardAsRedeemed(int userID) {

    try {
        // Write SQL update query
        String updateQuery = "UPDATE user_account SET exclusiveReward = false WHERE userID = ?";
        
        // Create a prepared statement
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        
        // Set parameters
        preparedStatement.setInt(1, userID);
        
        // Execute the update query
        int rowsUpdated = preparedStatement.executeUpdate();
        
        if (rowsUpdated < 0) {
        	 System.out.println("No user found with ID " + userID);
        } 
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public boolean memberHasExclusiveReward(int userID) {
    

    try {
        String query = "SELECT exclusiveReward FROM user_account WHERE userID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, userID);

        ResultSet resultSet = preparedStatement.executeQuery();
        // Check if the result set has any rows
        if (resultSet.next()) {
        	boolean hasExclusiveReward = resultSet.getBoolean("exclusiveReward");
            return hasExclusiveReward;
        } else {
            // No user found with the specified ID
            System.out.println("No user found with ID " + userID);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}



}
