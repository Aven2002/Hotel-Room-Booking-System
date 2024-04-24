package my.edu.utar;
import java.util.*;
import java.sql.*;
public class Booking {

	private final Scanner scanner=new Scanner(System.in);
	private Connection connection;
	private int bookingID;
	private User user=new User();
	private int userID;
	private Room room=new Room();
	private String room_type;
	private String memberLevel;
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
		System.out.println("  2. Create Booking                     ");
		System.out.println("  3. Cancel Booking                     ");
		System.out.println("  4. Back                               ");
		System.out.println("========================================");
		try {
		System.out.print("Enter your choice: ");
		int choice=scanner.nextInt();
		scanner.nextLine();
		
		switch(choice) 
		{
			case 1: reviewBookingInfo();
				break;
			case 2: 
				setBooking();
					break;
			case 3: cancelBooking();
					break;
			case 4: 
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
		String name=user.getUsername(userID);
		String memberLevel=user.getMemberLevel(userID);
		room_type=room.getRoomType(memberLevel);
		printer.printInfo(name,memberLevel,room_type);
		bookingMenu();
	}
	
	public void setBooking() {
	    int numRoom = 0;
	    memberLevel = user.getMemberLevel(userID);
	    room_type = room.getRoomType(memberLevel);
	    boolean available = room.checkRoom(room_type);

	    if (available) {
	        room.displayAvailableRooms(room_type);
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
	            
	            handleRoomSelection(numRoom);
	        } catch (InputMismatchException e) {
	            System.out.println("\n============================================");
	            System.out.println("    Error Message: Invalid Input Format   ");
	            System.out.println("==========================================");
	            System.out.println("Please try again ...\n");
	            bookingMenu();
	        }
	    } else {
	        addToWaitingList("In queue");
	        user.displayMenu();
	    }
	}

	private void handleRoomSelection(int numRoom) {
	    try {
	        int availableRooms = room.fetchRoomCount(room_type);
	        if (numRoom > availableRooms) {
	            System.out.println("\n======================================================");
	            System.out.println("  Dear customer,");
	            System.out.println("  Currently, there are not enough " + room_type + " rooms available for you.");
	            System.out.println("======================================================");

	            if (memberLevel.equals("VIP")) {
	                // Handle VIP room selection
	            } else if (memberLevel.equals("Member")) {
	                // Handle member room selection
	            } else {
	                // Handle non-member room selection
	            }
	        }
	        
	        ArrayList<Integer> roomIDs = new ArrayList<>();
	        for (int i = 0; i < numRoom; i++) {
	            System.out.println("Enter the corresponding Room ID: ");
	            int roomID = scanner.nextInt();
	            scanner.nextLine();
	            roomIDs.add(roomID);
	        }
	        
	        createBooking(roomIDs, userID, numRoom);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	

	public void createBooking(ArrayList<Integer> roomIDs, int userID, int numRoom) {
	    try {
	        user.initializeConnection();
	        
	        // Insert into booking table
	        String bookingQuery = "INSERT INTO booking (userID, roomQuantity, status) VALUES (?, ?, ?)";
	        PreparedStatement bookingStatement = connection.prepareStatement(bookingQuery, Statement.RETURN_GENERATED_KEYS);
	        bookingStatement.setInt(1, userID);
	        bookingStatement.setInt(2, numRoom);
	        bookingStatement.setString(3, "Checked In");
	        int rowsInserted = bookingStatement.executeUpdate();
	        
	        if (rowsInserted > 0) {
	            // Get the generated booking ID
	            ResultSet generatedKeys = bookingStatement.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int bookingID = generatedKeys.getInt(1);

	                String bookingRoomQuery = "INSERT INTO bookingRoom (bookingID, roomID) VALUES (?, ?)";
	                PreparedStatement bookingRoomStatement = connection.prepareStatement(bookingRoomQuery);
	                
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
	                    System.out.println("\n===========================================================");
	                    System.out.println("    Confirmation Message: Booking created successfully");
	                    System.out.println("===========================================================");
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
	   bookingMenu();
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
		 String checkWaitingQuery = "SELECT * FROM waiting_list WHERE userID = ?";
		    try (PreparedStatement checkStatement = connection.prepareStatement(checkWaitingQuery)) {
		        checkStatement.setInt(1, userID);
		        ResultSet checkResultSet = checkStatement.executeQuery();
		        
		        // If the booking ID is in the waiting list, remove it
		        if (checkResultSet.next()) {
		            String removeWaitingQuery = "DELETE FROM waiting_list WHERE userID = ?";
		            try (PreparedStatement removeStatement = connection.prepareStatement(removeWaitingQuery)) {
		                removeStatement.setInt(1, userID);
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
		    
		 // Update room status to available based on booking ID
	        String getRoomIDsQuery = "SELECT roomID FROM bookingRoom WHERE bookingID = ?";
	        try (PreparedStatement getRoomIDsStatement = connection.prepareStatement(getRoomIDsQuery)) {
	            getRoomIDsStatement.setInt(1, bookingID);
	            ResultSet roomIDsResultSet = getRoomIDsStatement.executeQuery();

	            // Iterate over room IDs and update their status
	            while (roomIDsResultSet.next()) {
	                int roomID = roomIDsResultSet.getInt("roomID");
	                String updateRoomStatusQuery = "UPDATE room SET roomStatus = 'Available' WHERE roomID = ?";
	                try (PreparedStatement updateStatement = connection.prepareStatement(updateRoomStatusQuery)) {
	                    updateStatement.setInt(1, roomID);
	                    updateStatement.executeUpdate();
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