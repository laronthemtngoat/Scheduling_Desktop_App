/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import controller.CustomerRecordsController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.CustomerRecords;
import model.DateTime;
import scheduling_desktop_ui.SchedulingDesktopApp;

/**
 *
 * @author laron
 */
public class CustomerSQL {
    
    //variable for SQL update statement
    public static int updateSQLCMId;
    public static int updateSQLAddId;
    //variable for customer ID check
    public static int checkCustomerId;
        
    //statement reference
    private static PreparedStatement statement; 
    
    //create statement object
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        
        statement = conn.prepareStatement(sqlStatement);
        
    }
    
    //return statement object
    public static PreparedStatement getPreparedStatement() {
        
        return statement;
        
    }
    
    //IMPORTANT
    //probably use for reporting requirement
    public static void selectAddressIdSQL(Connection conn, int customerId) {
        
        try {
            
            //SQL statement saved to String variable
            String selectCustomer = "SELECT addressId FROM customer "
                    + "WHERE customerId = ?";
            //create prepared statement object
            setPreparedStatement(conn, selectCustomer);
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();
            //map value to SQL statement
            ps.setInt(1, customerId);
                    
            //execute prepared statement
            ps.execute();
            
            ResultSet rs = ps.getResultSet();
            while(rs.next()) {
                
                updateSQLAddId = rs.getInt("addressId");
            }
                    
        } catch (SQLException ex) {
            Logger.getLogger(CustomerSQL.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
                
    }
    
    //SQL statement to select a customer from the DB
    public static void selectCustomerSQL(Connection conn) throws SQLException {
        
        //SQL statement saved to String variable
        String selectCustomer = "SELECT customer.customerId, customer.customerName,\n" +
        "address.address, address.address2, address.postalCode, address.phone,\n" +
        "city.city,\n" +
        "country.country \n" +
        "FROM U05m11.customer \n" +
        "INNER JOIN U05m11.address ON address.addressId = customer.addressId\n" +
        "INNER JOIN U05m11.city ON city.cityId = address.cityId\n" +
        "INNER JOIN U05m11.country ON country.countryId = city.countryId";
        
        //create prepared statement object
        setPreparedStatement(conn, selectCustomer);
        //get prepared statement reference
        PreparedStatement ps = getPreparedStatement();

        //execute prepared statements
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        while(rs.next()) {
            int cmId = rs.getInt("customerId");
            String cmName = rs.getString("customerName");
            String address = rs.getString("address");
            String address2 = rs.getString("address2");
            String postalCode = rs.getString("postalCode");
            String phone = rs.getString("phone");
            String city = rs.getString("city");
            String country = rs.getString("country");
            
            //call customer constructor
            CustomerRecords customer = 
                    new CustomerRecords(cmId, cmName, address, address2, 
                            postalCode, city, country, phone);
            
            //add customer to observable list
            CustomerRecords.addRecord(customer);             
        }
    }

    //SQL statement to add a customer to the DB
    public static void insertCustomerSQL(Connection conn, String customerName, 
            String address, String address2, String city, String country, 
            String postalCode, String phone) throws SQLException {
        
        String insertCustomerStatement = "INSERT INTO customer "
                + "(customerId, customerName, addressId, active, createDate, "
                + "createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES(?, ?, ?, 1, ?, ?, ?, ?)";
        
        int customerId = maxCustomerId(conn);
        
        //create prepared statement object
        setPreparedStatement(conn, insertCustomerStatement);
        
        //get prepared statement reference
        PreparedStatement ps = getPreparedStatement();        
        
        //check for existing country
        boolean countryMatch = CountrySQL.checkCountrySQL(conn, country);
        if(countryMatch==false) {
            
            try{
                CountrySQL.insertCountrySQL(conn, customerName, address, 
                        address2, city, country, postalCode, phone);             
            
            }
            catch(MySQLIntegrityConstraintViolationException e){
                System.out.println(e.getMessage());
            }            
        }
  
        //check for existing city
        boolean cityMatch = CitySQL.checkCitySQL(conn, city);
        if(cityMatch==false){
            
            try {
                CitySQL.insertCitySQL(conn, customerName, address, 
                        address2, city, country, postalCode, phone);              
            
            }
            catch(MySQLIntegrityConstraintViolationException e){
                System.out.println(e.getMessage());
            }              
        }

        //check for existing address
        boolean addressMatch = AddressSQL.checkAddressSQL(conn, address, 
                address2);
        if(addressMatch==false){
            
            try {
                AddressSQL.insertAddressSQL(conn, customerName, address, 
                        address2, city, country, postalCode, phone);               
            
            }
            catch(MySQLIntegrityConstraintViolationException e){
                System.out.println(e.getMessage());
            }                 
        }
 
        //determine date and time
        Timestamp currentDate = DateTime.currentDateTime();
        
        //key-value mapping
        ps.setInt(1, customerId);
        ps.setString(2, customerName);        
        ps.setInt(3, AddressSQL.addressId);
        ps.setTimestamp(4, currentDate);        
        ps.setString(5, SchedulingDesktopApp.currentUser);
        ps.setTimestamp(6, currentDate);
        ps.setString(7, SchedulingDesktopApp.currentUser);
        
        //execute prepared statement
        ps.execute();
        
        //affected rows
        rowsAffected();    
    }    
    
    //SQL statement to update entry in DB
    public static void updateCustomerSQL(Connection conn, String customerName, 
            String address, String address2, String city, String country, 
            String postalCode, String phone) throws SQLException {
        
        String updateStatement = "UPDATE customer SET customerName = ?, "
                + "lastUpdate =?, lastUpdateBy = ? WHERE customerId = ?";
        
        try {
            
            //create prepared statement object
            setPreparedStatement(conn, updateStatement);

            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();

            // determine date and time
            Timestamp currentDate = DateTime.currentDateTime();        
            
            //lambda to obtain customerId            
            CustomerRecords.getAllCustomers().forEach(CustomerRecords -> {
                String cmName = CustomerRecords.getCustomerName();
                if(CustomerRecordsController.selectedCMName.equals(cmName)
                        | cmName.equals(customerName)) {
                    updateSQLCMId = CustomerRecords.getCustomerId();
                }
            });
            
            //map value to Address Id variable to use in address update SQL
            selectAddressIdSQL(conn, updateSQLCMId);
            
            //key-value mapping
            ps.setString(1, customerName);
            ps.setTimestamp(2, currentDate);
            ps.setString(3, SchedulingDesktopApp.currentUser);
            ps.setInt(4, updateSQLCMId);

            //execute prepared statement
            ps.execute();

            //affected rows
            rowsAffected();             
        }
        catch(SQLException e){
            
            System.out.println(e.getMessage());
        } 
    }
    
    //SQL statement to delete a customer in DB, including their address
    public static void deleteCustomerSQL(Connection conn, String customerName, 
            String address, String address2, String city, String country, 
            String postalCode, String phone) throws SQLException {        
        
        String deleteStatement = "DELETE FROM customer WHERE customerName = ? "
                + "AND customerId = ?";
        try{
            
            //create prepared statement object
            setPreparedStatement(conn, deleteStatement);

            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();                            

            //key-value mapping
            ps.setString(1, customerName);
            ps.setInt(2, CustomerRecordsController.selectedCustomerId);

            //execute prepared statement
            ps.execute();

            //affected rows
            rowsAffected();            
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }   
    }
    
    //confirm rows affected
    public static void rowsAffected() {
     
        try {
            if(statement.getUpdateCount() > 0)
                System.out.println(statement.getUpdateCount() + 
                        " rows affected!");
            else
                System.out.println("No rows affected!");                
        } catch (SQLException ex) {
            Logger.getLogger(CustomerSQL.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    
    }

    public static boolean checkCustomerSQL(Connection conn) {
        
        //SQL select statment to check for existing address
        String selectUsers = "SELECT * FROM user WHERE customerId=?";
        boolean flag = false;
        
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
                
                checkCustomerId = rs.getInt("countryId");                
                flag = true;               
            }
            else{
                
                checkCustomerId = maxCustomerId(conn);
            }
            
            
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
        return flag;
    }
    
    public static void getCustomerIdSQL(Connection conn) {
        
        //SQL select statment to check for existing address
        String selectUsers = "SELECT * FROM customer WHERE customerName=?";
        
        try{
            //create prepared statement object
            setPreparedStatement(conn, selectUsers);
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();
            
            ps.setInt(1, checkCustomerId);
            //execute prepared statements
            ps.execute();      
            //create result set
            ResultSet rs = ps.getResultSet();
            
            if(rs.next()) {
                
                checkCustomerId = rs.getInt("customerId");                
                               
            }
            else {
                
                System.out.println("No customer found.");
            }                        
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
    }
        
    
    private static int maxCustomerId(Connection conn) {
        
        int cmMaxId = 0;
        
        String maxSQL = "SELECT MAX(customerId) FROM customer";
        
        try {
            
            setPreparedStatement(conn, maxSQL);
            PreparedStatement ps = getPreparedStatement();
            
            //execute prepared statements
            ps.execute();
        
            ResultSet rs = ps.getResultSet();            
            //create prepared statement object
            if(rs.next()) {
                
                cmMaxId = rs.getInt(1);
            }                                    
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
            
        }
        
        return cmMaxId + 1;
    }
    
}

 