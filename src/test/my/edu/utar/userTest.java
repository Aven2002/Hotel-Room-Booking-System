package my.edu.utar;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class userTest {
	
	
//Test isValidFullName() method
	@Test
	@Parameters({"John Weak", "Mao ZeBron", "Lee Se Hen"})
	public void testIsValidFullName(String name) {
		User user = new User();
		assertTrue(user.isValidFullName(name));
	}
	
	@Test
	@Parameters({"Aven", "Agnes", "Alvis"})
	public void testIsValidFullName_Invalid(String name) {
		User user = new User();
		assertFalse(user.isValidFullName(name));
	}
	
//Test isValidPhoneNumber() method
	@Test
	@Parameters({"0123456789", "0105475320", "0167794575"})
	public void testIsValidPhoneNumber(String phoneNum) {
		User user = new User();
		assertTrue(user.isValidPhoneNumber(phoneNum));
	}
	
	@Parameters({"1234124", "", "0"})
	public void testIsValidPhoneNumber_Invalid(String phoneNum) {
		User user = new User();
		assertFalse(user.isValidPhoneNumber(phoneNum));
	}
	
	
//Test isValidEmail()method
	@Test
	@Parameters({"yap@gmail.com", "aven@gmail.com", "mac@gmail.com"})
	public void testIsValidEmail(String email) {
		User user = new User();
		assertTrue(user.isValidEmail(email));
	}
	
	@Test
	@Parameters({"yap@gmailcom", "aven gmail.com", "mac"})
	public void testIsValidEmail_Invalid(String email) {
		User user = new User();
		assertFalse(user.isValidEmail(email));
	}
	
//Test isValidUsername()method
	@Test
	@Parameters({"yaphj", "aven999", "macd9"})
	public void testIsValidUsername(String username) {
		User user = new User();
		assertTrue(user.isValidUsername(username));
	}
	
	@Test
	@Parameters({"yap", "aven@gmail.com", "macd_9"})
	public void testIsValidUsername_Invalid(String username) {
		User user = new User();
		assertFalse(user.isValidUsername(username));
	}
	
//Test getUserID() method	
		@Test
	    public void testGetUserID_ValidUserID() {
	        User user = new User();
	        int userID = user.getUserID();
	        assertTrue(userID >= 0); 
	    }
		@Test
	    public void testGetUserID_InvalidUserID() {
	        User user = new User();
	        int userID = user.getUserID();
	        assertFalse(userID < 0);
	    }


}
