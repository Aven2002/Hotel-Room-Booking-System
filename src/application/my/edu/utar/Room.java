package my.edu.utar;

import java.sql.*;
import java.util.*;

public class Room {

	private Connection connection;
	private User user=new User();
	private String roomType;
	
	
	public Room() {
		this.connection=user.initializeConnection();
	}
	public Room(Connection connection) {
		this.connection=connection;
	}
	
	 public boolean checkRoom(int numRoomToBook,String room_type) {
		 int availableRoom=0;
	        try {
	        	availableRoom=fetchRoomCount(room_type);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        
	        if(numRoomToBook<=availableRoom) {
	        	return true;
	        }
	        return false;
	    }
	 
	public int fetchRoomCount(String roomType) throws SQLException {
	        String query = "SELECT COUNT(*) FROM room WHERE roomType = ? AND roomStatus = 'available'";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, roomType);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    return resultSet.getInt(1);
	                }
	            }
	        }
	        return 0;
	    }
	 
	 public void displayAvailableRooms(String roomType) {
	        String query = "SELECT roomID, roomPrice FROM room WHERE roomType = ? AND roomStatus = 'available'";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, roomType);
	            ResultSet resultSet = statement.executeQuery();
	            System.out.println("\nAvailable "+roomType+" Rooms:");
	            System.out.println("===========================================");
	            System.out.println("        Room ID      Room Price");
	            System.out.println("===========================================");
	            while (resultSet.next()) {
	                int roomID = resultSet.getInt("roomID");
	                double roomPrice = resultSet.getDouble("roomPrice");
	                System.out.printf("%12d %15.2f%n", roomID, roomPrice); 
	            }
	            System.out.println("===========================================");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	 }
	 
	public String getRoomTypeDependMember(String memberLevel) {
		String roomType;
		if(memberLevel.equalsIgnoreCase("VIP")) {
			roomType="VIP";
		}else if(memberLevel.equalsIgnoreCase("Member")) {
			roomType="Deluxe";
		}else {
			roomType="Standard";
		}
		return roomType;
	}
	
	public String getRoomTypeDependID(int roomID) throws SQLException {
	    String roomType = null;
	    String query = "SELECT roomType FROM room WHERE roomID = ?";
	    
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, roomID);
	        ResultSet resultSet = statement.executeQuery();
	        
	        if (resultSet.next()) {
	            roomType = resultSet.getString("roomType");
	        }
	    }
	    
	    return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType=roomType;
	}
	
	public String getRoomType() {
		return roomType;
	}
	
	public void updateRoomStatus_booked(ArrayList<Integer> roomIDs) {
	    try {
	        connection = dbConnector.getConnection();
	        String updateQuery = "UPDATE room SET roomStatus = 'Booked' WHERE roomID = ?";
	        PreparedStatement statement = connection.prepareStatement(updateQuery);
	        
	        // Update each room's status to booked
	        for (int roomID : roomIDs) {
	            statement.setInt(1, roomID);
	            statement.addBatch();
	        }
	        
	        // Execute batch update
	        int[] batchResults = statement.executeBatch();
	        int successfulUpdates = Arrays.stream(batchResults).sum();
	        
	        if (successfulUpdates != roomIDs.size()) {
	            System.out.println("\n===============================================");
	            System.out.println("  Error Message: Failed to update room status");
	            System.out.println("===============================================");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	 public ArrayList<Integer> fetchAvailableRoomIDs(String roomType, int numRooms) throws SQLException {
	        ArrayList<Integer> availableRoomIDs = new ArrayList<>();

	        String query = "SELECT roomID FROM room WHERE roomType = ? AND roomStatus = 'Available'";

	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, roomType);
	            ResultSet resultSet = statement.executeQuery();

	     
	            while (resultSet.next()) {
	                int roomID = resultSet.getInt("roomID");
	                availableRoomIDs.add(roomID);
	              
	            }
	        }
			return availableRoomIDs;
	    }

	
	public void updateRoomStatus_available(int bookingID) {
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
	}
	
}
