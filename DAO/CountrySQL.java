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
import model.Country;
import model.DateTime;
import scheduling_desktop_ui.SchedulingDesktopApp;

/**
 *
 * @author laron
 */
public class CountrySQL {
    
    public static int countryId = 0;
    
    //SQL statement to return all countries
    public static void selectCountriesSQL(Connection conn) throws SQLException {
        
        //SQL statement
        String selectStatement = "SELECT * FROM country";
        
        //create prepared statement object
        setPreparedStatement(conn, selectStatement);
        
        try(PreparedStatement ps = getPreparedStatement()) {
            
            //execute prepared statement
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()) {
                
                int cId = rs.getInt("countryId");
                String countryName = rs.getString("country");
                Timestamp createDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                
                Country country = new Country(cId, countryName, 
                createDate, createdBy, 
                lastUpdate, lastUpdateBy);
                
                Country.addCountry(country);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    //SQL statement to add to DB
    public static void insertCountrySQL(Connection conn, String customerName, 
            String address, String address2, String city, String country, 
            String postalCode, String phone) throws SQLException {
        
        String insertCustomerStatement = "INSERT INTO country "
                + "(countryId, country, "
                + "createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES(?, ?, ?, ?, ?, ?)";
        
        //create prepared statement object
        setPreparedStatement(conn, insertCustomerStatement);
        
        //get prepared statement reference
        PreparedStatement ps = getPreparedStatement();                
        
        //check for existing country
        CountrySQL.checkCountrySQL(conn, country);
        
        //determine date and time
        Timestamp currentDate = DateTime.currentDateTime();
        
        //key-value mapping
        ps.setInt(1, countryId);
        ps.setString(2, country);        
        ps.setTimestamp(3, currentDate);        
        ps.setString(4, SchedulingDesktopApp.currentUser);
        ps.setTimestamp(5, currentDate);
        ps.setString(6, SchedulingDesktopApp.currentUser);
                
        //execute prepared statement
        ps.execute();
        
        //affected rows
        rowsAffected();
                
    } 
    
    public static boolean checkCountrySQL(Connection conn, String country) {
        
        //SQL select statment to check for existing address
        String selectCountries = "SELECT * FROM country WHERE country=?";
        boolean flag = false;

        try{
            //create prepared statement object
            setPreparedStatement(conn, selectCountries);
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();
            
            ps.setString(1, country);
            //execute prepared statements
            ps.execute();      
            //create result set
            ResultSet rs = ps.getResultSet();
            
            if(rs.next()) {
                
                CountrySQL.countryId = rs.getInt("countryId");                
                flag = true;               
            }
            else{
                
                CountrySQL.countryId = maxCountryId(conn);
            }
            
            
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
        return flag;
    }

    public static int maxCountryId(Connection conn) {
        
        
        int countryMaxId = 0;
        
        String maxSQL = "SELECT MAX(countryId) FROM city";
        
        try {
            
            setPreparedStatement(conn, maxSQL);
            PreparedStatement ps = getPreparedStatement();
            
            //execute prepared statements
            ps.execute();
        
            ResultSet rs = ps.getResultSet();            
            //create prepared statement object
            if(rs.next()) {
                
                countryMaxId = rs.getInt(1);
            }                                    
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
            
        }
        
        return countryMaxId + 1;        
    }       
    
}
