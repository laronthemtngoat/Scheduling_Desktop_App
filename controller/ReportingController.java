/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.DatabaseConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;
import model.Reporting;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author laron
 */
public class ReportingController implements Initializable {

    //containers
    Stage stage;
    Parent scene;
         
    @FXML
    private Button scheduleButton;
    @FXML
    private Button back;
    @FXML
    private Button exit;
    @FXML
    private Button createReport;
    @FXML
    private Button allCountriesBttn;
    @FXML
    private Button apptTypeCountBttn;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }    

    @FXML
    private void clickScheduleButton(ActionEvent event) throws IOException {
        
        //load alert box to get customerId
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/view/ReportBox.fxml"));
        try{
            Loader.load();
        }
        catch(IOException e){

            Logger.getLogger(AppointmentsController.class.getName())
                .log(Level.SEVERE, null, e);
        }
        ReportBoxController reportBoxController = Loader.getController();
        Parent p = Loader.getRoot();
        Stage alertstage = new Stage();
        alertstage.setScene(new Scene(p));
        alertstage.show(); 
    }    

    @FXML
    private void clickAllCountriesButton(ActionEvent event) throws SQLException {
        
        Reporting report = new Reporting();
        ObservableList countries = report.countriesReport();
        JOptionPane.showMessageDialog(null, countries);
    }
    
    @FXML
    private void clickBackButton(ActionEvent event) throws IOException {
        
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

    @FXML
    private void clickApptTypesButton(ActionEvent event) throws SQLException {
        
        Reporting report = new Reporting();
        
        Map map = report.monthlyApptTypesCount();
        JOptionPane.showMessageDialog(null, map);

    }
}