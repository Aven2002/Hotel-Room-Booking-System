package my.edu.utar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class printerTest {

	private Object[] validData() {
		return new Object[] {
				new Object[] {"John Doe", "member", "Standard"},
				new Object[] {"Lee Wai Kin", "VIP", "VIP"},
				new Object[] {"Kishor Rames", "member", "Deluxe"},
				new Object[] {"Mao ZeBron", "VIP", "Deluxe"}
		};
	}
	
	
	@Test
	@Parameters(method = "validData")
	public void testPrintInfo(String name, String memberType, String roomType) {
        
        Printer mock = mock(Printer.class);
        
        // Call the method under test on the Mock object
        mock.printInfo(name, memberType, roomType);
        
        // Verify that the printInfo method was called 
        // on the mock with the correct arguments
        verify(mock).printInfo(name, memberType, roomType);
	}
	
	
	@Test
	public void testPrintInfoInvalid() {
        String name = null;
        String memberType = "member";
        String roomType = "Standard";
        
        Printer mock = mock(Printer.class);
        
        // Call the method under test on the Mock object
        mock.printInfo(name, memberType, roomType);
        
        // Verify that the printInfo method was NOT called 
        // on the mock with the correct arguments
        verify(mock, never()).printInfo(anyString(), anyString(), anyString());
	}
	

}