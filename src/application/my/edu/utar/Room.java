package my.edu.utar;

import java.sql.*;
import java.util.*;

public class Room {

	private Connection connection;
	private User user=new User();
	public Room() {
		this.connection=user.initializeConnection();
	}
	
	 public boolean checkRoom(String room_type) {
		 int result=0;
	        try {
			 result=fetchRoomCount(room_type);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        
	        if(result>0) {
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
	                System.out.printf("%12d %15.2f%n", roomID, roomPrice); // Adjusted formatting
	            }
	            System.out.println("===========================================");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	 }
	 
	public String getRoomType(String memberLevel) {
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
	
	public void updateRoomStatus(ArrayList<Integer> roomIDs) {
	    try {
	        connection = dbConnector.getConnection();
	        String updateQuery = "UPDATE room SET roomStatus = 'booked' WHERE roomID = ?";
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

	
	public static void main(String[]args) {
		Room room =new Room();
		boolean result=room.checkRoom("VIP");
		System.out.println(result);
	}
}
