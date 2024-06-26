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
	
	public void addWaiting(int userID,int bookingID) {
		
	    String query = "INSERT INTO waiting_list (userID, bookingID) VALUES (?, ?)";

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, userID);
	        statement.setInt(2, bookingID);
	        int rowsInserted = statement.executeUpdate();
	        if (rowsInserted > 0) {
	        	System.out.println("\n===============================================================================");
	            System.out.println("   Dear customer, ");
	            System.out.println("   Since there isn't a room available right now, you will be add to waiting list");
	            System.out.println("================================================================================");
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
	        String roomType=room.getRoomTypeDependMember(memberLevel);

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
	        System.out.println("  Dear customer, you are not querying in the waiting list.");
	        System.out.println("=============================================================");
	    }
	    user.displayMenu();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

   }
	public void removeWaiting(int bookingID) {
	    String query = "DELETE FROM waiting_list WHERE bookingID = ?";

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, bookingID);
	        int rowsDeleted = statement.executeUpdate();

	        if (rowsDeleted > 0) {
	        	System.out.println("\n===========================================================");
	            System.out.println("  Booking with "+bookingID+" has been removed from the waiting list.");
	            System.out.println("===========================================================");
	        } else {
	        	System.out.println("\n=================================================");
	            System.out.println("  No waiting record found with booking ID " + bookingID + ".");
	            System.out.println("================================================");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public boolean isBookingInWaitingList(int bookingID) {
        String query = "SELECT * FROM waiting_list WHERE bookingID = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookingID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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