package my.edu.utar;
import java.util.*;
import java.sql.*;
public class Booking {

	private final Scanner scanner=new Scanner(System.in);
	private Connection connection;
	private int bookingID;
	private int userID;
	private User user=new User();
	private Room room=new Room();
	private Printer printer=new Printer();
	private WaitingList waitList=new WaitingList();
	private ArrayList<Integer>roomIDs=new ArrayList<>();
	
	public Booking(int userID){
		this.userID=userID;
		this.connection = user.initializeConnection();
	}
	
	public void bookingMenu() {
		System.out.println("\n========================================");
		System.out.println("          Booking Menu Options          ");
		System.out.println("========================================");
		System.out.println("  1. Review Booking Information         ");
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
			case 1: reviewBookingInfo();
				break;
			case 2: searchBooking(bookingID);
					break;
			case 3: setBooking(userID);
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
	
	public void reviewBookingInfo() {
		System.out.println(userID);
		String name=user.getUsername(userID);
		String member_type=user.getMemberLevel(userID);
		String room_type=room.getRoomType(member_type);
		printer.printInfo(name,member_type,room_type);
	}
	
	public void setBooking(int userID) {
		int numRoom=0;
	    String memberLevel=user.getMemberLevel(userID);
	    String roomType = room.getRoomType(memberLevel);
	    boolean available=room.checkRoom(roomType);
	    if (available) {
	        room.displayAvailableRooms(roomType);
	        int maxRooms;
	        if (memberLevel.equals("VIP")) {
	            System.out.println("Dear customer, you are able to book up to 3 rooms at a time.");
	            maxRooms = 3;
	        } else if (memberLevel.equals("Member")) {
	            System.out.println("Dear customer, you are able to book up to 2 rooms at a time.");
	            maxRooms = 2;
	        } else {
	            maxRooms = 1; 
	        }
	        try {
	        System.out.println("How many rooms do you want to book: ");
	         numRoom = scanner.nextInt();
	        scanner.nextLine();
	        while (numRoom > maxRooms) {
	            System.out.println("You can only book up to " + maxRooms + " rooms at a time.");
	            System.out.println("Please enter a valid number of rooms: ");
	            numRoom = scanner.nextInt();
	            scanner.nextLine();
	        }
	        for (int i = 0; i < numRoom; i++) {
	            System.out.println("Enter the corresponding Room ID: ");
	            int roomID = scanner.nextInt();
	            scanner.nextLine();
	            roomIDs.add(roomID);
	        }
	        createBooking(roomIDs, userID, numRoom);
	        }catch(InputMismatchException e) {
				System.out.println("\n============================================");
	            System.out.println("    Error Message: Invalid Input Format   ");
	            System.out.println("==========================================");
	            System.out.println("Please try again ...\n");
	            bookingMenu();
			}
	    } else {
	    	waitList.addWaiting(userID);
	        System.out.println("\n===================================================================");
	        System.out.println("  Confirmation Message: You have been added to the waiting list");
	        System.out.println("===================================================================");
	    }
	}
	
	public void createBooking(ArrayList<Integer> roomIDs, int userID, int numRoom) {
	    try {
	        user.initializeConnection();
	        
	        // Insert into booking table
	        String bookingQuery = "INSERT INTO booking (userID, roomQuantity) VALUES (?, ?)";
	        PreparedStatement bookingStatement = connection.prepareStatement(bookingQuery, Statement.RETURN_GENERATED_KEYS);
	        bookingStatement.setInt(1, userID);
	        bookingStatement.setInt(2, numRoom);
	        int rowsInserted = bookingStatement.executeUpdate();
	        
	        if (rowsInserted > 0) {
	            // Get the generated booking ID
	            ResultSet generatedKeys = bookingStatement.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int bookingID = generatedKeys.getInt(1);
	                
	                // Insert into bookingRoom table
	                String bookingRoomQuery = "INSERT INTO bookingRoom (bookingID, roomID) VALUES (?, ?)";
	                PreparedStatement bookingRoomStatement = connection.prepareStatement(bookingRoomQuery);
	                
	                // Insert each room into bookingRoom table
	             // Insert each room into bookingRoom table
	                for (int roomID : roomIDs) {
	                    bookingRoomStatement.setInt(1, bookingID);
	                    bookingRoomStatement.setInt(2, roomID);
	                    bookingRoomStatement.addBatch(); 
	                }

	                // Execute batch insert
	                int[] batchResults = bookingRoomStatement.executeBatch();
	                int successfulInserts = Arrays.stream(batchResults).sum();
	                
	                if (successfulInserts == roomIDs.size()) {
	                    System.out.println("\n======================================================");
	                    System.out.println("    Confirmation Message: Booking created successfully");
	                    System.out.println("======================================================");
	                    room.updateRoomStatus(roomIDs);
	                } else {
	                    System.out.println("\n======================================================");
	                    System.out.println("  Error Message: Failed to create booking");
	                    System.out.println("======================================================");
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public void cancelBooking() {
		user.initializeConnection();
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
		//Remove from booking table
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
		//Remove from waiting_list table
		 String checkWaitingQuery = "SELECT * FROM waiting_list WHERE bookingID = ?";
		    try (PreparedStatement checkStatement = connection.prepareStatement(checkWaitingQuery)) {
		        checkStatement.setInt(1, bookingID);
		        ResultSet checkResultSet = checkStatement.executeQuery();
		        
		        // If the booking ID is in the waiting list, remove it
		        if (checkResultSet.next()) {
		            String removeWaitingQuery = "DELETE FROM waiting_list WHERE bookingID = ?";
		            try (PreparedStatement removeStatement = connection.prepareStatement(removeWaitingQuery)) {
		                removeStatement.setInt(1, bookingID);
		                int rowsAffected = removeStatement.executeUpdate();
		                if (rowsAffected > 0) {
		                    System.out.println("  Confirmation Message: You have been removed from the waiting list");
		                    System.out.println("===================================================================");
		                }
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
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
	                Timestamp created = resultSet.getTimestamp("created_at");

	                System.out.println("\n===========================================");
	                System.out.println("    Booking ID    : " + bookedID);
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
	
}
