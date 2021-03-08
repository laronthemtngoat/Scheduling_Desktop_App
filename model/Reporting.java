/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DAO.AppointmentsSQL;
import DAO.CountrySQL;
import DAO.DatabaseConnection;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author laron
 */
public class Reporting {
    
    public Reporting() {
    }
    
    private final ObservableList<String> typesList = 
            FXCollections.observableArrayList();
    private final ObservableList<String> countries = 
            FXCollections.observableArrayList();
    private final ObservableList<String> consultantSchedule = 
            FXCollections.observableArrayList();    
    
    String typeTest = "";
    Timestamp timeStamp;
    String month;
    int countryId = 0;
    String countryName = "";
    
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    public Map monthlyApptTypesCount() throws SQLException {
        
        Connection conn = DatabaseConnection.estConnection();
        
        Map map = null;
                
        
        try{
            
            AppointmentsSQL.selectAppointmentsSQL(conn);
            Appointments.getAllAppointments().forEach(object -> {

                timeStamp = object.getStart();
                month = DateTime.getMonth(timeStamp);
                int monthConvert = parseInt(month);
                String monthName = getMonth(monthConvert);                
                typeTest = object.getType();                                                
                typesList.add(typeTest + ": " + monthName);                                
            });
          
            map = countFrequencies(typesList);
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        DatabaseConnection.dbCloseConnection();
        return map;
    }
    
    //count number of times a type occurs in a list
    public static Map countFrequencies(ObservableList list) {
        
        Map<String, Long> counts = (Map<String, Long>) list.stream().
                collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        return counts;
    }    
    
    //custom report -> returns all countries
    public ObservableList countriesReport() throws SQLException {
        
        Connection conn = DatabaseConnection.estConnection();
        
        
        try{
            
            CountrySQL.selectCountriesSQL(conn);
            Country.getAllAppointments().forEach(object -> {
                
                countryId = object.getCountryId();
                countryName = object.getCountry();
                String reportLine = countryId + ", "  + countryName + " | ";
                countries.add(reportLine);
            });
           
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        DatabaseConnection.dbCloseConnection();
        return countries;
    }    
}
