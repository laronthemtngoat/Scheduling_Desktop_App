/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import scheduling_desktop_ui.SchedulingDesktopApp;

/**
 *
 * @author laron
 */
public class Logging {
    
    public static void loginTimestamp() throws FileNotFoundException, IOException {
        
        //convert timestamp to string
        String loginTimestamp = SchedulingDesktopApp.loginTime.toString();
        
        //create string to add to log file
        String userTimestamp = "Username: " + SchedulingDesktopApp.currentUser +
                " UserID: " + SchedulingDesktopApp.userId + " Login Time: " + 
                loginTimestamp;
        
        System.out.println(userTimestamp);
        
        
        
        //add string to log file and catch exception if file is not found
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("sdi_logins_log", true))) {
            
            writer.newLine();
            writer.append(userTimestamp);            
            writer.close();
            System.out.println("Successfully wrote to the file.");            
        }
        catch(IOException e) {
            
            System.out.println(e.getMessage());

            try{
               
                File createFile = new File("sdi_logins_log");
                if(createFile.createNewFile()) {
                    System.out.println("Success");
                }
                else {
                    System.out.println("Fail");
                }
            }
            catch(IOException ex){
                
                System.out.println(ex.getMessage());
            }
        } 
    } 
}
