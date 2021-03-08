/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import controller.AppointmentsController;
import controller.ApptAlertController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Appointments;
import model.Calendar;
import model.DateTime;
import scheduling_desktop_ui.SchedulingDesktopApp;

/**
 *
 * @author laron
 */
public class AppointmentsSQL {
    
    //statement reference
    private static PreparedStatement statement; 
    
    //create statement object
    public static void setPreparedStatement(Connection conn, String sqlStatement) 
            throws SQLException {
        
        statement = conn.prepareStatement(sqlStatement);
        
    }
    
    //return statement object
    public static PreparedStatement getPreparedStatement() {
        
        return statement;
        
    }

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
            
            //convert database timestamp to local timezone
            
            Timestamp start = DateTime.toLocalTime(rs.getTimestamp("start"));
            System.out.println(rs.getTimestamp("start"));
            //Timestamp start = rs.getTimestamp("start");
            Timestamp end = DateTime.toLocalTime(rs.getTimestamp("end"));
            //Timestamp end = rs.getTimestamp("end");        
            System.out.println(rs.getTimestamp("end"));
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
    
    //SQL statement to insert appointments into the DB
    public static void insertAppointmentsSQL(Connection conn, String title, 
            String description, String location, String contact, String type, 
            String url, Timestamp start, Timestamp end) throws SQLException {
        
        try{
            //SQL statement saved to String variable
            String insertAppointmentStatement = "INSERT INTO appointment "
                + "(appointmentId, customerId, userId, title, description, "
                + "location, contact, type, url, start, end, createDate, "
                + "createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            //outside business hours exception
            if(businessHrsException(start, end) == true) {
                
                JOptionPane.showMessageDialog(null,"Appointment times outside "
                        + "business hours of 08:00:00.0 to 16:00:00.0. "
                        + "Please enter timestamps within business hours and "
                        + "resubmit.");
            }
            
            //overlapping appointments exception
            else if (overLappingAppts(start) == true) {
                
                JOptionPane.showMessageDialog(null, "Overlapping appointments "
                        + "times. Please check times and resubmit.");
            }
            else {

                // determine date and time
                Timestamp currentDate = DateTime.currentDateTime();                     

                //create prepared statement object
                setPreparedStatement(conn, insertAppointmentStatement);
                //get prepared statement reference
                PreparedStatement ps = getPreparedStatement();

                //get current user's userId            
                UserSQL.getUserIdSQL(conn);            

                //determine appointmentId
                int appointmentId = maxAppointmentId(conn);

                //key-value mapping
                ps.setInt(1, appointmentId);
                ps.setInt(2, ApptAlertController.selectedCustomerId);        
                ps.setInt(3, UserSQL.userId);
                ps.setString(4, title);        
                ps.setString(5, description);
                ps.setString(6, location);
                ps.setString(7, contact);
                ps.setString(8, type);
                ps.setString(9, url);
                ps.setTimestamp(10, start);
                ps.setTimestamp(11, end);
                ps.setTimestamp(12, currentDate);
                ps.setString(13,SchedulingDesktopApp.currentUser);
                ps.setTimestamp(14, currentDate);
                ps.setString(15,SchedulingDesktopApp.currentUser);            

                //execute prepared statements
                ps.execute();

                //affected rows
                rowsAffected();                
            }

            
        }
        catch(SQLException e) {
            
            System.out.println(e.getMessage());
        } 
    }

    public static void updateAppointmentSQL(Connection conn, String title, 
            String description, String location, String contact, String type, 
            String url, Timestamp start, Timestamp end) {
        
        try{
            
            //SQL statement to update entry
            String updateStatement = "UPDATE appointment SET title = ?, "
                    + "description = ?, location = ?, contact = ?, type = ?, "
                    + "url = ?, start = ?, end = ?, lastUpdate =?, "
                    + "lastUpdateBy = ? WHERE appointmentId = ?";
                        //create prepared statement object
            setPreparedStatement(conn, updateStatement);
            
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();

            // determine date and time
            Timestamp currentDate = DateTime.currentDateTime();
            
            //key-value mapping
            ps.setString(1, title);
            ps.setString(2,description);
            ps.setString(3, location);
            ps.setString(4, contact);
            ps.setString(5, type);
            ps.setString(6, url);
            ps.setTimestamp(7, start);
            ps.setTimestamp(8, end);
            ps.setTimestamp(9, currentDate);
            ps.setString(10, SchedulingDesktopApp.currentUser);
            ps.setInt(11, AppointmentsController.apptIdSelected);

            //execute prepared statement
            ps.execute();

            //affected rows
            rowsAffected();  
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteAppointmentSQL(Connection conn, String title, 
            String description, String location, String contact, String type, 
            String url, Timestamp start, Timestamp end) {
        try{
            
            String deleteStatement = "DELETE FROM appointment WHERE "
                    + "appointmentId = ?";
                    
            setPreparedStatement(conn, deleteStatement);
            
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();
            
            //set appointmentId value
            ps.setInt(1, AppointmentsController.apptIdSelected);
            
            ps.execute();
            
            rowsAffected();
            
        }
        catch(SQLException e){
            
            System.out.println(e.getMessage());
        }
    }
    
    private static int maxAppointmentId(Connection conn) {
        
        int appointmentMaxId = 0;
        
        String maxSQL = "SELECT MAX(appointmentId) FROM appointment";
        
        try {
            
            //create prepared statement object
            setPreparedStatement(conn, maxSQL);
            PreparedStatement ps = getPreparedStatement();
            
            //execute prepared statements
            ps.execute();
            
            //create result set object
            ResultSet rs = ps.getResultSet();            
            
            //save returned value to variable
            if(rs.next()) {
                
                appointmentMaxId = rs.getInt(1);
            }                                    
        }catch(SQLException e) {
            
            System.out.println(e.getMessage());
            
        }
        
        return appointmentMaxId + 1;
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

    public static void appointmentsRangeSQL(Connection conn, Timestamp startDate, Timestamp EndDate) {
        
        String rangeSQL = "SELECT * FROM appointment WHERE start "
                + "BETWEEN ? AND ?";
        try{
            
            //getter and setter
            setPreparedStatement(conn, rangeSQL);
            PreparedStatement ps = getPreparedStatement();
            
            ps.setTimestamp(1, startDate);
            ps.setTimestamp(2, EndDate);
            //execute prepared statements
            ps.execute();
                    
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

                //call appointment constructor to create new appointment object
                Calendar calendar = 
                        new Calendar(appointmentId, customerId, userId, title, 
                                description, location, contact, type, url, start, 
                                end);

                //add appointment to observable list
                Calendar.addAppointment(calendar);                  
            }   
        }
        catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
    }
    
    //returns appointments with userId
    
    
    //generates message for appointment within 15 minutes of login time
    public static boolean appointmentAlert(Connection conn) {
        
        //flag to return 
        boolean flag = false;
        int sec = 900;
        //SQL statement saved to String variable
        String selectAppointments = "SELECT * FROM appointment WHERE userId = ?";
        
        // determine current date and time
        Timestamp currentDate = DateTime.currentDateTime();
        
        try{

            //create prepared statement object
            setPreparedStatement(conn, selectAppointments);
            //get prepared statement reference
            PreparedStatement ps = getPreparedStatement();
            
            //set value in sql statement
            ps.setInt(1, SchedulingDesktopApp.userId);
            //execute prepared statements
            ps.execute();

            //store results of query in appointment objects
            ResultSet rs = ps.getResultSet();
            while(rs.next()) {
                
                //obtain appointment start time
                Timestamp start = rs.getTimestamp("start");
                
                int diffmin = (int)compareTwoTimeStamps(currentDate, start);
                
                //changes flag if appointment is within 15 minutes
                if((diffmin >= 1) && (diffmin <= 15)) {
                    
                    flag = true;
                }
                
            }             
        }
        catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
        
     return flag;       
    }
    
    public static long compareTwoTimeStamps(Timestamp currentTime, Timestamp apptStart)
    {
        long milliseconds1 = apptStart.getTime();
        long milliseconds2 = currentTime.getTime();
        

        long diff = milliseconds1 - milliseconds2;
        long diffMinutes = (diff / (1000 * 60));
        
        return diffMinutes;
    }
    
    /*business hours are from 08:00:00.0 to 16:00:00.0 (8 AM to 8 PM)
    exception verifies start and end time do not occur before or after 
    business hours
    */
    public static boolean businessHrsException(Timestamp newApptStart, 
            Timestamp newApptEnd) {
        
        //timestamp format = "yyyy-MM-dd kk:mm:ss.S"            
        String currentYMD = DateTime.getYearMonthDay(newApptStart);
        
        boolean outsideBusinessH = false;
        
        Timestamp busStart = DateTime.toTimestamp(currentYMD + " 08:00:00.0");
        Timestamp busEnd = DateTime.toTimestamp(currentYMD + " 16:00:00.0");
        
        if(newApptStart.before(busStart) | newApptStart.after(busEnd) 
                | newApptEnd.before(busStart) | newApptEnd.after(busEnd)) {
            outsideBusinessH = true;
        }
        return outsideBusinessH;
    }
    
    private static Timestamp existAppStart;
    private static Timestamp existApptEnd;
    private static boolean overlapping = false;
    
    //verifies appointments do not overlap.
    private static boolean overLappingAppts(Timestamp newApptStart) {

        Appointments.getAllAppointments().forEach(Appointments -> {
            existAppStart = Appointments.getStart();
            existApptEnd = Appointments.getEnd();
            if(newApptStart.before(existApptEnd) && newApptStart.
                    after(existAppStart)) {
                
                overlapping = true;
            }
            else if (newApptStart.equals(existAppStart)) {
                
                overlapping = true;
            }
        });
        return overlapping;
    }
}
