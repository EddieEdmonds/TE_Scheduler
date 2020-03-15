/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author tyler
 */
public class TableRow {
    private final SimpleStringProperty month = new SimpleStringProperty();
    private final SimpleStringProperty type = new SimpleStringProperty();
    private final SimpleIntegerProperty count = new SimpleIntegerProperty();
    
    private final SimpleStringProperty cons = new SimpleStringProperty();
    private final SimpleStringProperty cust = new SimpleStringProperty();
    private final SimpleStringProperty start = new SimpleStringProperty();
    private final SimpleStringProperty end = new SimpleStringProperty();
    
    public TableRow(){};
    
    public TableRow(String month, String type, int count){
       
        setMonth(month);
        setType(type);
        setCount(count);
        
    }
    
    public TableRow(String cons, String cust, String start, String end){
        
        setCons(cons);
        setCust(cust);
        setStart(start);
        setEnd(end);
        
    }
    
    
    public String getMonth(){
        return month.get();
    }
    private void setMonth(String month){
        this.month.set(month);
    }
    
    public String getType(){
        return type.get();
    }
    private void setType(String type){
        this.type.set(type);
    }
    
    public Integer getCount(){
        return count.get();
    }
    private void setCount(int count){
        this.count.set(count);
    }
    
    //Consultant
    public String getCons(){
        return cons.get();
    }
    private void setCons(String cons){
        this.cons.set(cons);
    }
    
    //Customer
    public String getCust(){
        return cust.get();
    }
    private void setCust(String cust){
        this.cust.set(cust);
    }
    
    public String getStart(){
        return start.get();
    }
    private void setStart(String start){
        this.start.set(start);
    }
    
    public String getEnd(){
        return end.get();
    }
    private void setEnd(String end){
        this.end.set(end);
    }
    
    
}
