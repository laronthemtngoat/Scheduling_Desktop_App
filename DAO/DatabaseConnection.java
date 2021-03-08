/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *DB Connection String
 *Server name: wgudb.ucertify.com (ip: 3.227.166.251)
 *Port: 3306
 *Database name: U05m11
 *Username: U05m11
 *Password: 53688545345
 */

/**
 *
 * @author laron
 */
public class DatabaseConnection {
    // Connect to your database.
    // Replace server name, username, and password with your credentials
        
        //DB Connection String
    private static final String serverName = "3.227.166.251",
        connectionUrl = "jdbc:mysql://3.227.166.251:3306/U05m11",
        dbUserName = "U05m11",
        dbPassword = "53688545345",
        driver = "com.mysql.jdbc.Driver";        
        
    public static Connection dbConn;
        
    public static Connection estConnection() {
            
        try {
                
            Class.forName(driver);
                
            dbConn = DriverManager.getConnection(connectionUrl, 
            dbUserName, dbPassword);
            System.out.println("Connected"); 
        
        }
        catch(ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());            
        
        }
           return dbConn; 
    }
    
    
    public static void dbCloseConnection() {
        
        try {
            
            System.out.println("...closing database connection");
            
            dbConn.close();
            
            System.out.println("database connection closed");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());            
        
        }        
        
        
    }
    

}