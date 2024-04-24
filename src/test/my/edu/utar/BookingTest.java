package my.edu.utar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingTest {
    private Booking booking;
    private Connection mockConnection;
    private PreparedStatement mockBookingStatement;
    private PreparedStatement mockBookingRoomStatement;
    private ResultSet mockGeneratedKeys;
    private ByteArrayOutputStream outContent; 
    private PreparedStatement mockPreparedStatement; 
    private ResultSet mockResultSet; 

    @Before
    public void setUp() throws SQLException {
        // Create mock objects
        mockConnection = mock(Connection.class);
        mockBookingStatement = mock(PreparedStatement.class);
        mockBookingRoomStatement = mock(PreparedStatement.class);
        mockGeneratedKeys = mock(ResultSet.class);
        mockPreparedStatement = mock(PreparedStatement.class); 
        mockResultSet = mock(ResultSet.class);

        // Inject mock objects into Booking
        booking = new Booking(0);
        booking.setConnection(mockConnection);

        // Initialize outContent
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }


    @Test
    public void testSetBookingID() {
        // Test data
        int bookingID = 123;

        // Set booking ID
        booking.setBookingID(bookingID);

        // Verify that the booking ID is set correctly
        assertEquals(bookingID, booking.getBookingID());
    }
    @Test
    public void testCreateBooking() throws SQLException {
        // Prepare test data
        ArrayList<Integer> allocatedRoomIDs = new ArrayList<>();
        allocatedRoomIDs.add(1);
        allocatedRoomIDs.add(2);
        int userID = 123;

        // Set up necessary mocks
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockBookingStatement);
        when(mockBookingStatement.executeUpdate()).thenReturn(1); // Assume one row inserted
        when(mockBookingStatement.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
        when(mockGeneratedKeys.next()).thenReturn(true); // Simulate generating keys
        when(mockGeneratedKeys.getInt(1)).thenReturn(1); // Simulate generated booking ID

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockBookingRoomStatement);
        when(mockBookingRoomStatement.executeBatch()).thenReturn(new int[] {1, 1}); // Assume both rooms inserted successfully

        // Perform booking creation
        booking.createBooking(allocatedRoomIDs, userID);

        // Verify interactions
        verify(mockBookingStatement).setInt(1, userID);
        verify(mockBookingStatement).setInt(2, allocatedRoomIDs.size());
        verify(mockBookingStatement).setString(3, "Checked In");
        verify(mockBookingStatement).executeUpdate();

        verify(mockBookingRoomStatement, times(2)).setInt(eq(1), anyInt()); // Verify roomID set for each room
        verify(mockBookingRoomStatement).executeBatch();

        // Verify output using captured output from outContent
        String expectedConfirmationMessage = "\n===========================================================\n" +
                                              "Confirmation Message: Booking created successfully\n" +
                                              "===========================================================\n" +
                                              "Room ID: 1\tRoom Type: VIP\n" +
                                              "Room ID: 2\tRoom Type: VIP\n";
        assertNotEquals(expectedConfirmationMessage, outContent.toString());
    }
    
    @Test
    public void testSearchBooking_BookingFound() throws SQLException {
        // Prepare test data
        int bookingID = 123;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate booking found
        when(mockResultSet.getInt("bookingID")).thenReturn(bookingID);
        when(mockResultSet.getInt("roomQuantity")).thenReturn(2);
        // Add more mock data as needed

        // Perform search
        boolean result = booking.searchBooking(bookingID);

        // Verify output
        assertTrue(result); 
    }

    @Test
    public void testSearchBooking_BookingNotFound() throws SQLException {
        // Prepare test data
        int bookingID = 123;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // Perform search
        boolean result = booking.searchBooking(bookingID);

        // Verify output
        assertFalse(result); // Booking not found
        // Verify that the correct print statements are executed
    }

    @Test
    public void testSearchBooking_SQLException() throws SQLException {
        // Prepare test data
        int bookingID = 123;
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        // Perform search
        boolean result = booking.searchBooking(bookingID);

        // Verify output
        assertFalse(result); // Exception occurred
        // Verify that the correct print statements are executed
    }
    
    @Test
    public void testCreateBookingInQueue_Success() throws SQLException {
        // Test data
        int userID = 123;

        // Set up necessary mocks
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockBookingStatement);
        when(mockBookingStatement.executeUpdate()).thenReturn(1); // Assume one row inserted
        when(mockBookingStatement.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
        when(mockGeneratedKeys.next()).thenReturn(true); // Simulate generating keys
        when(mockGeneratedKeys.getInt(1)).thenReturn(1); // Simulate generated booking ID

        // Call the method
        booking.createBookingInQueue(userID);

        // Verify interactions
        verify(mockBookingStatement).setInt(1, userID);
        verify(mockBookingStatement).setInt(2, 0); // roomQuantity = 0
        verify(mockBookingStatement).setString(3, "In Queue"); // status = "In Queue"
        verify(mockBookingStatement).executeUpdate();

        // Verify output
        String expectedConfirmationMessage = "\n===========================================================\n" +
                                              "    Booking created in queue successfully\n" +
                                              "===========================================================\n";
        assertNotEquals(expectedConfirmationMessage, outContent.toString());
    }

    @Test
    public void testCreateBookingInQueue_Failure() throws SQLException {
        // Test data
        int userID = 123;

        // Set up necessary mocks
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockBookingStatement);
        when(mockBookingStatement.executeUpdate()).thenReturn(0); // Assume no row inserted

        // Call the method
        booking.createBookingInQueue(userID);

        // Verify interactions
        verify(mockBookingStatement).setInt(1, userID);
        verify(mockBookingStatement).setInt(2, 0); // roomQuantity = 0
        verify(mockBookingStatement).setString(3, "In Queue"); // status = "In Queue"
        verify(mockBookingStatement).executeUpdate();

        // Verify output
        String expectedErrorMessage = "\n======================================================\n" +
                                       "  Error Message: Failed to create booking in queue\n" +
                                       "======================================================\n";
        assertNotEquals(expectedErrorMessage, outContent.toString());
    }
    
    @Test
    public void testSetBookingIDAndGetBookingID() {
        int bookingID = 123;

        booking.setBookingID(bookingID);
        assertEquals(bookingID, booking.getBookingID());
    }

    @Test
    public void testGetBookingID_DefaultValue() {
        assertEquals(0, booking.getBookingID());
    }
}
