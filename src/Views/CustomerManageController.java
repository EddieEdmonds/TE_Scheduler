/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Model.Customer;
import Model.CustomerDBManager;
import static Model.CustomerDBManager.deleteCustomer;
import static Model.CustomerDBManager.getAllCustomers;
import Utilities.DatabaseConn;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tyler
 */
public class CustomerManageController implements Initializable {

    @FXML    private Button custCreateButton;
    @FXML    private Button custModButton;
    @FXML    private Button custDeleteButton;
    @FXML    private Button custManageCloseButton;
   
    @FXML    private TableView<Customer> custTableView;
    @FXML    private TableColumn<Customer, String> customerName;
    @FXML    private TableColumn<Customer, String> phone;
    @FXML    private TableColumn<Customer, String> address1;
    @FXML    private TableColumn<Customer, Integer> customerId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO         
        custTableView.setItems(getAllCustomers());
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        address1.setCellValueFactory(new PropertyValueFactory<>("address1"));

        
    }    

    @FXML
    private void custCreateHandle(ActionEvent event) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("CustomerAdd.fxml"));
            Parent root1 = (Parent) fxmlloader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Customer");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void custModHandle(ActionEvent event) {
        try {
            Customer selectedCust = custTableView.getSelectionModel().getSelectedItem();
            if(selectedCust==null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No customer selected");
                alert.setHeaderText("Please select customer.");
                alert.setContentText("Please select a customer to modify.");
                alert.showAndWait();
            }else{
                FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("CustomerMod.fxml"));
                Parent root1 = (Parent) fxmlloader.load();
                Stage stage = new Stage();
                stage.setTitle("Modify Customer");
                stage.setScene(new Scene(root1));
                stage.show();
                CustomerModController controller = fxmlloader.getController();
                controller.setCust(selectedCust);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void custDeleteHandle(ActionEvent event) throws SQLException {
        Customer selectedCust = custTableView.getSelectionModel().getSelectedItem();
        if(selectedCust==null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No customer selected");
                alert.setHeaderText("Please select customer.");
                alert.setContentText("Please select a customer to delete.");
                alert.showAndWait();
            }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText("Deleting customer.");
            alert.setContentText("You are attempting to delete a customer, is this correct?");
            alert.showAndWait().ifPresent(result -> { //used lambda here just to compare to the normal method. This is just as simple as an if statment with the button press result. 
            if(result == ButtonType.OK){
                try {
                    int selectedCustId = custTableView.getSelectionModel().getSelectedItem().getCustomerId();                    
                    System.out.println(selectedCustId);
                    deleteCustomer(selectedCustId);
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerManageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            });
        }
        CustomerDBManager.getAllCustomers();
    }

    @FXML
    private void custManageCloseHandle(ActionEvent event) {
        Stage stage = (Stage) custManageCloseButton.getScene().getWindow();
        stage.close();
    }
    
}
