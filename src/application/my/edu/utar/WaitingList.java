package my.edu.utar;

import java.sql.*;
import java.util.*;

public class WaitingList {
	ArrayList<String> VIP = new ArrayList<String>();
	ArrayList<String> member = new ArrayList<String>();
	ArrayList<String> normal = new ArrayList<String>();
	private final Connection connection;
	
	public WaitingList(Connection connection) {
		this.connection=connection;
	}
	
	public void addWaiting() {
		
	}
	public void getWaiting(int userID) {
        String query = "SELECT * FROM waiting_list WHERE userID = ?";
	try (PreparedStatement statement = connection.prepareStatement(query)) {
           statement.setInt(1, userID);
           ResultSet resultSet = statement.executeQuery();
           
           if (resultSet.next()) {
               // User is in the waiting list, display the information
               int waitingID = resultSet.getInt("waitListID");  
               System.out.println("User is in the waiting list.");
               System.out.println("Waiting ID: " + waitingID);
           } else {
               System.out.println("User is not in the waiting list.");
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
	public void removeWaiting() {
		
	}
	


	
	
	
}