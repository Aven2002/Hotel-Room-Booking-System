package my.edu.utar;

import java.sql.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;


import org.junit.Test;
import org.junit.runner.RunWith;
import junitparams.Parameters;
import junitparams.JUnitParamsRunner;
import junitparams.FileParameters;

@RunWith(JUnitParamsRunner.class)
public class WaitingListTest {

	private Object[] userID() {
	return new Object[] {
			new Object[] {"1"},
			new Object[] {"2"},
			new Object[] {"3"},
			new Object[] {"4"},
			new Object[] {"5"},
	};
}
	@Test
	@Parameters(method = "userID")
	public void testAddWaiting(int userID) {
		int bookingID = 1;
        WaitingList waitMock = mock(WaitingList.class);
        waitMock.addWaiting(userID, bookingID);

	}
	
	@Test(expected = IllegalArgumentException.class)
	@FileParameters("invalidvalue.txt")
	public void testAddWaitingInvalid(int userID) {

		int bookingID = 1;
        WaitingList waitMock = mock(WaitingList.class);
        waitMock.addWaiting(userID, bookingID);

	}
	
	@Test
	@FileParameters("user_id.txt")
	public void testGetWaiting(int userID) {

        WaitingList waitMock = mock(WaitingList.class);
        waitMock.getWaiting(userID);

	}
	
	@Test(expected = IllegalArgumentException.class)
	@FileParameters("invalidvalue.txt")
	public void testGetWaitingInvalid(int userID) {

        WaitingList waitMock = mock(WaitingList.class);
        waitMock.getWaiting(userID);

	}
	
	
	@Test
	@FileParameters("user_id.txt")
	public void testRemoveWaiting(int waitListID) {

        WaitingList waitMock = mock(WaitingList.class);
        waitMock.removeWaiting(waitListID);

	}
	
	
	@Test(expected = IllegalArgumentException.class)
	@FileParameters("invalidvalue.txt")
	public void testRemoveWaitingInvalid(int waitListID) {
		WaitingList waitMock = mock(WaitingList.class);
        waitMock.removeWaiting(waitListID);
	}
}

//@Parameters({"null", "cat", "999999999999999999"})
