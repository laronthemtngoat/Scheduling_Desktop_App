/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.DatabaseConnection;
import DAO.ReportingSQL;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointments;

/**
 * FXML Controller class
 *
 * @author laron
 */
public class ReportBoxController implements Initializable {

    @FXML
    private Button okButton;
    private Text textData;
    @FXML
    private TableColumn<Appointments, Timestamp> startCol;
    @FXML
    private TableColumn<Appointments, Timestamp> endCol;
    @FXML
    private TableColumn<Appointments, String> descriptionCol;
    @FXML
    private TableView<Appointments> apptTableView;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Connection conn = DatabaseConnection.estConnection();
            ReportingSQL.selectConsultantAppointments(conn);
        } catch (SQLException ex) {
            Logger.getLogger(ReportBoxController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        apptTableView.setItems(Appointments.getConsultantAppointments());
        
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));        
        
    }    

    @FXML
    private void clickOKButton(ActionEvent event) {
        
        DatabaseConnection.dbCloseConnection();
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
    
    
    
}
