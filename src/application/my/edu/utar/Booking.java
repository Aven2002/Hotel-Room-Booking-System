package my.edu.utar;
import java.sql.*;
import java.util.*;

public class Booking {

	private final Scanner scanner=new Scanner(System.in);
	private Connection connection;
	private User user=new User();
	private Room room=new Room();
	private WaitingList waitList=new WaitingList();
	private int userID;
	private int bookingID;
	private String memberLevel;
	private String roomType;
	private int numRoomToBook;
	private ArrayList<Integer> roomIDs = new ArrayList<>();
	private ArrayList<Integer> allocatedRoomIDs = new ArrayList<>();
	
	public Booking(int userID) {
		connection=user.initializeConnection();
		this.userID=userID;
	}
	
	public void bookingMenu() throws SQLException {
		System.out.println("\n========================================");
		System.out.println("          Booking Menu Options          ");
		System.out.println("========================================");
		System.out.println("  1. Create Booking                     ");
		System.out.println("  2. Cancel Booking                     ");
		System.out.println("  3. Back                               ");
		System.out.println("========================================");
		try {
		System.out.print("Enter your choice: ");
		int choice=scanner.nextInt();
		scanner.nextLine();
			switch(choice) 
			{
				case 1: 
						setBooking();
						break;
				case 2: 
						displayUserBookings(userID);
						cancelBooking();
						break;
				case 3: 
						user.displayMenu();
						break;
			default: 
				System.out.println("\n===========================================");
			    System.out.println("   Error Message: Invalid Selection  ");
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
	
	public void displayUserBookings(int userID) {
	    String query = "SELECT b.bookingID, b.roomQuantity, b.created_at, GROUP_CONCAT(br.roomID) AS roomIDs " +
	                   "FROM booking b " +
	                   "JOIN bookingRoom br ON b.bookingID = br.bookingID " +
	                   "WHERE b.userID = ? " +
	                   "GROUP BY b.bookingID";
	    
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, userID);
	        ResultSet resultSet = statement.executeQuery();
	        
	        List<Integer> bookingIDs = new ArrayList<>();
	        while (resultSet.next()) {
	            int bookingID = resultSet.getInt("bookingID");
	            bookingIDs.add(bookingID);
	            
	            int roomQuantity = resultSet.getInt("roomQuantity");
	            String roomIDs = resultSet.getString("roomIDs");
	            Timestamp createdAt = resultSet.getTimestamp("created_at");
	            
	            System.out.println("\n===========================================");
	            System.out.println("  Booking ID    : " + bookingID);
	            System.out.println("  Room ID(s)    : " + roomIDs);
	            System.out.println("  Room Quantity : " + roomQuantity);
	            System.out.println("  Created At    : " + createdAt);
	            System.out.println("===========================================");
	        }
	        
	        if (bookingIDs.isEmpty()) {
	        	System.out.println("\n===============================================================");
	            System.out.println("  Dear customer, currently you have not made any booking yet");
	            System.out.println("===============================================================");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	


	public void createBooking(ArrayList<Integer> allocatedRoomIDs, int userID) throws SQLException{
	    try {
	        user.initializeConnection();
	        
	        int numRoom = allocatedRoomIDs.size(); // Get the number of rooms
	        
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
	                for (int roomID : allocatedRoomIDs) {
	                    bookingRoomStatement.setInt(1, bookingID);
	                    bookingRoomStatement.setInt(2, roomID);
	                    bookingRoomStatement.addBatch(); 
	                }

	                // Execute batch insert
	                int[] batchResults = bookingRoomStatement.executeBatch();
	                int successfulInserts = Arrays.stream(batchResults).sum();
	                
	                if (successfulInserts == numRoom) {
	                    System.out.println("\n===========================================================");
	                    System.out.println("Confirmation Message: Booking created successfully");
	                    System.out.println("===========================================================");
	                    for (int roomID : allocatedRoomIDs) {
	                        String roomType = room.getRoomTypeDependID(roomID);
	                        System.out.println("Room ID: " + roomID + "\tRoom Type: " + roomType);
	                    }
	                    room.updateRoomStatus_booked(allocatedRoomIDs);
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



	

	public void setBooking() throws SQLException {
		memberLevel=user.getMemberLevel(userID);
		numRoomToBook=getNumOfRoomToBook(memberLevel);
	    memberLevel = user.getMemberLevel(userID);
	    roomType = room.getRoomTypeDependMember(memberLevel);

	  
	    int availableVipRoom = room.fetchRoomCount("VIP");
	    int availableDeluxeRoom = room.fetchRoomCount("Deluxe");
	    int availableStandardRoom = room.fetchRoomCount("Standard");
	    int totalAvailableRooms=availableVipRoom+availableDeluxeRoom+availableStandardRoom;
	    int remainingRooms = numRoomToBook;
	    if(numRoomToBook<=totalAvailableRooms) {
	    switch (memberLevel) {
	    case "VIP":
	        if (numRoomToBook <= availableVipRoom) {
	            // Allocate all requested rooms from VIP rooms if available
	        	List<Integer> vipRoomIDs = room.fetchAvailableRoomIDs("VIP", numRoomToBook);
	        	int roomsToAdd = Math.min(numRoomToBook, vipRoomIDs.size());
	        	allocatedRoomIDs.addAll(vipRoomIDs.subList(0, roomsToAdd));
	        } else {
	            // Allocate as many VIP rooms as available
	            if (availableVipRoom > 0) {
	                allocatedRoomIDs.addAll(room.fetchAvailableRoomIDs("VIP", availableVipRoom));
	                 remainingRooms = numRoomToBook - allocatedRoomIDs.size();
	                if (remainingRooms > 0) {
	                    // Allocate Deluxe rooms if necessary
	                	List<Integer> deluxeRoomIDs = room.fetchAvailableRoomIDs("Deluxe", remainingRooms);
	    	        	int roomsToAdd = Math.min(remainingRooms, deluxeRoomIDs.size());
	    	        	allocatedRoomIDs.addAll(deluxeRoomIDs.subList(0, roomsToAdd));
	                    remainingRooms = numRoomToBook - allocatedRoomIDs.size();
	                    if (remainingRooms > 0) {
	                        // Allocate Standard rooms if necessary
	                    	List<Integer> standardRoomIDs  = room.fetchAvailableRoomIDs("Standard", remainingRooms);
	                    	 roomsToAdd = Math.min(remainingRooms, standardRoomIDs.size());
		    	        	allocatedRoomIDs.addAll(standardRoomIDs.subList(0, roomsToAdd));
	                    }
	                }
	            } else {
	                // If no VIP rooms are available, try to allocate from Deluxe or Standard rooms
	            	List<Integer> deluxeRoomIDs = room.fetchAvailableRoomIDs("Deluxe", remainingRooms);
    	        	int roomsToAdd = Math.min(remainingRooms, deluxeRoomIDs.size());
    	        	allocatedRoomIDs.addAll(deluxeRoomIDs.subList(0, roomsToAdd));
                    remainingRooms = numRoomToBook - allocatedRoomIDs.size();
	                if (remainingRooms > 0) {
	                    // Allocate Standard rooms if necessary
	                	List<Integer> standardRoomIDs  = room.fetchAvailableRoomIDs("Standard", remainingRooms);
                   	 roomsToAdd = Math.min(remainingRooms, standardRoomIDs.size());
	    	        	allocatedRoomIDs.addAll(standardRoomIDs.subList(0, roomsToAdd));
	                }
	            }
	        }
	        // After allocating rooms, create the booking
	        createBooking(allocatedRoomIDs, userID);
	        break;
	    case "Member":
	        remainingRooms = numRoomToBook;
	        if (numRoomToBook <= availableDeluxeRoom) {
	            // Allocate all requested rooms from Deluxe rooms if available
	            List<Integer> deluxeRoomIDs = room.fetchAvailableRoomIDs("Deluxe", numRoomToBook);
	            int roomsToAdd = Math.min(numRoomToBook, deluxeRoomIDs.size());
	            allocatedRoomIDs.addAll(deluxeRoomIDs.subList(0, roomsToAdd));
	            remainingRooms -= roomsToAdd;
	        } else {
	            // Check if VIP rooms are available and the member has an exclusive reward
	            if (remainingRooms > 0 && user.memberHasExclusiveReward(userID)) {
	                List<Integer> vipRoomIDs = room.fetchAvailableRoomIDs("VIP", remainingRooms);
	                int vipRoomsToAdd = Math.min(remainingRooms, vipRoomIDs.size());
	                allocatedRoomIDs.addAll(vipRoomIDs.subList(0, vipRoomsToAdd));
	                remainingRooms -= vipRoomsToAdd;

	                // Mark the exclusive reward as redeemed
	                user.markExclusiveRewardAsRedeemed(userID);
	            }

	            // Allocate remaining rooms as Standard rooms if necessary
	            if (remainingRooms > 0) {
	                List<Integer> standardRoomIDs = room.fetchAvailableRoomIDs("Standard", remainingRooms);
	                int standardRoomsToAdd = Math.min(remainingRooms, standardRoomIDs.size());
	                allocatedRoomIDs.addAll(standardRoomIDs.subList(0, standardRoomsToAdd));
	                remainingRooms -= standardRoomsToAdd;
	            }
	        }
	        
	        // Create the booking
	        createBooking(allocatedRoomIDs, userID);
	        break; 
	    case "Non- member":
	        List<Integer> allocatedRoomIDs = new ArrayList<>();

	        // Check if Standard rooms are available
	        List<Integer> standardRoomIDs = room.fetchAvailableRoomIDs("Standard", 1); // Non-member can book only one room
	        if (!standardRoomIDs.isEmpty()) {
	            // If Standard rooms are available, allocate the room
	            allocatedRoomIDs.addAll(standardRoomIDs);
	            createBooking((ArrayList<Integer>) allocatedRoomIDs, userID);
	        } 
	        break;
	    }
	   }else {
	    	createBookingInQueue(userID);
	    	waitList.addWaiting(userID,bookingID);
	    }
	   
	}


	
	
	
	
	public void cancelBooking() throws SQLException {
		user.initializeConnection();
		//Get booking ID to delete
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
		if(waitList.isBookingInWaitingList(bookingID) ) {
			System.out.println("\n==========================================================");
			System.out.println("  Dear customer, your booking is still on the waiting list.");
			System.out.println("==========================================================");
			System.out.println("Are you sure want to cancel ? (Yes || No)");
			String choice = scanner.nextLine().trim(); 
			if(choice.equalsIgnoreCase("Yes")) {
				//Remove from booking table
				 String deleteQuery = "DELETE FROM booking WHERE bookingID = ?";
	                try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
	                    statement.setInt(1, bookingID);
	                    statement.executeUpdate();
	                } catch (SQLException e) {
	        	        e.printStackTrace();
	        	    }
				waitList.removeWaiting(bookingID); //Remove from waiting_list table
			}else {
				System.out.println("\n==============================================================================");
	            System.out.println("  Error Message: Booking with ID " + bookingID + " is not being remove from waiting list.");
	            System.out.println("==============================================================================");
			}
		}else {
			//Display selected booking
			if(searchBooking(bookingID)) {
				//Prompt user to confirm deletion, and remove from booking table if yes
				System.out.println("Do you want to continue the booking cancellation process? (Yes || No)");
				String choice = scanner.nextLine().trim(); 
		        if(choice.equalsIgnoreCase("Yes")) {
		                String deleteQuery = "DELETE FROM booking WHERE bookingID = ?";
		                try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
		                    statement.setInt(1, bookingID);
		                    int rowsAffected = statement.executeUpdate();
		                    if (rowsAffected > 0) {
		                        System.out.println("\n===================================================================");
		                        System.out.println("  Confirmation Message: Booking with ID " + bookingID + " successfully cancelled.");
		                        System.out.println("===================================================================");
		                       room.updateRoomStatus_available(bookingID);
		                    }
		         
		                } catch (SQLException e) {
		                    e.printStackTrace();
		                }
		        }else {
                	System.out.println("\n==========================================================");
    	            System.out.println("  Error Message: Booking with ID " + bookingID + " is not being cancel.");
    	            System.out.println("==========================================================");
                }
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
	                int roomQuantity = resultSet.getInt("roomQuantity");
	                Timestamp created = resultSet.getTimestamp("created_at");

	                System.out.println("\n===========================================");
	                System.out.println("    Booking ID    : " + bookedID);
	                System.out.println("    Room Quantity : " + roomQuantity);
	                System.out.println("    Creation Date : " + created);
	                System.out.println("===========================================");
	                
	                return true; // Return true when booking is found
	            } else {
	                System.out.println("\n======================================================");
	                System.out.println(" Dear Customer, the booking with ID " + bookingID + " is not found");
	                System.out.println("======================================================");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; 
	}

	
	public void setBookingID(int bookingID) {
		this.bookingID=bookingID;
	}
	
	public int getBookingID() {
		return bookingID;
	}

	public int getNumOfRoomToBook(String memberLevel) {
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

	    int numRoom;
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
	    } catch (InputMismatchException e) {
	        System.out.println("Invalid input. Please enter a valid number.");
	        scanner.nextLine(); // Clear the input buffer
	        numRoom = getNumOfRoomToBook(memberLevel); // Recursive call to get a valid input
	    }
	    return numRoom;
	}
	
	public void createBookingInQueue(int userID) {
	    try {
	        user.initializeConnection();
	        
	        // Insert into booking table
	        String bookingQuery = "INSERT INTO booking (userID, roomQuantity, status) VALUES (?, ?, ?)";
	        PreparedStatement bookingStatement = connection.prepareStatement(bookingQuery, Statement.RETURN_GENERATED_KEYS);
	        bookingStatement.setInt(1, userID);
	        bookingStatement.setInt(2, 0); // roomQuantity = 0
	        bookingStatement.setString(3, "In Queue"); // status = "In Queue"
	        int rowsInserted = bookingStatement.executeUpdate();
	        
	        if (rowsInserted > 0) {
	            // Get the generated booking ID
	            ResultSet generatedKeys = bookingStatement.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int bookingID = generatedKeys.getInt(1);
	                setBookingID(bookingID);
	                System.out.println("\n===========================================================");
	                System.out.println("    Booking created in queue successfully");
	                System.out.println("===========================================================");
	                // Print or handle the created booking information as needed
	            }
	        } else {
	            System.out.println("\n======================================================");
	            System.out.println("  Error Message: Failed to create booking in queue");
	            System.out.println("======================================================");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void setConnection(Connection connection) {
		this.connection=connection;
	}

}
