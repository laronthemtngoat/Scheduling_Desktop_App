/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.DatabaseConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Calendar;
import model.DateTime;

/**
 * FXML Controller class
 *
 * @author laron
 */
public class CalendarController implements Initializable {

    //containers
    Stage stage;
    Parent scene;    
    
    @FXML
    private Button back;
    @FXML
    private Button exit;
    
    //Radio buttons to select week or month view
    @FXML
    private ToggleGroup calendarView;    
    @FXML
    private RadioButton monthTggl;
    @FXML
    private RadioButton weekTggl;
    
    @FXML
    private TableView<Calendar> calendarTableView;    
    @FXML
    private TableColumn<Calendar, Timestamp> startTimeCol;
    @FXML
    private TableColumn<Calendar, Timestamp> endTimeCol;
    @FXML
    private TableColumn<Calendar, Integer> apptIdCol;
    @FXML
    private TableColumn<Calendar, String> typeCol;
    @FXML
    private TableColumn<Calendar, Integer> customerIdCol;
    @FXML
    private TableColumn<Calendar, String> contactCol;
    @FXML
    private TableColumn<Calendar, String> descriptionCol;
    @FXML
    private Button prvsBttn;
    @FXML
    private Button nxtBttn;

    Timestamp timeStampPrev;
    Timestamp timeStampNxt;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        //connect to db        
        Timestamp currentDate = DateTime.currentDateTime();
        DateTime.getMonthRange(currentDate);
        
        //store items in table view
        calendarTableView.setItems(Calendar.getAppointmentsRange());
        
        //populate items in cells
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
      
    }    

    @FXML
    private void selectMonth(ActionEvent event) {
        
        if(monthTggl.isSelected()) {
            
            calendarTableView.getItems().clear();
            
            //connect to db        
            Timestamp currentDate = DateTime.currentDateTime();
            DateTime.getMonthRange(currentDate);            
            
            //store items in table view
            calendarTableView.setItems(Calendar.getAppointmentsRange());

            //populate items in cells
            startTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            endTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));            
        }
        DatabaseConnection.dbCloseConnection();
    }

    @FXML
    private void selectWeek(ActionEvent event) {
        
        if(weekTggl.isSelected()) {

            calendarTableView.getItems().clear();
            
            //connect to db        
            Timestamp currentDate = DateTime.currentDateTime();
            DateTime.getWeekRange(currentDate);            
            
            //store items in table view
            calendarTableView.setItems(Calendar.getAppointmentsRange());

            //populate items in cells
            startTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            endTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));            
        }
        DatabaseConnection.dbCloseConnection();
    }

    @FXML
    private void clickPreviousButton(ActionEvent event) {
        //work in progress
    }

    @FXML
    private void clickNextButton(ActionEvent event) {
        //work in progress
    }
    
    @FXML
    private void clickBackButton(ActionEvent event) throws IOException {
        
        calendarTableView.getItems().clear();
        DatabaseConnection.dbCloseConnection();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();            
        
    }
    
    @FXML
    private void clickExitButton(ActionEvent event) {
        
        JOptionPane.showMessageDialog(null,"Exiting Application");
        DatabaseConnection.dbCloseConnection();
        System.exit(0);        
    } 
}
