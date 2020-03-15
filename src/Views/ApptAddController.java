/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Model.ApptDBManager;
import static Model.ApptDBManager.getCities;
import Model.Customer;
import Model.CustomerDBManager;
import static Model.CustomerDBManager.getAllCustomers;
import Utilities.DatabaseConn;
import Model.User;
import static Model.UserDBManager.getAllUsers;
import Utilities.Validator;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tyler
 */
public class ApptAddController implements Initializable {

    @FXML    private Button apptAddSaveButton;
    @FXML    private Button apptAddCancelButton;
    @FXML    private TextArea apptAddDescBox;
    @FXML    private ComboBox apptAddCustomerDrop;
    @FXML    private ComboBox apptAddConsultDrop;
    @FXML    private ComboBox apptAddStartTime;
    @FXML    private ComboBox apptAddEndTime;
    @FXML    private ComboBox apptAddLocationDrop;
    @FXML    private ComboBox apptAddTypeDrop;
    @FXML    private DatePicker apptAddDatePicker;
    
    private ObservableList<Customer> custList = FXCollections.observableArrayList();
    private ObservableList<String> custNames = FXCollections.observableArrayList();
    
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<String> userNames = FXCollections.observableArrayList();
    
    private ObservableList<String> times = FXCollections.observableArrayList("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", 
            "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30");
    
    private ObservableList<String> apptTypes = FXCollections.observableArrayList("Interview", "Sales", "Follow Up");
    
    private final ObservableList<String> cityList = FXCollections.observableArrayList();
    //private final ObservableList<Integer> customerUserIds = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //Initialize Customer List
        //used lambda expressions here to simplify some code. 
        //From a method I already had, getAllCustomers() I use a lambda expression for loop in order to pull out just the customer name to fill a drop down list. 
        custList = getAllCustomers();
        custList.forEach((c) -> {
            custNames.add(c.getCustomerName());
        }); 
        apptAddCustomerDrop.setItems(custNames);
        
        //Initialize Consultant (user) list
        userList = getAllUsers();
        userList.forEach((u) -> {
            userNames.addAll(u.getUsername());
        });
        apptAddConsultDrop.setItems(userNames);
        
        //Initialize time drop down lists
        apptAddStartTime.setItems(times);
        apptAddEndTime.setItems(times);
        
        //Initialize cities drop down
        apptAddLocationDrop.setItems(getCities());
        
        //Initialize type drop down
        apptAddTypeDrop.setItems(apptTypes);
        
        
    }    
    
   
    
    public Integer getCustomerId(String customerName){
        try{
            String sqlOne = "SELECT customerId FROM customer WHERE customerName ='" +customerName+"'";
            return Integer.parseInt(sqlOne);
        }catch(NumberFormatException e){
            System.out.println(e);
        }
        return null;
    }
    
    public Integer getUserId(String userName){
        try{
            String sqlOne = "SELECT userId FROM user WHERE userName ='" +userName+"'";
            return Integer.parseInt(sqlOne);
        }catch(NumberFormatException e){
            System.out.println(e);
        }
        return null;
    }
    

    @FXML
    private void appAddSaveHandle(ActionEvent event) throws SQLException{
        try{
            Object custName = apptAddCustomerDrop.getSelectionModel().getSelectedItem();
            Object userName = apptAddConsultDrop.getSelectionModel().getSelectedItem();
            LocalDate date = apptAddDatePicker.getValue();
            Object startTime = apptAddStartTime.getSelectionModel().getSelectedItem();
            Object endTime = apptAddEndTime.getSelectionModel().getSelectedItem();
            Object location = apptAddLocationDrop.getSelectionModel().getSelectedItem();
            Object type = apptAddTypeDrop.getSelectionModel().getSelectedItem();
            String description = apptAddDescBox.getText(); 
            
            

            //check to make sure a customer is selected. 
            if(apptAddCustomerDrop.getSelectionModel().getSelectedIndex()==-1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("No customer selected. ");
                alert.setContentText("Please select a Customer Name.");
                alert.showAndWait();
                throw new RuntimeException("");
            }else{
                System.out.println("Valid Customer Name: "+custName);
            }

            //Check to make sure consultant/user is selected. 
            if(apptAddConsultDrop.getSelectionModel().getSelectedIndex()==-1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("No consultant selected. ");
                alert.setContentText("Please select a Consultant Name.");
                alert.showAndWait();
                throw new RuntimeException("");
            }else{
                System.out.println("Valid Consultant Name: "+userName);
            }

            //Checks date to make sure something is selected and then validates to make sure it's in the future. 
            if(apptAddDatePicker.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("No date selected. ");
                alert.setContentText("Please select a date in the future.");
                alert.showAndWait();
                throw new RuntimeException("");
            }else{
                if(Validator.validateDate(date)==true){
                System.out.println("Valid date: " + date);
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Invalid date.");
                    alert.setContentText("Please enter a valid date in the future.");

                    alert.showAndWait();
                    throw new RuntimeException("");
                }
                System.out.println("Valid Date picked: "+date);
            }

            //checks to make sure start time and end time are selected. If they are, then validate to make sure they're chronologically correct. 
            if(apptAddStartTime.getSelectionModel().getSelectedIndex()==-1||apptAddEndTime.getSelectionModel().getSelectedIndex()==-1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Start or End time not selected.");
                alert.setContentText("Please select a Start/End Time.");
                alert.showAndWait();
                throw new RuntimeException("");
            }else{
                try {
                    if(Validator.validateStartEndTime(startTime, endTime)==true){
                        System.out.println("Valid Start and End Times: "+startTime+" "+endTime);
                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error!");
                        alert.setHeaderText("Start Time is after end time.");
                        alert.setContentText("Please make sure start time is prior to end time.");
                        alert.showAndWait();
                        throw new RuntimeException("");
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(ApptAddController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            //checks to make sure a location is selected. 
            if(apptAddLocationDrop.getSelectionModel().getSelectedIndex()==-1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("No location selected. ");
                alert.setContentText("Please select a Location.");
                alert.showAndWait();
                throw new RuntimeException("");
            }else{
                System.out.println("Valid Location: "+location);
            }

            //checks to make sure type is selected. 
            if(apptAddTypeDrop.getSelectionModel().getSelectedIndex()==-1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("No type selected. ");
                alert.setContentText("Please select a Meeting Type.");
                alert.showAndWait();
                throw new RuntimeException("");
            }else{
                System.out.println("Valid Meeting Type: "+type);
            }

            //Checks to make sure description is entered. 
            if(description.equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("No description entered.");
                alert.setContentText("Please enter a description.");
                alert.showAndWait();
                throw new RuntimeException("");
            }else{
                System.out.println("Description: "+description);
            }
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Appointment Confirmation");
            alert.setHeaderText("Adding new Appointment.");
            alert.setContentText("Click OK to save appointment.");
            Optional<ButtonType> result = alert.showAndWait();
            
            if (result.get() == ButtonType.OK) {
                ApptDBManager.saveAppt(custName.toString(), userName.toString(), date, startTime.toString(), endTime.toString(), location.toString(), type.toString(), description);
                ApptDBManager.getWeekAppts();
                ApptDBManager.getMonthAppts();
                Stage stage = (Stage)  apptAddCancelButton.getScene().getWindow();
                stage.close();
            }
        
        }catch(RuntimeException re){
            System.out.println(re);         
        }
        
        
        
    }

    @FXML
    private void apptAddCancelHandle(ActionEvent event) {
        Stage stage = (Stage) apptAddCancelButton.getScene().getWindow();
        stage.close();
    }
    
}
