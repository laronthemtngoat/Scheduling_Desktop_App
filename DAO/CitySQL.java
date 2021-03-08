/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import static DAO.CustomerSQL.getPreparedStatement;
import static DAO.CustomerSQL.rowsAffected;
import static DAO.CustomerSQL.setPreparedStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import model.DateTime;
import scheduling_desktop_ui.SchedulingDesktopApp;

/**
 *
 * @author laron
 */
public class CitySQL {
    
    public static int cityId = 0;

    //SQL statement to add to DB
    public static void insertCitySQL(Connection conn, String customerName, 
            String address, String address2, String city, String country, 
            String postalCode, String phone) throws SQLException {
        
        String insertCustomerStatement = "INSERT INTO city "
                + "(cityId, city, countryId, "
                + "createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        //create prepared statement object
        setPreparedStatement(conn, insertCustomerStatement);
        
        //get prepared statement reference
        PreparedStatement ps = getPreparedStatement();                        
        
        //determine date and time
        Timestamp currentDate = DateTime.currentDateTime();
        
        //key-value mapping
        ps.setInt(1, CitySQL.cityId);
        ps.setString(2, city);        
        ps.setInt(3, CountrySQL.countryId);
        ps.setTimestamp(4, currentDate);        
        ps.setString(5, SchedulingDesktopApp.currentUser);
        ps.setTimestamp(6, currentDate);
        ps.setString(7, SchedulingDesktopApp.currentUser);
                
        //execute prepared statement
        ps.execute();
        
        //affected rows
        rowsAffected();
                
    } 
    
    public static boolean checkCitySQL(Connection conn, String city) {
        
        //SQL select statment to check for existing address
        String selectAllAddress = "SELECT * FROM city WHERE city=?";
        boolean flag = false;
        try{
            //create prepared statement object
            setPreparedStatement(conn, selectAllAddress);
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();
            
            ps.setString(1, city);            
            //execute prepared statements
            ps.execute();      
            //create result set
            ResultSet rs = ps.getResultSet();
            
            if(rs.next()) {
                
                cityId = rs.getInt("cityId");
                flag = true;
                                
            }
            else{
                
                cityId = maxCityId(conn);
            }
            
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
        
       return flag;
    }

    public static int maxCityId(Connection conn) {
        
        
        int cityMaxId = 0;
        
        String maxSQL = "SELECT MAX(cityId) FROM city";
        
        try {
            
            setPreparedStatement(conn, maxSQL);
            PreparedStatement ps = getPreparedStatement();
            
            //execute prepared statements
            ps.execute();
        
            ResultSet rs = ps.getResultSet();            
            //create prepared statement object
            if(rs.next()) {
                
                cityMaxId = rs.getInt(1);
            }                                    
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
            
        }
        
        return cityMaxId + 1;        
    }    
    
}
