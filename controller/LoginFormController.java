/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import DAO.DatabaseConnection;
import model.Logging;
import model.LoginForm;
import static model.LoginForm.identifyLocale;

/**
 * FXML Controller class
 *
 * @author laron
 */
public class LoginFormController implements Initializable {
    
    //containers
    Stage stage;
    Parent scene;
    
    //variable to store login attempts
    int loginAttempts = 0;
    
    
    @FXML
    private Button loginButton;
    @FXML
    private TextField userName;
    @FXML
    private TextField passWord;    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Localized prompt text
        String un = identifyLocale().getString("usernamePrompt");
        String pw = identifyLocale().getString("passwordPrompt");
        String loginbttn = identifyLocale().getString("loginButton");
        userName.setPromptText(un);
        passWord.setPromptText(pw);
        loginButton.setText(loginbttn);
        
    }    

        
     

    
    @FXML
    void clickLoginButton(ActionEvent event) throws IOException, SQLException {
        
        String invalidAttempts = identifyLocale().getString("lockedOut");
        
        try{
            
            String un = userName.getText();
            String pw = passWord.getText();
            Connection connxn = DatabaseConnection.estConnection();
            if ((LoginForm.checkCredentials(connxn, un, pw)) == true) {                                
                
                Logging.loginTimestamp();
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();  
                loginAttempts = 0;

                DatabaseConnection.dbCloseConnection();
            }
            
            else {
                
                //increment variable to store number of login attempts
                loginAttempts += 1;
                
                if (loginAttempts >= 3) {
                    
                    //account locked message
                    JOptionPane.showMessageDialog(null, invalidAttempts);
                    
                    //reset count variable to zero
                    loginAttempts = 0;
                    
                    //close connection to the DB
                    DatabaseConnection.dbCloseConnection();
                    System.exit(0);
                }                 
            }

        }
        catch(IOException e){
            
            System.out.print(e);
            
        }
 
    }

}

