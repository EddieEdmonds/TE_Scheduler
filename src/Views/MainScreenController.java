/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Model.Appt;
import Model.ApptDBManager;
import static Model.ApptDBManager.getAllAppts;
import static Model.ApptDBManager.getMonthAppts;
import static Model.ApptDBManager.getWeekAppts;
import Model.Customer;
import Model.CustomerDBManager;
import static Model.CustomerDBManager.deleteCustomer;
import static Model.CustomerDBManager.getAllCustomers;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tyler
 */
public class MainScreenController implements Initializable {

    @FXML    private Tab apptWeek;
    @FXML    private Tab apptMonth;
    @FXML    private Button mainCloseButton;
    @FXML    private TableColumn<Appt, String> calWCustCol;
    @FXML    private TableColumn<Appt, String> calWConsultCol;
    @FXML    private TableColumn<Appt, String> calWTypeCol;
    @FXML    private TableColumn<Appt, String> calWStartTimeCol;
    @FXML    private TableColumn<Appt, String> calWEndTimeCol;
    @FXML    private TableColumn<Appt, String> calMCustCol;
    @FXML    private TableColumn<Appt, String> calMConsultCol;
    @FXML    private TableColumn<Appt, String> calMTypeCol;
    @FXML    private TableColumn<Appt, String> calMStartTimeCol;
    @FXML    private TableColumn<Appt, String> calMEndTimeCol;
    @FXML    private TableColumn<Appt, Integer> calWApptIdCol;
    @FXML    private TableColumn<Appt, Integer> calMApptIdCol;
    @FXML    private Button apptModButton;
    @FXML    private Button apptDeleteButton;
    @FXML    private Button apptAddButton;
    @FXML    private Button custManageButton;
    @FXML    private Button reportsButton;
    @FXML    private Button logsButton;
    @FXML    private TableView<Appt> apptWeekTableView;
    @FXML    private TableView<Appt> apptMonthTableView;
    private ObservableList<Integer> customerIds = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            // TODO
            apptWeekTableView.setItems(getWeekAppts());
            apptMonthTableView.setItems(getMonthAppts());
            calWApptIdCol.setCellValueFactory(new PropertyValueFactory("apptId"));
            calWCustCol.setCellValueFactory(new PropertyValueFactory("customerName"));
            calWConsultCol.setCellValueFactory(new PropertyValueFactory("contactName"));
            calWTypeCol.setCellValueFactory(new PropertyValueFactory("apptType"));
            calWStartTimeCol.setCellValueFactory(new PropertyValueFactory("apptStart"));
            calWEndTimeCol.setCellValueFactory(new PropertyValueFactory("apptEnd"));
            calMApptIdCol.setCellValueFactory(new PropertyValueFactory("apptId"));
            calMCustCol.setCellValueFactory(new PropertyValueFactory("customerName"));
            calMConsultCol.setCellValueFactory(new PropertyValueFactory("contactName"));
            calMTypeCol.setCellValueFactory(new PropertyValueFactory("apptType"));
            calMStartTimeCol.setCellValueFactory(new PropertyValueFactory("apptStart"));
            calMEndTimeCol.setCellValueFactory(new PropertyValueFactory("apptEnd"));
        } catch (SQLException ex) {
            String se = "MainScreen Initializer SQL Exception: "+ex;
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, se);
        }

        
    }    

    @FXML
    private void apptModHandle(ActionEvent event) {

        try{
            Appt selectedWeekAppt = apptWeekTableView.getSelectionModel().getSelectedItem();
            Appt selectedMonthAppt = apptMonthTableView.getSelectionModel().getSelectedItem();
            if(selectedWeekAppt!=null){
                FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ApptMod.fxml"));
                Parent root1 = (Parent) fxmlloader.load();
                Stage stage = new Stage();
                stage.setTitle("Modify Appointment");
                stage.setScene(new Scene(root1));
                stage.show();
                ApptModController controller = fxmlloader.getController();
                controller.setAppt(selectedWeekAppt);
            }else if(selectedMonthAppt!=null){
                FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ApptMod.fxml"));
                Parent root1 = (Parent) fxmlloader.load();
                Stage stage = new Stage();
                stage.setTitle("Modify Appointment");
                stage.setScene(new Scene(root1));
                stage.show();
                ApptModController controller = fxmlloader.getController();
                controller.setAppt(selectedMonthAppt);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No appointment selected");
                alert.setHeaderText("Please select an appointment.");
                alert.setContentText("Please select an appointment to modify.");
                alert.showAndWait();
            }
        }catch(IOException ioe){
            System.out.println(ioe);
        }        
        
        
       
    }

    @FXML
    private void apptDeleteHandle(ActionEvent event) throws SQLException {
        
        Appt selectedApptWeek = apptWeekTableView.getSelectionModel().getSelectedItem();
        Appt selectedApptMonth = apptMonthTableView.getSelectionModel().getSelectedItem();
        if(selectedApptWeek!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("Deleting appointment.");
            alert.setContentText("You are attempting to delete an appointment, is this correct?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                int apptId = selectedApptWeek.getApptId();
                ApptDBManager.deleteAppt(apptId);
            }
        }else if(selectedApptMonth!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("Deleting appointment.");
            alert.setContentText("You are attempting to delete an appointment, is this correct?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                int apptId = selectedApptMonth.getApptId();
                ApptDBManager.deleteAppt(apptId);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No customer selected");
            alert.setHeaderText("Please select customer.");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
        }
        
        ApptDBManager.getWeekAppts();
        ApptDBManager.getMonthAppts();
        
    }

    @FXML
    private void apptAddHandle(ActionEvent event) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ApptAdd.fxml"));
            Parent root1 = (Parent) fxmlloader.load();
            Stage stage = new Stage();
            stage.setTitle("Scheduler Main Screen");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void custManageHandle(ActionEvent event) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("CustomerManage.fxml"));
        Parent root1 = (Parent) fxmlloader.load();
        Stage stage = new Stage();
        stage.setTitle("Scheduler Main Screen");
        stage.setScene(new Scene(root1));
        getAllCustomers();
        stage.show();
    }

    @FXML
    private void reportsHandle(ActionEvent event) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("Reports.fxml"));
        Parent root1 = (Parent) fxmlloader.load();
        Stage stage = new Stage();
        stage.setTitle("Reports Screen");
        stage.setScene(new Scene(root1));
        getAllCustomers();
        stage.show();
    }

    @FXML
    private void logsHandle(ActionEvent event) {
       if(Desktop.isDesktopSupported()){
           try{
               File file = new File("LoginTracker.txt");
               Desktop.getDesktop().open(file);
           } catch (IOException ioe){
               
           }
       }
    }

    @FXML
    private void mainCloseHandle(ActionEvent event) {
        Stage stage = (Stage) mainCloseButton.getScene().getWindow();
        stage.close();
    }
    
}
