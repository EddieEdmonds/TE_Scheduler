/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Model.Customer;
import Model.CustomerDBManager;
import Utilities.DatabaseConn;
import Utilities.Validator;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tyler
 */
public class CustomerModController implements Initializable {

    @FXML    private ComboBox custModCityDrop;
    @FXML    private TextField custModCountryField;
    @FXML    private TextField custModNameField;
    @FXML    private TextField custModPhoneField;
    @FXML    private TextField custModAddr1Field;
    @FXML    private TextField custModAddr2Field;
    @FXML    private TextField custModZipField;
    @FXML    private Button custModSaveButton;
    @FXML    private Button custModCancelButton;
    private int custId;
    Customer customer;
    
    private final ObservableList<String> cityList = FXCollections.observableArrayList();
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        custModCityDrop.setItems(getCity());
    }    
    
    @FXML
    public void setCountry(){
        String selectedCity = custModCityDrop.getSelectionModel().getSelectedItem().toString();
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT * FROM cityCountryView";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                if(selectedCity.equals(rs.getString("city"))){
                    custModCountryField.setText(rs.getString("country"));
                }
            }
            stmt.close();
        }
        catch(SQLException se){
            System.out.println("customerModController - setCountry() - SQL Error: " + se.getMessage());
        }
    }
    
    @FXML //I use this to dynamically pull a lit of Cities from the DB and populate the comboBox on the Add Customer page. 
    private ObservableList<String> getCity(){
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT city, countryId FROM city";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                cityList.addAll(rs.getString("city"));
            }
            stmt.close();
            return cityList;
        }
        catch(SQLException se){
            System.out.println("CustomerModController - getCity() - SQL Error: " + se.getMessage());
            return null;
        }
    }
    
    

    @FXML
    private void custModSaveHandle(ActionEvent event) throws SQLException {
        try{
            //get customer entered values and use these to pass into saveCustomer();
            String name = custModNameField.getText();
            String phone = custModPhoneField.getText();
            String address1 = custModAddr1Field.getText();
            String address2 = custModAddr2Field.getText();
            Integer cityId = custModCityDrop.getSelectionModel().getSelectedIndex()+1;
            String country = custModCountryField.getText();
            String zip = custModZipField.getText();
            
            
            //I use my validator functions here to make sure the phone number is only numeric or "-" and that the text fields are Aa-Zz.
            boolean validPhone = Validator.validatePhone(phone);
            boolean validName = Validator.validateText(name);
            boolean validAddress1 = Validator.validateAddr(address1);
            boolean validAddress2 = Validator.validateAddr(address2);
            boolean validZip = Validator.validatePhone(zip);

            System.out.println("Valid Zip: " + validZip);
            
            
            //The following if statements just check to make sure everything entered is valid. 
            //If not, throws an error informting the user to enter info. 
            if(validName==true){ 
                System.out.println("Valid name: " + validName);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid or no name entered.");
                alert.setContentText("Please enter a valid name, consisting of \n"
                        + "only characters Aa-Zz");

                alert.showAndWait();
                throw new RuntimeException("Invalid or no name entered.");
            }
            
            if(validPhone==true){
                System.out.println("Valid phone: " + validPhone);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid phone number.");
                alert.setContentText("Please enter a valid phone number consisting of \n"
                        + "numerals 0-9. You may also include '-' (dash).");

                alert.showAndWait();
                throw new RuntimeException("Invalid phone number.");
            }
            
            if(validAddress1==true){
                System.out.println("Valid address 1: " + validAddress1);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid Address 1.");
                alert.setContentText("Please enter a valid address consisting of \n "
                        + "characters Aa-Zz and numerals 0-9");

                alert.showAndWait();
                throw new RuntimeException("Invalid address 1.");
            }
            
            if(validAddress2==true){
                System.out.println("Valid address 2: " + validAddress2);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid Address 2.");
                alert.setContentText("Please enter a valid 2nd address consisting of \n "
                        + "characters Aa-Zz and numerals 0-9");

                alert.showAndWait();
                throw new RuntimeException("Invalid address 2.");
            }
            
            if(cityId!=0){//This, by default, validates Country as well.  
                //You have to select a city to continue, and when you select a city, it auto populates the country from the DB based on the cityId/countryId.
                System.out.println("City ID: " + cityId.toString());
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("No city selected.");
                alert.setContentText("Please select a city.");

                alert.showAndWait();
                throw new RuntimeException("No city selected.");
            }
            
            if(validZip==true){
                System.out.println("Zip code: " + zip);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid Zip Code entered.");
                alert.setContentText("Please enter a valid zip code consisting of \n"
                        + "numerals 0-9.");

                alert.showAndWait();
                throw new RuntimeException("Invalid Zip Code entered.");
            }
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Customer Confirmation");
            alert.setHeaderText("Adding new Customer.");
            alert.setContentText("Click OK to save customer: " + name);
            Optional<ButtonType> result = alert.showAndWait();
            
            if (result.get() == ButtonType.OK) {
                    
                    CustomerDBManager.updateCustomer(name, phone, address1, address2, cityId, country, zip, custId);
                    CustomerDBManager.getAllCustomers();
                    Stage stage = (Stage)  custModCancelButton.getScene().getWindow();
                    stage.close();
                }
            
            
        }catch(RuntimeException re){
            System.out.println(re);
        }
        
        
    }

    @FXML
    private void custModCancelHandle(ActionEvent event) {
        Stage stage = (Stage) custModCancelButton.getScene().getWindow();
        stage.close();
    }
    
    public void setCust(Customer cust){
        this.customer = cust;
        
        custId = cust.getCustomerId();
        
        custModNameField.setText(cust.getCustomerName());
        custModPhoneField.setText(cust.getPhone());
        custModAddr1Field.setText(cust.getAddress1());
        custModAddr2Field.setText(cust.getAddress2());
        custModCityDrop.setValue(cust.getCity());
        custModCountryField.setText(cust.getCountry());
        custModZipField.setText(cust.getZip());
        
        
    }

}
