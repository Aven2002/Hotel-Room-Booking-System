package my.edu.utar;

import static org.junit.Assert.*;

import org.junit.Test;

public class roomTest {

	private Room room=new Room();
	
	@Test
	public void checkRoomTest() {
		boolean result=room.checkRoom("VIP");
		assertTrue(result);
	}

//	@Test
//	public void fetchRoomCountTest() {
//		int result=room.fetchRoomCount();
//	}
}
