/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduling_desktop_ui;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DAO.DatabaseConnection;
import java.sql.Timestamp;
import model.Address;
import model.Appointments;
import model.City;
import model.Country;
import model.Customer;
import model.DateTime;
import model.LoginForm;
import model.User;

/**
 *
 * @author laron
 */
public class SchedulingDesktopApp extends Application {
    
    //table objects
    public Address address;
    public Appointments appointment;
    public City city;
    public Country country;
    public Customer customer;
    public User user;
    
    //global variables
    public static String currentUser;
    public static int userId;
    public static Timestamp loginTime = DateTime.currentDateTime();
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
                
        LoginForm.identifyLocale();
        launch(args);
        
        DatabaseConnection.dbCloseConnection();
        
    }
    
}

