/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Model.Appt;
import Model.ApptDBManager;
import static Model.ApptDBManager.getCities;
import Model.Customer;
import static Model.CustomerDBManager.getAllCustomers;
import Model.User;
import static Model.UserDBManager.getAllUsers;
import Utilities.DatabaseConn;
import Utilities.Validator;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tyler
 */
public class ApptModController implements Initializable {

    @FXML    private Button apptModSaveButton;
    @FXML    private Button apptModCancelButton;
    @FXML    private TextArea apptModDescBox;
    @FXML    private ComboBox apptModConsultDrop;
    @FXML    private ComboBox apptModCustDrop;
    @FXML    private ComboBox apptModStartTime;
    @FXML    private ComboBox apptModEndTime;
    @FXML    private ComboBox apptModLocation;
    @FXML    private ComboBox apptModTypeDrop;
    @FXML    private DatePicker apptModDatePicker;
    @FXML    private TextField apptModApptId;
    Appt appt;
    
    
    private ObservableList<Customer> custList = FXCollections.observableArrayList();
    private ObservableList<String> custNames = FXCollections.observableArrayList();
    
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<String> userNames = FXCollections.observableArrayList();
    
    private ObservableList<String> times = FXCollections.observableArrayList("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", 
            "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30");
    
    private ObservableList<String> apptTypes = FXCollections.observableArrayList("Interview", "Sales", "Follow Up");
    
    private final ObservableList<String> cityList = FXCollections.observableArrayList();
    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //Initialize location list. 
        apptModLocation.setItems(getCities());
        
        //Initialize Customer List
        //I use the lambda expression here to simplify some code.  Similar to how I used it in the ApptAddController, I use a lambda for loop to pull out customer names. 
        custList = getAllCustomers();
        custList.forEach((c) -> {
            custNames.add(c.getCustomerName());
        }); 
        apptModCustDrop.setItems(custNames);
        
        //Initialize Consultant (user) list
        userList = getAllUsers();
        userList.forEach((u) -> {
            userNames.addAll(u.getUsername());
        });
        apptModConsultDrop.setItems(userNames);
        
        //Initialize time drop down lists
        apptModStartTime.setItems(times);
        apptModEndTime.setItems(times);
        
        //Initialize type drop down
        apptModTypeDrop.setItems(apptTypes);
        
        
        
    }    

    @FXML
    private void modApptSaveHandle(ActionEvent event) throws SQLException {
        try{
            Object custName = apptModCustDrop.getSelectionModel().getSelectedItem();
            Object userName = apptModConsultDrop.getSelectionModel().getSelectedItem();
            LocalDate date = apptModDatePicker.getValue();
            Object startTime = apptModStartTime.getSelectionModel().getSelectedItem();
            Object endTime = apptModEndTime.getSelectionModel().getSelectedItem();
            Object location = apptModLocation.getSelectionModel().getSelectedItem();
            Object type = apptModTypeDrop.getSelectionModel().getSelectedItem();
            String description = apptModDescBox.getText(); 
            String apptId = apptModApptId.getText();
            
            

            //check to make sure a customer is selected. 
            if(apptModCustDrop.getSelectionModel().getSelectedIndex()==-1){
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
            if(apptModConsultDrop.getSelectionModel().getSelectedIndex()==-1){
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
            if(apptModDatePicker.getValue() == null){
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
            if(apptModStartTime.getSelectionModel().getSelectedIndex()==-1||apptModEndTime.getSelectionModel().getSelectedIndex()==-1){
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
                        System.out.println("Start: "+startTime+" End: "+endTime);
                        throw new RuntimeException("");
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(ApptAddController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            //checks to make sure a location is selected. 
            if(apptModLocation.getSelectionModel().getSelectedIndex()==-1){
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
            if(apptModTypeDrop.getSelectionModel().getSelectedIndex()==-1){
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
                if(ApptDBManager.modAppt(Integer.parseInt(apptId), custName.toString(), userName.toString(), date, startTime.toString(), endTime.toString(), location.toString(), type.toString(), description)){
                    ApptDBManager.getWeekAppts();
                    ApptDBManager.getMonthAppts();
                    Stage stage = (Stage)  apptModCancelButton.getScene().getWindow();
                    stage.close();
                }else{
                    alert.setTitle("Error!");
                    alert.setHeaderText("Overlapping appt times.");
                    alert.setContentText("There is overlap with a scheduled appt.\nPlease modify times so they do not overlap.");
                    alert.showAndWait();
                }
                
            }
        
        }catch(RuntimeException re){
            System.out.println(re);         
        }
    }

    @FXML
    private void modApptCancelHandle(ActionEvent event) {
        Stage stage = (Stage) apptModCancelButton.getScene().getWindow();
        stage.close();
    }
    
    
    public void setAppt(Appt appt){
        this.appt = appt;
        
        int apptId = appt.getApptId();
        
        DateTimeFormatter datef = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm");
        
        String startTime = appt.getApptStart();
        String endTime = appt.getApptEnd();
        
        //LocalDate date = LocalDate.parse(startTime.substring(0, 10), datef);
        String date = startTime.substring(0, 10);
        LocalDate localDate = LocalDate.parse(date);
        endTime = endTime.substring(11);
        startTime = startTime.substring(11);
        
        apptModDescBox.setText(appt.getApptDesc());
        apptModCustDrop.setValue(appt.getCustomerName());
        apptModConsultDrop.setValue(appt.getContactName());
        apptModLocation.setValue(appt.getApptLocation());
        apptModTypeDrop.setValue(appt.getApptType());
        apptModDatePicker.setValue(localDate);
        apptModStartTime.setValue(startTime);
        apptModEndTime.setValue(endTime);
        apptModApptId.setText(Integer.toString(apptId));
        
        
        
        
        //still need to figure out how to get times and date from the DB info. 
        
        
        
        
    
      
        
        
    }
    
}
