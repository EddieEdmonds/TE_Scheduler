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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tyler
 */
public class CustomerAddController implements Initializable {

    @FXML    private ComboBox custAddCityDrop;
    @FXML    private TextField custAddNameField;
    @FXML    private TextField custAddPhoneField;
    @FXML    private TextField custAddAddr1Field;
    @FXML    private TextField custAddAddr2Field;
    @FXML    private TextField custAddZipField;
    @FXML    private Button custAddCancelButton;
    @FXML    private TextField custAddCountryField;

    
    private final ObservableList<String> cityList = FXCollections.observableArrayList();


    
    @FXML  //Used to set the text in the country Text Box based on the city selected from the combo box. 
    //This and getCity combined allow for growth of the app. 
    //If the company expands into new countries/cities, they just need added to the DB and the app will pull them dynamicalyl. 
    public void setCountry(){
        String selectedCity = custAddCityDrop.getSelectionModel().getSelectedItem().toString();
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT * FROM cityCountryView";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                if(selectedCity.equals(rs.getString("city"))){
                    custAddCountryField.setText(rs.getString("country"));
                }
            }
            stmt.close();
        }
        catch(SQLException se){
            System.out.println("setCountry() SQL Error: " + se.getMessage());
        }
    }
    
    @FXML //I use this to dynamically pull a lit of Cities from the DB and populate the comboBox on the Add Customer page. 
    public ObservableList<String> getCities(){
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
            System.out.println("getCities() SQL Error: " + se.getMessage());
            return null;
        }
    }

    @FXML
    public void custAddSaveHandle(ActionEvent event) throws SQLException {
        try{
            //get customer entered values and use these to pass into saveCustomer();
            String name = custAddNameField.getText();
            String phone = custAddPhoneField.getText();
            String address1 = custAddAddr1Field.getText();
            String address2 = custAddAddr2Field.getText();
            Integer cityId = custAddCityDrop.getSelectionModel().getSelectedIndex()+1;
            String country = custAddCountryField.getText();
            String zip = custAddZipField.getText();
            
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
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Error!");
//                alert.setHeaderText("Invalid or no name entered.");
//                alert.setContentText("Please enter a valid name, consisting of \n"
//                        + "only characters Aa-Zz");
//
//                alert.showAndWait();
//                throw new RuntimeException("Invalid or no name entered.");
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
                    CustomerDBManager.saveCustomer(name, phone, address1, address2, cityId, country, zip);
                    CustomerDBManager.getAllCustomers();
                    Stage stage = (Stage)  custAddCancelButton.getScene().getWindow();
                    stage.close();
                }
            
            
        }catch(RuntimeException re){
            System.out.println(re);
        }
        
    }

    @FXML
    private void custAddCancelHandle(ActionEvent event) {
        Stage stage = (Stage) custAddCancelButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //getCity();
        custAddCityDrop.setItems(getCities());
    }

}
