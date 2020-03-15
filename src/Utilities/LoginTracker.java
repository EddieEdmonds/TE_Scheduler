/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import static Utilities.LoginTracker.logFile;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Tyler.Edmonds
 */
public class LoginTracker {
    
    
    public static File logFile = new File("LoginTracker.txt");
    
    public static void log(String username, boolean success){
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            LocalDateTime currentDT = LocalDateTime.now();
            FileWriter fw = new FileWriter(logFile, true);
            if(success==true){
                fw.write(username+" "+ dtf.format(currentDT)+" Success!" + "\r\n");
                fw.close();
            }else{
                fw.write(username+" "+ dtf.format(currentDT)+" Failure!" + "\r\n");
                fw.close();
            }
            
        }catch(Exception e){
            
        }
        
    }
    
        
}
