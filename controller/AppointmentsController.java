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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Appointments;
import model.DateTime;

/**
 * FXML Controller class
 *
 * @author laron
 */
public class AppointmentsController implements Initializable {

    //containers
    Stage stage;
    Parent scene;
    
    //@FXML Annotations
    //Buttons
    @FXML
    private Button back;
    @FXML
    private Button exit;
    //search field and button
    @FXML
    private TextField searchTxt;    
    @FXML
    private Button searchBttn;

    //choice box
    @FXML
    private ChoiceBox<String> actionChoices;
    private final ObservableList<String> availableChoices = 
            FXCollections.observableArrayList("Add", "Update", "Delete");     
    //Table and columns
    @FXML
    private TableView<Appointments> appointmentsTableView;
    @FXML
    private TableColumn<Appointments, Integer> apptIdCol;
    @FXML
    private TableColumn<Appointments, Integer> cmIdCol;
    @FXML
    private TableColumn<Appointments, String> titleCol;
    @FXML
    private TableColumn<Appointments, String> descriptionCol;
    @FXML
    private TableColumn<Appointments, String> locationCol;
    @FXML
    private TableColumn<Appointments, String> contactCol;
    @FXML
    private TableColumn<Appointments, String> typeCol;
    @FXML
    private TableColumn<Appointments, String> urlCol;
    @FXML
    private TableColumn<Appointments, Timestamp> startCol;
    @FXML
    private TableColumn<Appointments, Timestamp> endCol;
    //fields to get values for Add, update, delete
    @FXML
    private TextField titleTxt;
    @FXML
    private TextField descriptionTxt;
    @FXML
    private TextField locationTxt;
    @FXML
    private TextField contactTxt;
    @FXML
    private TextField typeTxt;
    @FXML
    private TextField urlTxt;    
    @FXML
    private TextField startTxt;
    @FXML
    private TextField endTxt;
    
    //buttons
    @FXML
    private Button submitBttn;
    @FXML
    private Button clearBttn;
    //button to refresh table contents...work in progress
    @FXML
    private Button refreshBttn;
    
    //variables to store cell values
    public static int apptIdSelected;
    public static int customerIdSelected;
    public static int addApptCustomerId;
    int userIdSelected;
    String titleSelected;
    String descriptionSelected;
    String locationSelected;
    String contactSelected;
    String typeSelected;
    String urlSelected;
    Timestamp startSelected;
    Timestamp endSelected;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //stores data in table view
        populateAppointments();

        //set choice available in choice box
        actionChoices.setItems(availableChoices);

        //lambda to return selected row
        appointmentsTableView.setOnMousePressed((MouseEvent event) -> {
            apptIdSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getAppointmentId();
            customerIdSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getCustomerId();
            userIdSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getUserId();               
            titleSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getTitle();
            descriptionSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getDescription();            
            locationSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getLocation();
            contactSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getContact();
            typeSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getType();
            urlSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getUrl();
            startSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getStart();
            endSelected = appointmentsTableView.getSelectionModel().
                    getSelectedItem().getEnd();
            
            //set text fields for user selection
            setTxtFields();        
        });
    }    

    private void clearTxtFields(){
        
        titleTxt.clear();
        descriptionTxt.clear();        
        locationTxt.clear();        
        contactTxt.clear();       
        typeTxt.clear();
        urlTxt.clear();
        startTxt.clear();
        endTxt.clear();         
    }    
    
    private void setTxtFields() {
        
        titleTxt.setText(titleSelected);
        descriptionTxt.setText(descriptionSelected);
        locationTxt.setText(locationSelected);
        contactTxt.setText(contactSelected);
        typeTxt.setText(typeSelected);
        urlTxt.setText(urlSelected);
        startTxt.setText(startSelected.toString());
        endTxt.setText(endSelected.toString());  
    }
    
    @FXML
    private void clickBackButton(ActionEvent event) throws IOException {
        
        appointmentsTableView.getItems().clear();
        DatabaseConnection.dbCloseConnection();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();          
        
    }

    @FXML
    private void clickSubmitButton(ActionEvent event) {
        
        //get values and assign to variables        
        String title = titleTxt.getText();
        String description = descriptionTxt.getText();
        String location = locationTxt.getText();
        String contact = contactTxt.getText();
        String type = typeTxt.getText();
        String url = urlTxt.getText();
        Timestamp start = DateTime.toTimestamp(startTxt.getText());
        Timestamp end = DateTime.toTimestamp(endTxt.getText());          
        
        Connection conn = DatabaseConnection.estConnection();
        
        String choice = actionChoices.getValue();
        
        try{
                        
            switch(choice) {
                case "Add":
                    
                    FXMLLoader Loader = new FXMLLoader();
                    Loader.setLocation(getClass().getResource("/view/ApptAlertBox.fxml"));
                    try{
                        Loader.load();
                    }
                    catch(IOException e){

                        Logger.getLogger(AppointmentsController.class.getName())
                                .log(Level.SEVERE, null, e);
                    }
                    ApptAlertController apptAlertController = Loader.getController();
                    Parent p = Loader.getRoot();
                    Stage alertstage = new Stage();
                    alertstage.setScene(new Scene(p));
                    alertstage.showAndWait();
                                                 
                    //SQL Statement
                    AppointmentsSQL.insertAppointmentsSQL(conn, title, 
                            description, location, contact, type, url, start, 
                            end);     
                    
                    //clear text fields
                    clearTxtFields();
                    break;
                case "Update":
                    AppointmentsSQL.updateAppointmentSQL(conn, title, 
                            description, location, contact, type, url, start, 
                            end);
                    clearTxtFields();
                    break;
                case "Delete":
                    AppointmentsSQL.deleteAppointmentSQL(conn, title, 
                            description, location, contact, type, url, start, 
                            end);
                    clearTxtFields();
                    break;
                default:
                    clearTxtFields();
                    break;
            }            
        }
        catch (SQLException ex) {
            
            Logger.getLogger(CustomerRecordsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            
        }    
    }

    @FXML
    private void clickRefreshButton(ActionEvent event) {
        
        populateAppointments();
    }

    @FXML
    private void clickClearButton(ActionEvent event) {
        
        clearTxtFields();
    }
    
    @FXML
    private void clickSearchButton(ActionEvent event) throws SQLException {
        
        //work in progress
    }    

    @FXML
    private void clickExitButton(ActionEvent event) {

        JOptionPane.showMessageDialog(null,"Exiting Application");
        DatabaseConnection.dbCloseConnection();
        System.exit(0);        
    }
    
    private void populateAppointments() {
        
        //clear table data
        appointmentsTableView.getItems().clear();
        
        //connect to db
        Connection conn = DatabaseConnection.estConnection();
        
        //query db and return appointment data to store in table        
        try {
            
            AppointmentsSQL.selectAppointmentsSQL(conn);
        } catch (SQLException e) {
            Logger.getLogger(CustomerRecordsController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
                               
        //store appointment objects in table view
        appointmentsTableView.setItems(Appointments.getAllAppointments());        
        
        //populate items in cells
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        cmIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));        
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        
    }
    
}
