package my.edu.utar;

import java.util.*;
import java.sql.*;

public class AppManager {
    
    private Scanner scanner = new Scanner(System.in);
    private static Connection connection;
    private User user=new User();

    public static void main(String[] args) {
        AppManager app = new AppManager();
        app.welcomePage();
    }
    
    public AppManager() {
    	initializeConnection();
    }
    

    

    
   
}