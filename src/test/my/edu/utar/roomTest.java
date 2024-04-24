package my.edu.utar;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class roomTest {

	private Room room;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        // Mock the required objects
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        // Create an instance of the Room class with the mocked connection
        room = new Room(connection);

        // Stub the prepareStatement method of the connection to return the prepared statement
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }
	
	@Test
	@Parameters({"5, VIP, 3", "2, Deluxe, 1", "3, Standard, 2"})
	public void testCheckRoom_NotEnoughRooms(int numRoomToBook, String roomType, int availableRoom) {
	    Room room = mock(Room.class);
	    
	    try {
	        // Mock the behavior of the fetchRoomCount method
	        when(room.fetchRoomCount(roomType)).thenReturn(availableRoom);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    boolean result = room.checkRoom(numRoomToBook, roomType);
	    
	    assertFalse(result);
	}
	@Test
	@Parameters({ "101, 300.00", "102, 800.00" })
	public void testDisplayAvailableRooms(int roomID, double price) throws SQLException {
	    // Define the room type for testing
	    String roomType = "Standard";

	    // Stub the executeQuery method of the prepared statement to return the result set
	    when(preparedStatement.executeQuery()).thenReturn(resultSet);

	    // Stub the next method of the result set to simulate multiple rows
	    when(resultSet.next()).thenReturn(true, false);

	    // Stub the getInt and getDouble methods of the result set to return dummy values
	    when(resultSet.getInt("roomID")).thenReturn(roomID);
	    when(resultSet.getDouble("roomPrice")).thenReturn(price);

	    // Call the displayAvailableRooms method
	    room.displayAvailableRooms(roomType);

	    int ER_roomIDs=resultSet.getInt("roomID");
	    double ER_price=resultSet.getDouble("roomPrice");
	    // Assert that the room ID and price match the expected values
	    assertEquals(roomID, ER_roomIDs);
	    assertEquals(price, ER_price, 0.0); 
	}


	@Test
    public void testFetchRoomCount() throws SQLException {
        // Define the expected room count
        int expectedRoomCount = 8;

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(expectedRoomCount);

        // Call the fetchRoomCount method with a dummy room type
        int actualRoomCount = room.fetchRoomCount("Standard");

        // Verify that the correct SQL query is prepared
        verify(connection).prepareStatement("SELECT COUNT(*) FROM room WHERE roomType = ? AND roomStatus = 'available'");
        verify(preparedStatement).setString(1, "Standard");
        verify(preparedStatement).executeQuery();
        verify(resultSet).getInt(1);

        // Check that the actual room count matches the expected room count
        assertEquals(expectedRoomCount, actualRoomCount);
    }

//getRoomTypeDependMember
	 @Test
	 @Parameters({"VIP, VIP", "Member, Deluxe", "Regular, Standard"})
	 public void testGetRoomTypeDependMember_v1(String memberLevel, String expectedRoomType)  {
		 String actualRoomType = room.getRoomTypeDependMember(memberLevel);

	        assertEquals(expectedRoomType, actualRoomType);
	}
	 
	 @Test
	 @Parameters({"VIP, Deluxe", "Member, Standard", "Regular, VIP"})
	 public void testGetRoomTypeDependMember_v2(String memberLevel, String expectedRoomType)  {
		 String actualRoomType = room.getRoomTypeDependMember(memberLevel);
  
	        assertNotEquals(expectedRoomType, actualRoomType);
	}
	 
// getRoomTypeDependID
	 @Test
	    @Parameters({"101, VIP", "102, Deluxe", "103, Standard"})
	    public void testGetRoomTypeDependID(int roomID, String expectedRoomType) throws SQLException {
	        // Stub the executeQuery method of the prepared statement to return the result set
	        when(preparedStatement.executeQuery()).thenReturn(resultSet);
	        when(resultSet.next()).thenReturn(true);
	        when(resultSet.getString("roomType")).thenReturn(expectedRoomType);

	        // Call the method to be tested
	        String actualRoomType = room.getRoomTypeDependID(roomID);

	        verify(connection).prepareStatement("SELECT roomType FROM room WHERE roomID = ?");
	        verify(preparedStatement).setInt(1, roomID);
	        verify(preparedStatement).executeQuery();
	        verify(resultSet).getString("roomType");

	        assertEquals(expectedRoomType, actualRoomType);
	    }
	 
	 @Test
	  @Parameters({"VIP", "Deluxe", "Standard"})
	    public void testSetRoomType_v1(String roomType) {

	        room.setRoomType(roomType);

	        // Verify that the room type is set correctly
	        assertEquals(roomType, room.getRoomType());
	    }
	 
	


}
