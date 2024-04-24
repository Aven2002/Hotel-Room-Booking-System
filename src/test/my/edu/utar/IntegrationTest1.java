package my.edu.utar;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntegrationTest1 {

	@Test
	public void testValidUsername() {
		  User user = new User();
		  
		  String valid_username = "aven999";
		  
		  boolean validResult = user.isValidUsername(valid_username);
		  
		  assertTrue("Valid username", validResult);
	}
	
	@Test
	public void testInvalidUsername() {
		  User user = new User();
		  
		  String valid_username = "mac";
		  
		  boolean validResult = user.isValidUsername(valid_username);
		  
		  assertFalse("Invalid username", validResult);
	}

}
