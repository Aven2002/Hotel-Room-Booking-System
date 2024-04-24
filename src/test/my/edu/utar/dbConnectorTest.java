package my.edu.utar;

import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

public class dbConnectorTest {

    @Test
    public void testConnection() {
        try {
            Connection conn = dbConnector.getConnection();
            assertNotNull("Connection should not be null", conn);
            conn.close(); // Close the connection after testing
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}