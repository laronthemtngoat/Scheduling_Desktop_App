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
public class AddressSQL {
    
    //variables to use in SQL statements 
    public static int addressId = 0;
    
    //SQL statement to add to DB
    public static void insertAddressSQL(Connection conn, String customerName, 
            String address, String address2, String city, String country, 
            String postalCode, String phone) throws SQLException {
        
        String insertCustomerStatement = "INSERT INTO address "
                + "(addressId, address, address2, cityId, postalCode, phone,"
                + " createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        //create prepared statement object
        setPreparedStatement(conn, insertCustomerStatement);
        
        //get prepared statement reference
        PreparedStatement ps = getPreparedStatement();                
        
        //determine date and time
        Timestamp currentDate = DateTime.currentDateTime();
        
        //key-value mapping
        ps.setInt(1, addressId);
        ps.setString(2, address);        
        ps.setString(3, address2);
        ps.setInt(4, CitySQL.cityId);        
        ps.setString(5, postalCode);
        ps.setString(6, phone);
        ps.setTimestamp(7, currentDate);
        ps.setString(8, SchedulingDesktopApp.currentUser);
        ps.setTimestamp(9, currentDate);
        ps.setString(10, SchedulingDesktopApp.currentUser);
                
        //execute prepared statement
        ps.execute();
        
        //affected rows
        rowsAffected();
                
    }

    //SQL statement to update entry in DB
    public static void updateAddressSQL(Connection conn, String customerName, 
            String address, String address2, String city, String country, 
            String postalCode, String phone) throws SQLException {
        
        String updateStatement = "UPDATE address SET address = ?, address2 = ?, "
                + "postalCode = ?, phone = ?, lastUpdate =?, lastUpdateBy = ? "
                + "WHERE addressId = ?";        
        
        //create prepared statement object
        setPreparedStatement(conn, updateStatement);
        
        //get prepared statement reference
        PreparedStatement ps = getPreparedStatement();
        
        // determine date and time
        Timestamp currentDate = DateTime.currentDateTime();        
            
        //key-value mapping
        ps.setString(1, address);
        ps.setString(2, address2);
        ps.setString(3, postalCode);
        ps.setString(4, phone);        
        ps.setTimestamp(5, currentDate);
        ps.setString(6, SchedulingDesktopApp.currentUser);
        ps.setInt(7, CustomerSQL.updateSQLAddId);        
        
        //execute prepared statement
        ps.execute();
        
        //affected rows
        rowsAffected();

        
    }    
    
    //SQL statement to delete a customer in DB, including their address
    public static void deleteAddressSQL(Connection conn, String customerName, 
            String address, String address2, String city, String country, 
            String postalCode, String phone) throws SQLException {        
        
        String deleteStatement = "DELETE FROM address WHERE address = ? AND "
                + "address2 = ?";
        
        //create prepared statement object
        setPreparedStatement(conn, deleteStatement);
        
        //get prepared statement reference
        PreparedStatement ps = getPreparedStatement();                            
        
        //key-value mapping
        ps.setString(1, address);
        ps.setString(2, address2);
        
        //execute prepared statement
        ps.execute();

        //affected rows
        rowsAffected();                
    }    
    
    //method to check if addressId already exists
    public static boolean checkAddressSQL(Connection conn, String address, String address2) {
        
        //SQL select statment to check for existing address
        String selectAllAddress = "SELECT * FROM address WHERE address=? AND address2=?";
        boolean flag = false;
        try{
            //create prepared statement object
            setPreparedStatement(conn, selectAllAddress);
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();
            
            ps.setString(1, address);
            ps.setString(2, address2);
            //execute prepared statements
            ps.execute();      
            //create result set
            ResultSet rs = ps.getResultSet();
            
            if(rs.next()) {
                
                addressId = rs.getInt("addressId");
                flag = true;
                                
            }
            else{
                
                addressId = maxAddressId(conn);
            }
            
            
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
        
        return flag;
    }
    
    public static int maxAddressId(Connection conn) {
        
        
        int addressMaxId = 0;
        
        String maxSQL = "SELECT MAX(addressId) FROM address";
        
        try {
            
            setPreparedStatement(conn, maxSQL);
            PreparedStatement ps = getPreparedStatement();
            
            //execute prepared statements
            ps.execute();
        
            ResultSet rs = ps.getResultSet();            
            //create prepared statement object
            if(rs.next()) {
                
                addressMaxId = rs.getInt(1);
            }                                    
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
            
        }
        
        return addressMaxId + 1;        
    }
    
}

/*
    //SQL statement to select a customer from the DB
    public static void selectCustomerSQL(Connection conn) throws SQLException {
                
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
*/