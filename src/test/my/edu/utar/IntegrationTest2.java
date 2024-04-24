package my.edu.utar;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntegrationTest2 {

	@Test
	public void testValidFullname() {
		  User user = new User();
		  
		  String valid_name = "Yap Harw Jien";
		  
		  boolean validResult = user.isValidFullName(valid_name);
		  
		  assertTrue("Valid name", validResult);
	}
	
	@Test
	public void testInvalidFullname() {
		  User user = new User();
		  
		  String valid_username = "Aven";
		  
		  boolean validResult = user.isValidUsername(valid_username);
		  
		  assertFalse("Invalid username", validResult);
	}

}
