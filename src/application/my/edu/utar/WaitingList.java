package my.edu.utar;

import java.sql.*;
import java.util.*;

public class WaitingList {
	ArrayList<String> VIP = new ArrayList<String>();
	ArrayList<String> member = new ArrayList<String>();
	ArrayList<String> normal = new ArrayList<String>();
	private Connection connection;
	private User user=new User();
	private Room room=new Room();
	
	public WaitingList() {
		this.connection=user.initializeConnection();
	}
	
	public void addWaiting(int userID) {
		
	    String query = "INSERT INTO waiting_list (userID) VALUES (?)";

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, userID);
	        int rowsInserted = statement.executeUpdate();
	        if (rowsInserted > 0) {
	        	System.out.println("\n==============================================");
	            System.out.println("   New waiting record added successfully.");
	            System.out.println("==============================================");
	        } else {
	        	System.out.println("\n==============================================");
	            System.out.println("   Failed to add new waiting record.");
	            System.out.println("==============================================");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }	
	}
	public void getWaiting(int userID) {
		String query = "SELECT w.waitListID, u.username, u.memberLevel, w.created_at " +
	               "FROM waiting_list w " +
	               "INNER JOIN user_account u ON w.userID = u.userID " +
	               "WHERE w.userID = ?";
	try (PreparedStatement statement = connection.prepareStatement(query)) {
	    statement.setInt(1, userID); 
	    ResultSet resultSet = statement.executeQuery();

	    if (resultSet.next()) {
	        int waitingID = resultSet.getInt("waitListID");
	        String username = resultSet.getString("username");
	        Timestamp timestamp = resultSet.getTimestamp("created_at");
	        String memberLevel=resultSet.getString("memberLevel");
	        String roomType=room.getRoomType(memberLevel);

	        System.out.println("\n==============================================");
	        System.out.println("                Waiting Status                 ");
	        System.out.println("==============================================");
	        System.out.println("   Waiting ID   : " + waitingID);
	        System.out.println("   Username     : " + username);
	        System.out.println("   Room Type    : "+ roomType);
	        System.out.println("   Created At   : " + timestamp);
	        System.out.println("==============================================");
	    } else {
	    	System.out.println("\n============================================================");
	        System.out.println("  Dear customer, you are not quering in the waiting list.");
	        System.out.println("=============================================================");
	    }
	    user.displayMenu();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

   }
	public void removeWaiting(int waitListID) {
	    String query = "DELETE FROM waiting_list WHERE waitListID = ?";

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, waitListID);
	        int rowsDeleted = statement.executeUpdate();

	        if (rowsDeleted > 0) {
	        	System.out.println("\n===========================================================");
	            System.out.println("  Waiting record with ID " + waitListID + " has been removed successfully.");
	            System.out.println("===========================================================");
	        } else {
	        	System.out.println("\n=========================================");
	            System.out.println("  No waiting record found with ID " + waitListID + ".");
	            System.out.println("=========================================");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


//	public static void main(String[]args) {
//		dbConnector db=new dbConnector();
//		try {
//			Connection connection=dbConnector.getConnection();
//			WaitingList w = new WaitingList(connection);
//			w.addWaiting(5);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

	
	
	
}