/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Utilities.DatabaseConn;
import Model.UserDBManager.*;
import static Model.UserDBManager.getCurrentUser;
import Views.LoginController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Tyler.Edmonds
 */
public class CustomerDBManager {
    
    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    
//    *************Leave this for now. Not sure if I'll end up needing this function or not.*************
    
    public static Customer getCustomerName(int id){
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT * FROM user WHERE userId='" + id + "'";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                Customer customer = new Customer();
                customer.setCustomerName(rs.getString("customerName"));
                stmt.close();
                return customer;
            }
        }catch(SQLException se){
            System.out.println("getCustomerName() SQL Error: " + se.getMessage());
            
        }
        return null;
    }
    
    
    public static ObservableList<Customer> getAllCustomers(){
        allCustomers.clear();
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT c.customerId, c.customerName, a.phone, a.address, a.address2, ct.city, co.country, a.postalCode "
                    + "FROM customer c INNER JOIN address a ON c.addressId = a.addressId "
                    + "INNER JOIN city ct ON a.cityId = ct.cityId "
                    + "INNER JOIN country co ON ct.countryId = co.countryId "
                    + "ORDER BY customerId";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){                
                allCustomers.add(new Customer(
                        rs.getInt("customerId"), rs.getString("customerName"), 
                        rs.getString("phone"), rs.getString("address"), 
                        rs.getString("address2"), rs.getString("city"), 
                        rs.getString("country"), rs.getString("postalCode")));
            }
            stmt.close();
            return allCustomers;
            
        }catch(SQLException se){
            System.out.println("getAllCustomers() SQL Error: " + se.getMessage());
            return null;
        }
    }
    
    public static boolean saveCustomer(String customerName, String phone, String address1, String address2, int cityId, String country, String zip) throws SQLException{
        String createdBy = getCurrentUser().getUsername();
        String lastUpdateBy = createdBy;
        try{         
            DatabaseConn.getDbConnection().setAutoCommit(false); //Because the below INSERTS are set up as two separate inserts, 
            //using setAutoCommit(false) prevents the inserts from committing until both have ran successfully. 
            //The catch statmenet actually rolls them back if either one fails. 
            
            String sqlOne = "INSERT INTO address (address, address2, postalCode, phone, cityId, createdBy, lastUpdateBy, createDate, lastUpdate) "
                + "VALUES(?,?,?,?,?,?,?,now(),now())";
            String sqlTwo = "INSERT INTO customer (customerName, createDate, lastUpdate, addressId, createdBy, lastUpdateBy, active) "
                + "VALUES (?, now(), now(), LAST_INSERT_ID(), ?, ?,?)";
            
            
            PreparedStatement ps1 = DatabaseConn.getDbConnection().prepareStatement(sqlOne);
            ps1.setString(1, address1);
            ps1.setString(2, address2);
            ps1.setString(3, zip);
            ps1.setString(4, phone);
            ps1.setInt(5, cityId);
            ps1.setString(6, createdBy);
            ps1.setString(7, lastUpdateBy);
            ps1.execute();   
            ps1.close();
            
            PreparedStatement ps2 = DatabaseConn.getDbConnection().prepareStatement(sqlTwo);
            ps2.setString(1, customerName);
            ps2.setString(2, createdBy);
            ps2.setString(3, lastUpdateBy);
            ps2.setInt(4, 1);
            ps2.execute();
            ps2.close();
            
            DatabaseConn.getDbConnection().commit();

        }catch(SQLException se){
            DatabaseConn.getDbConnection().rollback();
            System.out.println("Failure, query rolled back. SQL Error: " + se.getMessage());
            
        }
        
        return false;
            
        }
    
    public static boolean updateCustomer(String customerName, String phone, String address1, String address2, int cityId, String country, String zip, int custId) throws SQLException{
        String lastUpdateBy = getCurrentUser().getUsername();

        try{         
            DatabaseConn.getDbConnection().setAutoCommit(false);
            
            String sqlUpdate = "UPDATE address INNER JOIN customer ON address.addressId = customer.addressId "
                    + "SET address.address = ?, address.address2 = ?, address.postalCode = ?, address.phone = ?, address.cityId = ?, address.lastUpdateBy = ?, address.lastUpdate = now(), "
                    + "customer.customerName = ?, customer.lastUpdate = now(), customer.lastUpdateBy = ? "
                    + "WHERE customer.customerId = ?";    
            
            PreparedStatement ps1 = DatabaseConn.getDbConnection().prepareStatement(sqlUpdate);
            ps1.setString(1, address1);
            ps1.setString(2, address2);
            ps1.setString(3, zip);
            ps1.setString(4, phone);
            ps1.setInt(5, cityId);
            ps1.setString(6, lastUpdateBy);
            ps1.setString(7, customerName);
            ps1.setString(8, lastUpdateBy);
            ps1.setInt(9, custId);
            ps1.execute();   
            ps1.close();
            
            DatabaseConn.getDbConnection().commit();

        }catch(SQLException se){
            DatabaseConn.getDbConnection().rollback();
            System.out.println("Failure, query rolled back. SQL Error: " + se.getMessage());
            
        }
        
        return false;
            
        }
    
    public static boolean deleteCustomer(int customerId) throws SQLException{
        
        
        try{
            DatabaseConn.getDbConnection().setAutoCommit(false);
            
            int test = customerId;
            //Deleting customer from customer table where customerId = the ID we pass into this function. 
            String sqlDeleteCustomer = "DELETE c, a FROM customer c INNER JOIN address a ON a.addressId = c.addressId "
                    + "WHERE c.customerId = ?";
            PreparedStatement ps2 = DatabaseConn.getDbConnection().prepareStatement(sqlDeleteCustomer);
            ps2.setInt(1, test);
            ps2.executeUpdate();
            ps2.close();
            
            DatabaseConn.getDbConnection().commit();
            
        }catch(SQLException se){
            DatabaseConn.getDbConnection().rollback();
            System.out.println("Failure, query rolled back. SQL Error: " + se.getMessage());
            
        }
        
        
        
        return true;
    }
    
    
}
