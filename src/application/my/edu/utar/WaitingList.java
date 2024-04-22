package my.edu.utar;

import java.sql.*;
import java.util.*;

public class WaitingList {
	ArrayList<String> VIP = new ArrayList<String>();
	ArrayList<String> member = new ArrayList<String>();
	ArrayList<String> normal = new ArrayList<String>();
	private final Connection connection;
	User user=new User();
	
	public WaitingList(Connection connection) {
		this.connection=connection;
	}
	
	public void addWaiting() {
		
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
	        String roomType=getRoomType(memberLevel);

	        System.out.println("\n==============================================");
	        System.out.println("                Waiting Status                 ");
	        System.out.println("==============================================");
	        System.out.println("   Waiting ID   : " + waitingID);
	        System.out.println("   Username     : " + username);
	        System.out.println("   Room Type    : "+ roomType);
	        System.out.println("   Created At   : " + timestamp);
	        System.out.println("==============================================");
	    } else {
	    	System.out.println("\n==============================================");
	        System.out.println("     No waiting record found for the user.");
	        System.out.println("==============================================");
	    }
	    user.displayMenu();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

   }
	public void removeWaiting() {
		
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


	
	
	
}