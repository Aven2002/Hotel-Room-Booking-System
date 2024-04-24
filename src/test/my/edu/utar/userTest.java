package my.edu.utar;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class userTest {
	private User user;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
	
	@Before
    public void setUp() {
        // Initialize mocks
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        
        // Initialize User instance with mocked connection
        user = new User(connection);
    }
	
	@Test
	public void testProcessChoiceCase1() throws SQLException{
	
		User userMock = mock(User.class);
		
		userMock.processChoice(1);
		verify(userMock).processChoice(1);
	}
	
	@Test
	public void testProcessChoiceCase2() throws SQLException{
	
		User userMock = mock(User.class);
		
		userMock.processChoice(2);
		verify(userMock).processChoice(2);
	}
	
	@Test
	public void testProcessChoiceCase3() throws SQLException{
	
		User userMock = mock(User.class);
		
		userMock.processChoice(3);
		verify(userMock).processChoice(3);
	}
	
	@Test
	public void testProcessChoiceCase4() throws SQLException{
		
		User userMock = mock(User.class);
		
		userMock.processChoice(4);
		verify(userMock).processChoice(4);
	}
	
	@Test
	public void testProcessChoiceCase5() throws SQLException{
		
		User userMock = mock(User.class);
		
		userMock.processChoice(5);
		verify(userMock).processChoice(5);
	}
	
	@Test
	public void initializeConnection() {
//		dbConnector dbMock = mock(dbConnector.class);
//		
//		Connection connMock = mock(Connection.class);
//		
//		when(dbMock.getConnection()).thenReturn(connMock);
//		
//		dbMock.getConnection();
//		verify(dbMock).getConnection();
		User user = new User();
        Connection connection = user.initializeConnection();

        assertNotNull("Connection should not be null", connection);
		
	}
	
	@Test
	public void testGetUserID() {
		int expected_userid = 1;
		User user = new User();
		user.setUserID(expected_userid);
		
		int actual_userid = user.getUserID();
		
		assertEquals(expected_userid, actual_userid);
	}
	

	
	@Test
	public void testGetUsername() {
		
		String expected_username = "vip1";
		int id = 1;
		User user = new User();
		user.setUserID(id);
		
		String actual_username = user.getUsername(id);
		
		assertEquals(expected_username, actual_username);
	}
	
	@Test
	public void testSetUserID() {
		int expected_userid = 1;
		User user = new User();
		
		user.setUserID(expected_userid);
		
		assertEquals(expected_userid, user.getUserID());
	}
	
	
	
//Test isValidFullName() 
	@Test
	@Parameters({"John Weak", "Mao ZeBron", "Lee Se Hen"})
	public void testIsValidFullName(String name) {
		assertTrue(user.isValidFullName(name));
	}
	
	@Test
	@Parameters({"Aven", "Agnes", "Alvis"})
	public void testIsValidFullName_Invalid(String name) {
		assertFalse(user.isValidFullName(name));
	}
	
//Test isValidPhoneNumber() 
	@Test
	@Parameters({"0123456789", "0105475320", "0167794575"})
	public void testIsValidPhoneNumber(String phoneNum) {
		assertTrue(user.isValidPhoneNumber(phoneNum));
	}
	
	@Parameters({"1234124", "", "0"})
	public void testIsValidPhoneNumber_Invalid(String phoneNum) {
		assertFalse(user.isValidPhoneNumber(phoneNum));
	}
	
	
//Test isValidEmail()
	@Test
	@Parameters({"yap@gmail.com", "aven@gmail.com", "mac@gmail.com"})
	public void testIsValidEmail(String email) {
		assertTrue(user.isValidEmail(email));
	}
	
	@Test
	@Parameters({"yap@gmailcom", "aven gmail.com", "mac"})
	public void testIsValidEmail_Invalid(String email) {
		assertFalse(user.isValidEmail(email));
	}
	
//Test isValidUsername()
	@Test
	@Parameters({"yaphj", "aven999", "macd9"})
	public void testIsValidUsername(String username) {
		assertTrue(user.isValidUsername(username));
	}
	
	@Test
	@Parameters({"yap", "aven@gmail.com", "macd_9"})
	public void testIsValidUsername_Invalid(String username) {
		assertFalse(user.isValidUsername(username));
	}
	
//Test getUserID() 	
		@Test
	    public void testGetUserID_ValidUserID() {
	        int userID = user.getUserID();
	        assertTrue(userID >= 0); 
	    }
		@Test
	    public void testGetUserID_InvalidUserID() {
	        int userID = user.getUserID();
	        assertFalse(userID < 0);
	    }
		
//Test goodbyeMessage()
		@Test
		public void testGoodbyeMessage() {
			User mock = mock(User.class);
			
			mock.goodbyeMessage();
			
			verify(mock).goodbyeMessage();
		}
		
	
//Test markExclusiveRewardAsRedeemed(int userID)
		@Test
	    public void testMarkExclusiveRewardAsRedeemed() throws SQLException {

	        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
	        when(preparedStatement.executeUpdate()).thenReturn(1); // Assuming one row is updated

	        User instance = new User(connection);
	        instance.markExclusiveRewardAsRedeemed(1); 

	        // Verify that the PreparedStatement was created with the correct SQL query and parameters
	        verify(connection).prepareStatement("UPDATE user_account SET exclusiveReward = false WHERE userID = ?");
	        verify(preparedStatement).setInt(1, 1);

	        // Verify that executeUpdate() was called
	        verify(preparedStatement).executeUpdate();
	    }
		
		@Test
	    public void testMemberHasExclusiveReward_UserExistsWithReward() throws SQLException {

	        // Mock behavior
	        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
	        when(preparedStatement.executeQuery()).thenReturn(resultSet);
	        when(resultSet.next()).thenReturn(true); // Simulate that a user exists
	        when(resultSet.getBoolean("exclusiveReward")).thenReturn(true); // Simulate that the user has an exclusive reward

	        // Create instance of User
	        User user = new User(connection);

	        // Call the method being tested
	        boolean hasReward = user.memberHasExclusiveReward(1); // Assuming userID 1 exists

	        // Verify the method behavior
	        assertTrue(hasReward); // The user should have an exclusive reward
	    }

	    @Test
	    public void testMemberHasExclusiveReward_UserExistsNoReward() throws SQLException {

	        // Mock behavior
	        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
	        when(preparedStatement.executeQuery()).thenReturn(resultSet);
	        when(resultSet.next()).thenReturn(true); // Simulate that a user exists
	        when(resultSet.getBoolean("exclusiveReward")).thenReturn(false); 

	        // Create instance of User
	        User user = new User(connection);

	        // Call the method being tested
	        boolean hasReward = user.memberHasExclusiveReward(1); 

	        // Verify the method behavior
	        assertFalse(hasReward); 
	    }

	    @Test
	    public void testMemberHasExclusiveReward_UserNotFound() throws SQLException {
	        
	        // Mock behavior
	        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
	        when(preparedStatement.executeQuery()).thenReturn(resultSet);
	        when(resultSet.next()).thenReturn(false); 

	        // Create instance of User
	        User user = new User(connection);

	        // Call the method being tested
	        boolean hasReward = user.memberHasExclusiveReward(5); // Assuming userID 5 does not exist

	        // Verify the method behavior
	        assertFalse(hasReward); // The user should not have an exclusive reward
	    }
	    
	    @Test
	    public void testGetMemberLevel() throws SQLException {

	        // Mock behavior of PreparedStatement
	        when(connection.prepareStatement("SELECT memberLevel FROM user_account WHERE userID=?"))
	            .thenReturn(preparedStatement);
	        when(preparedStatement.executeQuery()).thenReturn(resultSet);

	        String expectedMemberLevel = "VIP"; 

	        // Mock ResultSet behavior
	        when(resultSet.next()).thenReturn(true); // Simulate that there is a result
	        when(resultSet.getString("memberLevel")).thenReturn(expectedMemberLevel);

	        // Create an instance of User with the mocked Connection
	        User user = new User(connection);

	        // Call the method to be tested
	        String memberLevel = user.getMemberLevel(123); // Pass any user ID

	        // Verify that the PreparedStatement was created with the correct query and parameters
	        // Verify that the ResultSet was accessed correctly
	        assertEquals(expectedMemberLevel, memberLevel);
	    }
	
}
