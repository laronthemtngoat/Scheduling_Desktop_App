/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.AppointmentsSQL;
import DAO.DatabaseConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author laron
 */
public class MainMenuController implements Initializable {

    //containers
    Stage stage;
    Parent scene;

    //boolean variable to disable appt reminder after first initialization
    public static boolean apptReminder = true;
    
    //FXML annotations
    @FXML
    private Button customerRecords;
    @FXML
    private Button appointments;
    @FXML
    private Button calendar;
    @FXML
    private Button reportingBttn;    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        if(apptReminder == true) {
            
            Connection conn = DatabaseConnection.estConnection();
            boolean apptSoon = AppointmentsSQL.appointmentAlert(conn);
            if(apptSoon == true) {
                JOptionPane.showMessageDialog(null, "Appointment within "
                + "15 minutes. Please check your appointments."); 
                apptReminder = false;
            }
            DatabaseConnection.dbCloseConnection();  
        }        
    }    

    @FXML
    private void clickCustomerRecordsButton(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/CustomerRecords.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();          
        
    }
    
    @FXML
    private void clickAppointmentsButton(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Appointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();          
        
    }

    @FXML
    private void clickCalendarButton(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Calendar.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();          
        
    }    

    @FXML
    private void clickReporintgButton(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Reporting.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();         
    }
    
}
