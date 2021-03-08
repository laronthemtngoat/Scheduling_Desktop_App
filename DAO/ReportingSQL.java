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
import java.sql.Timestamp;
import model.Appointments;
import model.Country;
import scheduling_desktop_ui.SchedulingDesktopApp;

/**
 *
 * @author laron
 */
public class ReportingSQL {
    
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
    
    public static void selectApptTypes(Connection conn) throws SQLException {
        
        //SQL statement
        String selectStatement = "SELECT * FROM appointment ORDERBY type";
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
    
    public static void selectConsultantAppointments(Connection conn) throws 
            SQLException {
        
        //SQL statement saved to String variable
        String selectAppointments = "SELECT * FROM appointment where userId = ?";
        
        try{
            //create prepared statement object
            setPreparedStatement(conn, selectAppointments);
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();
            
            //set userId
            ps.setInt(1, SchedulingDesktopApp.userId);
            
            //execute prepared statements
            ps.execute();
            
            //store results of query in appointment objects
            ResultSet rs = ps.getResultSet(); 
            
            //assign data to variables
            while(rs.next()) {
                                
            int appointmentId = rs.getInt("appointmentId");
            int customerId = rs.getInt("customerId");
            int userId = rs.getInt("userId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String type = rs.getString("type");
            String url = rs.getString("url");
            Timestamp start = rs.getTimestamp("start");
            Timestamp end = rs.getTimestamp("end");
            Timestamp createDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            
            //call appointment constructor to create new appointment object
            Appointments appointment = 
                    new Appointments(appointmentId, customerId, userId, title, 
                            description, location, contact, type, url, start, 
                            end, createDate, createdBy, lastUpdate, 
                            lastUpdateBy);
            
            //add appointment to observable list
            Appointments.addConsultantAppointment(appointment);  
            }            
        }
        catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
        
    }
}

/*
    //SQL statement to select appointments from the DB
    public static void selectAppointmentsSQL(Connection conn) throws SQLException {
        
        //SQL statement saved to String variable
        String selectAppointments = "SELECT * FROM appointment";       
        
        //create prepared statement object
        setPreparedStatement(conn, selectAppointments);
        //get prepared statement reference
        PreparedStatement ps = getPreparedStatement();

        //execute prepared statements
        ps.execute();
        
        //store results of query in appointment objects
        ResultSet rs = ps.getResultSet();
        while(rs.next()) {
            
            int appointmentId = rs.getInt("appointmentId");
            int customerId = rs.getInt("customerId");
            int userId = rs.getInt("userId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String type = rs.getString("type");
            String url = rs.getString("url");
            Timestamp start = rs.getTimestamp("start");
            Timestamp end = rs.getTimestamp("end");
            Timestamp createDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            
            //call appointment constructor to create new appointment object
            Appointments appointment = 
                    new Appointments(appointmentId, customerId, userId, title, 
                            description, location, contact, type, url, start, 
                            end, createDate, createdBy, lastUpdate, 
                            lastUpdateBy);
            
            //add appointment to observable list
            Appointments.addAppointment(appointment);    
        }
    }
*/