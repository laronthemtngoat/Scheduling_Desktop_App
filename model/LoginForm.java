/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import DAO.CustomerSQL;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import scheduling_desktop_ui.SchedulingDesktopApp;

/**
 *DB Connection String
 *Server name: wgudb.ucertify.com
 *Port: 3306
 *Database name: U05m11
 *Username: U05m11
 *Password: 53688545345
 */

/**
 *
 * @author laron
 */

public class LoginForm {
    
    public static ResourceBundle identifyLocale(){
        
        
        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("languagefiles/lang", 
                locale);
        if(!(locale.getLanguage().equals("fr") | 
                locale.getLanguage().equals("es") | 
                locale.getLanguage().equals("en"))){
            
            JOptionPane.showMessageDialog(null, "Language not supported. "
                    + "Application will run in English");
            
            //rb = ResourceBundle.getBundle("languagefiles/lang_en.properties");
        }
       return rb;          
    }
    
    public static boolean checkCredentials(Connection conn, String un, String pw) {
                
        String username = un;
        String password = pw;
        boolean flag = false;
        
        
        //Localized messages
        String welcomeMessage = identifyLocale().getString("welcomeMessage");
        String invalidLogin = identifyLocale().getString("invalidLogin");
        
        try{
            String queryString = "SELECT userId, userName, password, active FROM"
                    + " user WHERE userName=? and password=? and active=1";
            CustomerSQL.setPreparedStatement(conn, queryString);
            PreparedStatement ps = CustomerSQL.getPreparedStatement();
           
            ps.setString(1, username);
            ps.setString(2, password);            
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    
                   
                    //welcomes user and logs into database                    
                    JOptionPane.showMessageDialog(null, welcomeMessage);                   
                    flag = true;                    
                    SchedulingDesktopApp.currentUser = username;
                    SchedulingDesktopApp.userId = rs.getInt("userId");
                }
                else {
                    
                    //invalid login message
                    JOptionPane.showMessageDialog(null, invalidLogin);
                    
                }
            }            
                
            } catch(SQLException sql){
                
                System.out.print(sql.getLocalizedMessage());
                
            }
        return flag;
    }
}
    
