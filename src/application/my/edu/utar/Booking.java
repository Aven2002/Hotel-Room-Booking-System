package my.edu.utar;
import java.util.*;
import java.sql.*;
public class Booking {

	private final Scanner scanner=new Scanner(System.in);
	private Connection connection;
	private int bookingID;
	private User user=new User();
	
	public Booking(int userID,int roomID) {
		user.initializeConnection();
//		this.userID=userID;
//		this.roomID=roomID;
	}
	public Booking(){
		this.connection = user.initializeConnection();
	}
	
	public void bookingMenu() {
		System.out.println("\n========================================");
		System.out.println("          Booking Menu Options          ");
		System.out.println("========================================");
		System.out.println("  1. View Booking                       ");
		System.out.println("  2. Search Booking                     ");
		System.out.println("  3. Create Booking                     ");
		System.out.println("  4. Cancel Booking                     ");
		System.out.println("  5. Back                               ");
		System.out.println("========================================");
		try {
		System.out.print("Enter your choice: ");
		int choice=scanner.nextInt();
		scanner.nextLine();
		
		switch(choice) 
		{
//			case 1: createBooking(userId,roomID);
//					break;
			case 2: searchBooking(1);
					break;
			case 3: cancelBooking();
					break;
			case 4: cancelBooking();
					break;
			case 5: 
				user.displayMenu();
				break;
		default: 
			System.out.println("\n===========================================");
		    System.out.println("   Error Message: Booking ID not found   ");
		    System.out.println("===========================================");
		    System.out.println("Please try again ...\n");
		    bookingMenu();
		break;
		
		}
		}
		catch(InputMismatchException e) {
			System.out.println("\n============================================");
            System.out.println("    Error Message: Invalid Input Format   ");
            System.out.println("==========================================");
            System.out.println("Please try again ...\n");
            bookingMenu();
		}
	}
	
	public void cancelBooking() {
		try {
			connection = dbConnector.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.print("Enter the correspond booking ID to process booking cancelation :");
		try {
		bookingID=scanner.nextInt();
		scanner.nextLine();
		}catch(InputMismatchException e) {
			scanner.nextLine();
			  System.out.println("\n============================================");
	            System.out.println("    Error Message: Invalid Input Format   ");
	            System.out.println("==========================================");
	            System.out.println("Please try again ...\n");
	            cancelBooking();
		}
		
		if (searchBooking(bookingID)) {
		        System.out.println("Do you want to continue the booking cancellation process? (Yes || No)");
		        String choice = scanner.nextLine();
		        if(choice.equalsIgnoreCase("Yes")) {
		                String deleteQuery = "DELETE FROM booking WHERE bookingID = ?";
		                try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
		                    statement.setInt(1, bookingID);
		                    int rowsAffected = statement.executeUpdate();
		                    if (rowsAffected > 0) {
		                        System.out.println("\n===================================================================");
		                        System.out.println("  Confirmation Message: Booking with ID " + bookingID + " successfully cancelled.");
		                        System.out.println("===================================================================");
		                    }
		                } catch (SQLException e) {
		                    e.printStackTrace();
		                }
		        	}else {
                   	 System.out.println("\n===============================================================");
                     System.out.println("  Error Message: Booking with ID " + bookingID + " is not being cancelled.");
                     System.out.println("===============================================================");
             }
			} 
		bookingMenu();
	}
	
	public boolean searchBooking(int bookingID) {
	    String searchQuery = "SELECT * FROM booking WHERE bookingID = ?";
	    try (PreparedStatement statement = connection.prepareStatement(searchQuery)) {
	        statement.setInt(1, bookingID);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	                int bookedID = resultSet.getInt("bookingID");
	                int roomID = resultSet.getInt("roomID");
	                Timestamp created = resultSet.getTimestamp("created_at");

	                System.out.println("\n===========================================");
	                System.out.println("    Booking ID    : " + bookedID);
	                System.out.println("    Room ID       : " + roomID);
	                System.out.println("    Creation Date : " + created);
	                System.out.println("===========================================");
	                return true;
	            } else {
	            	System.out.println("\n===========================================");
	    		    System.out.println("   Error Message: Booking ID not found   ");
	    		    System.out.println("===========================================");   
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public static void main(String[]args) {
		Booking book=new Booking();
		book.bookingMenu();
	}
}
