package my.edu.utar;



import java.util.*;
import java.sql.*;

public class AppManager {
    
    private Scanner scanner = new Scanner(System.in);
    private static Connection connection;

    public static void main(String[] args) {
        AppManager app = new AppManager();
        app.welcomePage();
    }
    
    public AppManager() {
    	//initializeConnection();
    }
    

    public void welcomePage() {
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
            	//User user=new User();
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
               // scanner.nextLine();
                switch (choice) {
                    case 1:
                        //user.loginPhase(connection);
                        break;
                    case 2:
                       // user.signUpPhase(connection);
                        break;
                    case 3:
                     //   displayMenu();
                        break;
                    case 4:
                    	goodbyeMessage();
                    	return;
				default:
                        System.out.println("\n==========================================");
                        System.out.println("     Error Message: Invalid Selection     ");
                        System.out.println("==========================================");
                        System.out.println("Please try again ...");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n==========================================");
                System.out.println("    Error Message: Invalid Input Format   ");
                System.out.println("==========================================");
                System.out.println("Please enter a valid integer choice (1-4) ...");
                scanner.nextLine();
            }
        }

    public void goodbyeMessage() {
        System.out.println("\n===============================");
        System.out.println("                               ");
        System.out.println("   Goodbye! Have a nice day!   ");
        System.out.println("                               ");
        System.out.println("===============================");
    }

    
    // Menu
    public void displayMenu() {
            System.out.println("\n@=============================@");
            System.out.println("|         Main Menu           |");
            System.out.println("|=============================|");
            System.out.println("|  1. Check Avaiable Room     |");
            System.out.println("|  2. Manage Booking          |");
            System.out.println("|  3. Trace Waiting Status    |");
            System.out.println("|  4. Quit                    |");
            System.out.println("@=============================@");

        try {
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            //scanner.nextLine();
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

    public void processChoice(int choice) {
            switch (choice) {
                case 1:
                   //Room room=new Room(connection);
                   // room.checkRoom();
                    break;
                case 2:
                   // Booking booking=new Booking();
                   // booking.bookingMenu();
                    break;
                case 3:
                   // WaitingList wait_list=new WaitingList();
                    //wait_list.getWaiting();
                    break;
                case 4:
                   // welcomePage();
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
    
//    // Establish connection
//    public void initializeConnection() {
//        try {
//            connection = dbConnector.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}