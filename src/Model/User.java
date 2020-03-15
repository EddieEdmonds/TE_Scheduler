/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Tyler.Edmonds
 */
public final class User {
    private String userNameString;
    
    
    private final SimpleStringProperty testUsername = new SimpleStringProperty();
    
    
    public User(){}
    
    public User(String userName){
        setUsername(userName);
    }
    
    
    
    public String getUsername(){
        return testUsername.get();
    }
    
    public void setUsername(String username){
        this.testUsername.set(username);
    }
    
    @Override
    public String toString(){
        return userNameString;
    }
    
    
}
