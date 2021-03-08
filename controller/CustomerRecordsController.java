/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.AddressSQL;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import DAO.DatabaseConnection;
import model.CustomerRecords;
import DAO.CustomerSQL;
import javafx.scene.input.MouseEvent;


/**
 * FXML Controller class
 *
 * @author laron
 */
public class CustomerRecordsController implements Initializable {

    //containers
    Stage stage;
    Parent scene;
    
    //@FXML Annotations
    //action buttons to move back to main menu or exit program
    @FXML
    private Button back;
    @FXML
    private Button exit;    

    
    //add, update, delete choices
    @FXML
    private ChoiceBox<String> actionChoices;
    private final ObservableList<String> availableChoices = 
            FXCollections.observableArrayList("Add", "Update", "Delete");        
    @FXML
    private TextField cmNameTxt;
    @FXML
    private TextField cmAddressTxt;
    @FXML
    private TextField cmCityTxt;
    @FXML
    private TextField cmCountryTxt;
    @FXML
    private TextField cmAddressTxt2;
    @FXML
    private TextField cmPostalCodeTxt;    
    @FXML
    private TextField cmPhoneNumberTxt;
    @FXML
    private Button clearBttn;
    @FXML
    private Button submitBttn;
    
    //customer records table
    @FXML
    private TableView<CustomerRecords> customerTableView;
    @FXML
    private TableColumn<CustomerRecords, Integer> cmIdCol;
    @FXML
    private TableColumn<CustomerRecords, String> cmNameCol;
    @FXML
    private TableColumn<CustomerRecords, String> addressCol;
    @FXML
    private TableColumn<CustomerRecords, String> address2Col;
    @FXML
    private TableColumn<CustomerRecords, String> cityCol;
    @FXML
    private TableColumn<CustomerRecords, String> countryCol;
    @FXML
    private TableColumn<CustomerRecords, String> postalCol;    
    @FXML
    private TableColumn<CustomerRecords, String> phoneCol;    
    
    //search function
    @FXML
    private TextField searchTxt;
    @FXML
    private Button searchBttn;
    
    //refresh button...work in progress
    @FXML
    private Button refreshButton;
    
    //global variables to store selected rows
    public static int selectedCustomerId;
    public static String selectedCMName;
    String selectedAddress;
    String selectedAddress2;
    String selectedCity;
    String selectedCountry;
    String selectedPostalCode;
    String selectedPhone;
    CustomerRecords selectedCustomer;

    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        populateCustomerRecords();
        
        //set choice available in choice box
        actionChoices.setItems(availableChoices);        

        /*lambda to return selected row -> populates text fields
        when user selects a row in the table view
        */
        customerTableView.setOnMousePressed((MouseEvent event) -> {
            selectedCustomerId = customerTableView.getSelectionModel().
                    getSelectedItem().getCustomerId();
            selectedCMName= customerTableView.getSelectionModel().
                    getSelectedItem().getCustomerName();            
            selectedAddress = customerTableView.getSelectionModel().
                    getSelectedItem().getAddress();
            selectedAddress2 = customerTableView.getSelectionModel().
                    getSelectedItem().getAddress2();
            selectedCity = customerTableView.getSelectionModel().
                    getSelectedItem().getCity();
            selectedCountry = customerTableView.getSelectionModel().
                    getSelectedItem().getCountry();
            selectedPostalCode = customerTableView.getSelectionModel().
                    getSelectedItem().getPostalCode();
            selectedPhone = customerTableView.getSelectionModel().
                    getSelectedItem().getPhone();
            selectedCustomer = new CustomerRecords(selectedCustomerId, selectedCMName, 
                    selectedAddress, selectedAddress2, selectedCity, 
                    selectedCountry, selectedPostalCode, selectedPhone);
            setTxtFields();
        });          
    }
    
    //clear text fields 
    private void clearTxtFields(){
        
        cmNameTxt.clear();
        cmAddressTxt.clear();        
        cmAddressTxt2.clear();        
        cmCityTxt.clear();       
        cmCountryTxt.clear();
        cmPostalCodeTxt.clear();
        cmPhoneNumberTxt.clear();         
    }    
    
    //set text fields for adding, updating, and deleting
    private void setTxtFields() {
        
        cmNameTxt.setText(selectedCMName);
        cmAddressTxt.setText(selectedAddress);
        cmAddressTxt2.setText(selectedAddress2);
        cmCityTxt.setText(selectedCity);
        cmCountryTxt.setText(selectedCountry);
        cmPostalCodeTxt.setText(selectedPostalCode);
        cmPhoneNumberTxt.setText(selectedPhone);  
    }    
    
    //personal project - i want to mess around with different types of searches 
    @FXML
    private void clickSearchButton(ActionEvent event) {
        
        int customerId = 0;
        //use search to limit results in table
        //customerTableView.setItems(CustomerRecords.searchRecords(customerId));
    }
    
    //clears text fields to add, update, delete items in DB
    @FXML
    private void clickClearButton(ActionEvent event) {
        
        clearTxtFields();
    }    
    
    //commits add (insert), update, delete operation
    @FXML
    private void clickSubmitButton(ActionEvent event) {
                        
        String customerName = cmNameTxt.getText();
        String address = cmAddressTxt.getText();
        String address2 = cmAddressTxt2.getText();
        String city = cmCityTxt.getText();
        String country = cmCountryTxt.getText();
        String postalCode = cmPostalCodeTxt.getText();
        String phone = cmPhoneNumberTxt.getText();                
        
        Connection conn = DatabaseConnection.estConnection();
        
        try {
                        
            String choice = actionChoices.getValue();
            
            switch (choice) {
                //call insert method
                case "Add":
                    boolean inputEx;
                    inputEx = CustomerRecords.inputException(customerName, 
                            address, address2, city, country, postalCode, phone);
                    if(inputEx==true) {
                        
                        JOptionPane.showMessageDialog(null, "Field required. "
                                + "Please enter valid input.");
                    }
                    else {
                        CustomerSQL.insertCustomerSQL(conn, customerName, address, 
                                address2, city, country, postalCode, phone);

                        //clear text fields
                        clearTxtFields();                          
                    }
                    break;
                    
                //call update method
                case "Update":
                    
                    boolean inputExU;
                    inputExU = CustomerRecords.inputException(customerName, 
                            address, address2, city, country, postalCode, phone);
                    if(inputExU==true) {
                        
                        JOptionPane.showMessageDialog(null, "Field required. "
                                + "Please enter valid input.");
                    }
                    else {
                        
                        CustomerSQL.updateCustomerSQL(conn, customerName, address, 
                                address2, city, country, postalCode, phone);
                        AddressSQL.updateAddressSQL(conn, customerName, address, 
                                address2, city, country, postalCode, phone);
                        //clear text fields
                        clearTxtFields();                         
                    }
                    break;
                    
                //call delete method
                case "Delete":

                    boolean inputExD;
                    inputExD = CustomerRecords.inputException(customerName, 
                            address, address2, city, country, postalCode, phone);
                    if(inputExD==true) {
                        
                        JOptionPane.showMessageDialog(null, "Field required. "
                                + "Please enter valid input.");
                    }
                    else {
                        
                        CustomerSQL.deleteCustomerSQL(conn, customerName, address, 
                                address2, city, country, postalCode, phone);
                        //clear text fields
                        clearTxtFields();                        
                    }                    
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
        
        populateCustomerRecords();  
    }    
    
    @FXML
    private void clickBackButton(ActionEvent event) throws IOException {
        
        customerTableView.getItems().clear();        
        DatabaseConnection.dbCloseConnection();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();           
        
    }

    @FXML
    void clickExitButton(ActionEvent event) {
        
        JOptionPane.showMessageDialog(null,"Exiting Application");
        DatabaseConnection.dbCloseConnection();
        System.exit(0);
    }
    
    //retrieves data from DB and loads it into table
    private void populateCustomerRecords() {
        
        //clear table view
        customerTableView.getItems().clear();
        
        //connect to db
        Connection conn = DatabaseConnection.estConnection();
        
        //SQL statement to retrieve data from DB
        try {
            //
            CustomerSQL.selectCustomerSQL(conn);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerRecordsController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        //store customer objects in table view
        customerTableView.setItems(CustomerRecords.getAllCustomers());
        
        //populate items in cell
        cmIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        cmNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        address2Col.setCellValueFactory(new PropertyValueFactory<>("address2"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));        
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));        
    }
}
