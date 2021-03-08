/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.CustomerSQL;
import DAO.DatabaseConnection;
import static controller.ApptAlertController.selectedCustomerId;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.CustomerRecords;

/**
 * FXML Controller class
 *
 * @author laron
 */
public class ApptAlertController implements Initializable {

    //FXML annotations
    @FXML
    private TableView<CustomerRecords> selectCustomerTableView;    
    @FXML
    private TableColumn<CustomerRecords, Integer> cmIdCol;
    @FXML
    private TableColumn<CustomerRecords, String> cmNameCol;
    
    //submit and cancel buttons -> redirect to appointments table
    @FXML
    private Button submitBttn;
    @FXML
    private Button cancelBttn;
    
    //public variable to store customer ID and pass to Insert SQL statement
    public static int selectedCustomerId = 1;
    
    //
    public static boolean customerSelected = false;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //connect to db
        Connection conn = DatabaseConnection.estConnection();
        
        try {
            //
            CustomerSQL.selectCustomerSQL(conn);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerRecordsController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
            
        //store customer objects in table view
        selectCustomerTableView.setItems(CustomerRecords.getAllCustomers());
        
        //populate items in cell
        cmIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        cmNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        
        selectCustomerTableView.setOnMousePressed((MouseEvent event) -> {
        selectedCustomerId = selectCustomerTableView.getSelectionModel().
            getSelectedItem().getCustomerId();}); 
    }    
    
    @FXML
    private void clickSubmitButton(ActionEvent event) {

        DatabaseConnection.dbCloseConnection();        
        AppointmentsController.addApptCustomerId = selectedCustomerId;
        selectCustomerTableView.getItems().clear();
        customerSelected = true;
        Stage stage = (Stage) submitBttn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clickCancelButton(ActionEvent event) {
        
        selectCustomerTableView.getItems().clear();
        Stage stage = (Stage) submitBttn.getScene().getWindow();
        stage.close();
        
    }
 
}
