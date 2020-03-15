/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Model.ApptDBManager;
import Utilities.LoginTracker;
import Model.UserDBManager;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Tyler.Edmonds
 */
public class LoginController implements Initializable {

    @FXML    private TextField userName;
    @FXML    private TextField userPassword;
    @FXML    private Label userNameLabel;
    @FXML    private Label passwordLabel;
    @FXML    private Label titleLabel;
    @FXML    private Label userMessageLabel;
    @FXML    private Label defaultLanguageLabel;
    @FXML    private Button loginCancelButton;
    @FXML    private Button loginButton;
    private String basicError;
    private String loginError;
    private String errorText;
    
    public String username;

    @FXML
    public String loginButtonHandle(ActionEvent event) throws IOException {
        String username = userName.getText();
        String userPass = userPassword.getText();
        
        boolean successfulUser = UserDBManager.login(username, userPass);
        
        if (successfulUser){
            try{
                if(ApptDBManager.apptReminder()){
                    System.out.println("test");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment Reminder");
                    alert.setHeaderText("Appointment Reminder");
                    alert.setContentText("You have an appointment in the next 15 minutes.");
                    alert.showAndWait();
                }
                
                LoginTracker.log(username, true);
                FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/Views/MainScreen.fxml"));
                Parent root1 = (Parent) fxmlloader.load();
                Stage stage = new Stage();
                stage.setTitle("Scheduler Main Screen");
                stage.setScene(new Scene(root1));
                stage.setMinHeight(440);
                stage.setMinWidth(900);
                stage.show();
                Stage stage1 = (Stage) loginCancelButton.getScene().getWindow();
                stage1.close();
            }
            catch(IOException e){
                System.out.println("Exception occurred: " +e);
            }
        } else{
            LoginTracker.log(username, false);
            System.out.println("did not work");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(basicError);
            alert.setHeaderText(loginError);
            alert.setContentText(errorText);
            alert.showAndWait();            
        }
        
        
        return username;
        
    }

    @FXML
    private void loginCancelHandle(ActionEvent event) {
        Stage stage = (Stage) loginCancelButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("Loc/loginPage", locale);
        titleLabel.setText(rb.getString("title"));
        userNameLabel.setText(rb.getString("userName"));
        passwordLabel.setText(rb.getString("userPassword"));
        loginButton.setText(rb.getString("login"));
        loginCancelButton.setText(rb.getString("cancel"));
        userMessageLabel.setText(rb.getString("userMessage"));
        defaultLanguageLabel.setText(rb.getString("defaultLanguage"));
        basicError = rb.getString("basicError");
        loginError = rb.getString("loginError");
        errorText = rb.getString("errorText");
        
        
    } 



    
}
