package my.edu.utar;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import org.junit.Test;

@RunWith(value = Suite.class)
@SuiteClasses(value = {
		WaitingListTest.class,
		roomTest.class,
		BookingTest.class,
		dbConnectorTest.class,
		printerTest.class,
		userTest.class,
		IntegrationTest1.class,
		IntegrationTest2.class
		
	
})
public class TestSuite {

}
