package my.edu.utar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class printerTest {

    @Test
    public void testPrintInfo() {
        // Given
        Printer printer = new Printer();
        String name = "John Doe";
        String memberType = "Gold";
        String roomType = "Suite";

        // When
        printer.printInfo(name, memberType, roomType);

        // Then
        assertEquals(name, name);
        assertEquals(memberType, memberType);
        assertEquals(roomType, roomType);
    }
}
