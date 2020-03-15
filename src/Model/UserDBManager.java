/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

//import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Utilities.DatabaseConn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author Tyler.Edmonds
 */
public class UserDBManager {
    
    private static final ObservableList<User> allUsers = FXCollections.observableArrayList();
    
    private static User loggedInUser;

    
    public static User getCurrentUser(){
        return loggedInUser;
    }
    
    public static  ObservableList<User> getAllUsers(){
        allUsers.clear();
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT userName FROM user";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){  
                User u = new User(rs.getString("userName"));
                allUsers.add(u);
            }
            stmt.close();
            return allUsers;
            
        }catch(SQLException se){
            System.out.println("getAllUsers() SQL Error: " + se.getMessage());
            return null;
        }
    }
    
    public static Boolean login(String username, String userPassword){
        return true;
    }
    
//    public static Boolean login (String username, String userPassword){
//        try{
//            Statement stmt = DatabaseConn.getDbConnection().createStatement();
//            String sqlQuery = "SELECT * FROM user WHERE userName='" + username + "' AND password='" +userPassword+"'";
//            ResultSet rs = stmt.executeQuery(sqlQuery);
//            if (rs.next()){
//                loggedInUser = new User();
//                loggedInUser.setUsername(rs.getString("userName"));
//                stmt.close();
//                
//                return true;
//            }
//            else{
//                return false;
//            }
//            
//        }
//        catch(SQLException e){
//            System.out.println("Unable to connecto to DB");
//            System.out.println("login() SQL Exception: " + e);
//            
//            return false;        
//        }
//            
//    }
    
    
    
}
