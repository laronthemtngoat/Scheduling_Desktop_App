/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import static DAO.CustomerSQL.getPreparedStatement;
import static DAO.CustomerSQL.setPreparedStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scheduling_desktop_ui.SchedulingDesktopApp;

/**
 *
 * @author laron
 */
public class UserSQL {
    
    public static int userId = 0;
    
    
    public static void selectUserSQL(Connection conn, String username) {
        
        String selectUser = "SELECT * FROM user";
        
        try{
            
            //create prepared statement object
            setPreparedStatement(conn, selectUser);
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();
            
            //execute prepared statements
            ps.execute();

            ResultSet rs = ps.getResultSet();
            if(rs.next()) {
                
               
            }
        }catch(SQLException e) {
            System.out.println(e);
        }                
    }
    
    public static void insertUserSQL() {
        
    }
    
    public static void updateUserSQL() {
        
    }
    
    public static void deleteUserSQL() {
        
    }    

    public static void getUserIdSQL(Connection conn) {
        
        //SQL select statment to check for existing address
        String selectUsers = "SELECT userId FROM user WHERE userName=?";
        
        try{
            //create prepared statement object
            setPreparedStatement(conn, selectUsers);
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();
            
            ps.setString(1, SchedulingDesktopApp.currentUser);
            //execute prepared statements
            ps.execute();      
            //create result set
            ResultSet rs = ps.getResultSet();
            
            if(rs.next()) {
                
                userId = rs.getInt("userId");                
                               
            }
            else {
                
                System.out.println("No such user");
            }                        
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
    }
    
    private static int maxUserId(Connection conn) {
        
        int userMaxId = 0;
        
        String maxSQL = "SELECT MAX(userId) FROM user";
        
        try {
            
            setPreparedStatement(conn, maxSQL);
            PreparedStatement ps = getPreparedStatement();
            
            //execute prepared statements
            ps.execute();
        
            ResultSet rs = ps.getResultSet();            
            //create prepared statement object
            if(rs.next()) {
                
                userMaxId = rs.getInt(1);
            }                                    
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
            
        }
        
        return userMaxId + 1;
    }    
    
}
