/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.time.ZonedDateTime;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author tyler
 */
public final class Appt {
    
    private final SimpleIntegerProperty apptId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty userId = new SimpleIntegerProperty();
    private final SimpleStringProperty customerName = new SimpleStringProperty();
    private final SimpleStringProperty apptTitle = new SimpleStringProperty();
    private final SimpleStringProperty apptDesc = new SimpleStringProperty();
    private final SimpleStringProperty apptLocation = new SimpleStringProperty();
    private final SimpleStringProperty contactName = new SimpleStringProperty();
    private final SimpleStringProperty apptType = new SimpleStringProperty();
    private final SimpleStringProperty apptStart = new SimpleStringProperty();
    private final SimpleStringProperty apptEnd = new SimpleStringProperty();
    

    public Appt(){};
    

//    public Appt(String apptTitle, String apptDesc, String apptLocation, 
//            String contactName, String apptType, String apptStart, String apptEnd, int customerId, int userId){
//    
//        setCustomerId(customerId);
//        setUserId(userId);
//        setApptTitle(apptTitle);
//        setApptDesc(apptDesc);
//        setApptLocation(apptLocation);
//        setContactName(contactName);
//        setApptType(apptType);
//        setApptStart(apptStart);
//        setApptEnd(apptEnd);    
//    };
    
    public Appt(String customerName, String contactName, String apptType, String apptStart, String apptEnd,
            String apptTitle, String apptDesc, String apptLocation, int customerId, int userId){
    
        setCustomerName(customerName);
        setCustomerId(customerId);
        setUserId(userId);
        setApptTitle(apptTitle);
        setApptDesc(apptDesc);
        setApptLocation(apptLocation);
        setContactName(contactName);
        setApptType(apptType);
        setApptStart(apptStart);
        setApptEnd(apptEnd);    
    };
    
    public Appt(int apptId, String customerName, String contactName, String apptType, String apptStart, String apptEnd,
            String apptTitle, String apptDesc, String apptLocation, int customerId, int userId){
    
        setApptId(apptId);
        setCustomerName(customerName);
        setCustomerId(customerId);
        setUserId(userId);
        setApptTitle(apptTitle);
        setApptDesc(apptDesc);
        setApptLocation(apptLocation);
        setContactName(contactName);
        setApptType(apptType);
        setApptStart(apptStart);
        setApptEnd(apptEnd);    
    };

    

   
    
    public int getApptId(){
        return apptId.get();
    }
    public void setApptId(int apptId){
        this.apptId.set(apptId);
    }
    
    public String getCustomerName(){
        return customerName.get();
    }
    public void setCustomerName(String customerName){
        this.customerName.set(customerName);
    }
    
    
    public int getCustomerId(){
        return customerId.get();
    }
    public void setCustomerId(int customerId){
        this.customerId.set(customerId);
    }
    
    public int getUserId(){
        return userId.get();
    }
    public void setUserId(int userId){
        this.userId.set(userId);
    }
    
    public String getApptTitle(){
        return apptTitle.get();
    }
    public void setApptTitle(String apptTitle){
        this.apptTitle.set(apptTitle);
    }
    
    public String getApptDesc(){
        return apptDesc.get();
    }
    public void setApptDesc(String apptDesc){
        this.apptDesc.set(apptDesc);
    }
    
    public String getApptLocation(){
        return apptLocation.get();
    }
    public void setApptLocation(String apptLocation){
        this.apptLocation.set(apptLocation);
    }
    
    public String getContactName(){
        return contactName.get();
    }
    public void setContactName(String contactName){
        this.contactName.set(contactName);
    }
    
    public String getApptType(){
        return apptType.get();
    }
    public void setApptType(String apptType){
        this.apptType.set(apptType);
    }
    
    public String getApptStart(){
        return apptStart.get();
    }
    public void setApptStart(String apptStart){
        this.apptStart.set(apptStart);
    }
    
    public String getApptEnd(){
        return apptEnd.get();
    }
    public void setApptEnd(String apptEnd){
        this.apptEnd.set(apptEnd);
    }
    
    
}
