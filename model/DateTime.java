/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DAO.AppointmentsSQL;
import DAO.DatabaseConnection;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

/**
 *
 * @author laron
 */
public class DateTime {
    
    //lists for determining date ranges
    private static final ObservableList<String> months31 = 
            FXCollections.observableArrayList("1", "3", "5", "7", "8", "10", "12");
    private static final ObservableList<String> months30 = 
            FXCollections.observableArrayList("4", "6", "9", "11");     
   
    //timestamps to find first and last day for calendar week
    private static Timestamp firstDay; // "yyyy-MM-dd kk:mm:ss.S" format
    private static Timestamp lastDay; // "yyyy-MM-dd kk:mm:ss.S" format
            
    
    //get current date and time based on default system timezone
    public static Timestamp currentDateTime() {
        
	//Getting the LocalDateTime Objects from String values
	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
        String timeStamp = testTimestamp();
        String newTimeStamp = timeStamp.substring(0, 19);

	LocalDateTime ldtStart = LocalDateTime.parse(newTimeStamp, df);
		
	//Convert to a ZonedDate Time in UTC
	ZoneId zid = ZoneId.systemDefault();
        
        //determine date time by time zone
	ZonedDateTime zdtStart = ldtStart.atZone(zid);
        
	ZonedDateTime utcStart = zdtStart.withZoneSameInstant(zid);        
		
	ldtStart = utcStart.toLocalDateTime();
		
	//Create Timestamp values to insert into the database
	Timestamp startsqlts = Timestamp.valueOf(ldtStart); 
		
	return startsqlts;
        
    }
    
    //convert string to timestamp to save to DB with UTC timezone
    public static Timestamp toTimestamp(String textTimestamp) {
        
        //set format for timestamp value
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss.S");
        
        //convert timestamp to local date time
        LocalDateTime ldtStart = LocalDateTime.parse(textTimestamp, df);     
        
        ZoneId zid = ZoneId.systemDefault();
        
        ZonedDateTime zdtStart = ldtStart.atZone(zid);
        
        //determine date time by time zone
        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));

	ldtStart = utcStart.toLocalDateTime();

	//Create Timestamp values from Instants to update database
	Timestamp sqlTimestamp = Timestamp.valueOf(ldtStart); 
        
        return sqlTimestamp;
    }
    
    //convert timestamp in DB from UTC to local timezone
    public static Timestamp toLocalTime(Timestamp timestamp) {
           
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime newZdt = timestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZonedDateTime newLocalTime = newZdt.withZoneSameInstant(zid);
        
	LocalDateTime localTime = newLocalTime.toLocalDateTime();
        
        Timestamp sqlTimestamp = Timestamp.valueOf(localTime);
        return sqlTimestamp;
    }
    
    //create date range
    public static void getMonthRange(Timestamp timeStamp) {
        
        //variables to hold values to operate on
        String month = getMonth(timeStamp);        
        String year = getYear(timeStamp);
        String monthDays = "";
        String start = "";
        String end = "";
       
      
        //28 OR 29 Days = 02-February
        if(month.equals("02")){
                        
            boolean leapYear = getLeapYear(timeStamp);
            if(leapYear==true) {

                monthDays = "29";
                start = year + "-02-01 00:00:00.0";
                end = year + "-02-29 23:59:59.0";
            }
            else {
                
                monthDays = "28";
                start = year + "-02-01 00:00:00.0";
                end = year + "-02-28 23:59:59.0";                
            }            
        }
        
        //30 days 4-April, 6-June, 9-September, 11-November
        else if(months30.contains(month)) {
            
            monthDays="30";
            start = year + "-" + month + "-01 00:00:00.0";            
            end = year + "-" + month + "-30 23:59:59.0";            
        }
            
        //31 days 1-Jan, 3-March, 5-May, 7-July, 8-August, 10-October, 12-December
        else if(months31.contains(month)) {
            
            monthDays = "31";
            start = year + "-" + month + "-01 00:00:00.0";            
            end = year + "-" + month + "-31 23:59:59.0";            
        }        
        else {
            
            JOptionPane.showMessageDialog(null,"Month format incorrect. "
                    + "Please check date formatting.");
        }
        
        Timestamp startDate = toTimestamp(start);        
        Timestamp endDate = toTimestamp(end);          
        Connection conn = DatabaseConnection.estConnection();
        
        switch(monthDays) {
            
            case "28":
                AppointmentsSQL.appointmentsRangeSQL(conn, startDate, endDate);
                break;
            case "29":
                AppointmentsSQL.appointmentsRangeSQL(conn, startDate, endDate);
                break;
            case "30":
                AppointmentsSQL.appointmentsRangeSQL(conn, startDate, endDate);
                break;
            case "31":
                AppointmentsSQL.appointmentsRangeSQL(conn, startDate, endDate);
                break;
            default:
                break;       
        }
    }
    
    //week is sun-sat
    public static void getWeekRange(Timestamp timeStamp) {
        
        Connection conn = DatabaseConnection.estConnection();
        
        //string to find zoned date time
        String timeString = timeStamp.toString();
        
        //format day and return day of week
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss.S");
        LocalDateTime ldtStart = LocalDateTime.parse(timeString, df);
        DayOfWeek dayName = ldtStart.getDayOfWeek();
        ZonedDateTime zonedDateTime = timeStamp.toInstant().atZone(ZoneId.of("UTC"));
        
        switch(dayName) {
            
            case SUNDAY:
                
                calcWeek(conn, zonedDateTime, 0, 6);
                
                break;
                
            case MONDAY:
                
                calcWeek(conn, zonedDateTime, 1, 5);
                
                break;
                
            case TUESDAY:
                
                calcWeek(conn, zonedDateTime, 2, 4);
                
                break;
                
            case WEDNESDAY:
                
                calcWeek(conn, zonedDateTime, 3, 3);

                break;
                
            case THURSDAY:
                
                calcWeek(conn, zonedDateTime, 4, 2); 
                
                break;
                
            case FRIDAY:
                
                calcWeek(conn, zonedDateTime, 5, 1); 

                break;
                
            case SATURDAY:
                
                calcWeek(conn, zonedDateTime, 6, 0); 

                break; 
            
            default:
                break;
                
        } 
    }
    
    //years when the month of february has 29 days
    public static boolean getLeapYear(Timestamp timeStamp) {
        
        String timeString = timeStamp.toString();
        boolean leapYear = false;	 
	String yearString = timeString;        
        int year = Integer.parseInt(yearString);
        
        //determine if leap year
        if((year/4)==0) {
            if((year/100)==0)
                if((year/400)==0)
                    leapYear = true;
        }
        
        return leapYear;
    }
    
    
    public static String getDay(Timestamp timeStamp) {
        
        String timeString = timeStamp.toString();
        String day = timeString.substring(9, 10);
        return day;        
    }
    
    public static String getMonth(Timestamp timeStamp) {
        
        String timeString = timeStamp.toString();
        String month = timeString.substring(5, 7);
        return month;
    }
    
    public static String getYear(Timestamp timeStamp) {
        
        String timeString = timeStamp.toString();
        String year = timeString.substring(0, 4);
        return year;
    }
    
    public static String getYearMonthDay (Timestamp timeStamp) {
        
        String timeString = timeStamp.toString();
        String monthAndYear = timeString.substring(0, 10);
        return monthAndYear;
    }
    
    public static String testTimestamp() {
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String timeStamp = timestamp.toString();        
        return timeStamp;
    }
    
    public static void calcWeek(Connection conn, ZonedDateTime zonedDateTime, int minusDays, int plusDays) {

                firstDay = Timestamp.from
                (zonedDateTime.minus(minusDays, ChronoUnit.DAYS).toInstant());
                String YMD = getYearMonthDay(firstDay);
                firstDay = toTimestamp(YMD + " 00:00:00.0");
                
                lastDay = Timestamp.from
                (zonedDateTime.plus(plusDays, ChronoUnit.DAYS).toInstant());
                String YMD1 = getYearMonthDay(lastDay);
                lastDay = toTimestamp(YMD1 + " 23:59:59.9");                
                
                AppointmentsSQL.appointmentsRangeSQL(conn, firstDay, lastDay);
                
    }
    
}

