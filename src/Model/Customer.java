/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Tyler.Edmonds
 */
public final class Customer {
    
    private final SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    private final SimpleStringProperty customerName = new SimpleStringProperty();
    private final SimpleStringProperty address1 = new SimpleStringProperty();
    private final SimpleStringProperty address2 = new SimpleStringProperty();
    private final SimpleStringProperty city = new SimpleStringProperty();
    private final SimpleStringProperty country = new SimpleStringProperty();
    private final SimpleStringProperty zip = new SimpleStringProperty();
    private final SimpleStringProperty phone = new SimpleStringProperty();
    

    
    public Customer(){};
    
    public Customer(int customerId, String customerName, String phone, String address1, 
            String address2, String city, String country, String zip){
        
        setCustomerId(customerId);
        setCustomerName(customerName);
        setPhone(phone);
        setAddress1(address1);
        setAddress2(address2);
        setCity(city);
        setCountry(country);
        setZip(zip);  
    }


    
    public int getCustomerId(){
        return customerId.get();
    }
    
    public String getCustomerName(){
        return customerName.get();
    }
    
    public String getPhone(){
        return phone.get();
    }
    
    public String getAddress1(){
        return address1.get();
    }
    
    public String getAddress2(){
        return address2.get();
    }
    
    public String getCity(){
        return city.get();
    }
    
    public String getCountry(){
        return country.get();
    }
    
    public String getZip(){
        return zip.get();
    }
    
    public void setCustomerId(int customerId){
        this.customerId.set(customerId);
    }
    
    public void setCustomerName(String customerName){
        this.customerName.set(customerName);
    }
    
    public void setPhone(String phone){
        this.phone.set(phone);
    }
    
    public void setAddress1(String address1){
        this.address1.set(address1);
    }
    
    public void setAddress2(String address2){
        this.address2.set(address2);
    }
    
    public void setCity(String city){
        this.city.set(city);
    }
    
    public void setCountry(String country){
        this.country.set(country);
    }
    
    public void setZip(String zip){
        this.zip.set(zip);
    }



    
}
