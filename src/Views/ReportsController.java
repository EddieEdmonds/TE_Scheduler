/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Model.TableRow;
import static Utilities.ReportGen.apptTypeMonth;
import static Utilities.ReportGen.consSchedAllTime;
import static Utilities.ReportGen.consSchedToday;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tyler
 */
public class ReportsController implements Initializable {

    @FXML    private TableColumn<TableRow, String> rep1MonthCol;
    @FXML    private TableColumn<TableRow, String> rep1TypeCol;
    @FXML    private TableColumn<TableRow, Integer> rep1CountCol;
    @FXML    private Button reportCloseButton;
    @FXML    private TableColumn<TableRow, String> consSchedConsCol;
    @FXML    private TableColumn<TableRow, String> consShedCustCol;
    @FXML    private TableColumn<TableRow, String> consSchedStartCol;
    @FXML    private TableColumn<TableRow, String> consSchedEnd;
    @FXML    private TableColumn<TableRow, String> apptTodayConsCol;
    @FXML    private TableColumn<TableRow, String> apptTodayCustCol;
    @FXML    private TableColumn<TableRow, String> apptTodayStartCol;
    @FXML    private TableColumn<TableRow, String> apptTodayEndCol;
    
    @FXML    private TableView<TableRow> apptTypeMonthTV;
    @FXML    private TableView<TableRow> consSchedTV;
    @FXML    private TableView<TableRow> apptTodayTV;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        apptTypeMonthTV.setItems(apptTypeMonth());
        rep1MonthCol.setCellValueFactory(new PropertyValueFactory("month"));
        rep1TypeCol.setCellValueFactory(new PropertyValueFactory("type"));
        rep1CountCol.setCellValueFactory(new PropertyValueFactory("count"));
        
        consSchedTV.setItems(consSchedAllTime());
        consSchedConsCol.setCellValueFactory(new PropertyValueFactory("cons"));
        consShedCustCol.setCellValueFactory(new PropertyValueFactory("cust"));
        consSchedStartCol.setCellValueFactory(new PropertyValueFactory("start"));
        consSchedEnd.setCellValueFactory(new PropertyValueFactory("end"));
        
        apptTodayTV.setItems(consSchedToday());
        apptTodayConsCol.setCellValueFactory(new PropertyValueFactory("cons"));
        apptTodayCustCol.setCellValueFactory(new PropertyValueFactory("cust"));
        apptTodayStartCol.setCellValueFactory(new PropertyValueFactory("start"));
        apptTodayEndCol.setCellValueFactory(new PropertyValueFactory("end"));
        
    }    

    @FXML
    private void reportCloseHandle(ActionEvent event) {
        Stage stage = (Stage) reportCloseButton.getScene().getWindow();
        stage.close();
    }
    
}
