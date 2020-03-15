/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Model.Appt;
import Model.TableRow;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author tyler
 */
public class ReportGen {
    
    private static final ObservableList<TableRow> apptTypePerMonthList = FXCollections.observableArrayList();
    private static final ObservableList<TableRow> consSchedListAllTime = FXCollections.observableArrayList();
    private static final ObservableList<TableRow> consSchedListToday = FXCollections.observableArrayList();
    
    public static ObservableList<TableRow> apptTypeMonth(){
        apptTypePerMonthList.clear();
        
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT MONTHNAME(start)as Month, type as Type, COUNT(type) as Count "
                    + "FROM appointment "
                    + "GROUP BY type, Month "
                    + "ORDER BY FIELD(Month, 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'), Type;";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                TableRow tr = new TableRow(rs.getString("Month"), rs.getString("Type"), rs.getInt("Count"));
                apptTypePerMonthList.add(tr);
                
            }
            stmt.close();
            return apptTypePerMonthList;
        }catch(SQLException se){
            System.out.println("apptTypeMonth() SQL Exception: "+se);
        }

        return null;
        
    }
    
    public static ObservableList<TableRow> consSchedAllTime(){
        consSchedListAllTime.clear();
        
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT contact, url, start, end "
                    + "FROM appointment "
                    + "ORDER BY start;";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                TableRow tr = new TableRow(rs.getString("contact"), rs.getString("url"), rs.getString("start"), rs.getString("end"));
                consSchedListAllTime.add(tr);
                
            }
            stmt.close();
            return consSchedListAllTime;
        }catch(SQLException se){
            System.out.println("consSched() SQL Exception: "+se);
        }
        
        return null;
    }
    
    public static ObservableList<TableRow> consSchedToday(){
        consSchedListToday.clear();
        
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT contact, url, start, end "
                    + "FROM appointment "
                    + "WHERE date(start)=CURDATE() "
                    + "ORDER BY start;";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                TableRow tr = new TableRow(rs.getString("contact"), rs.getString("url"), rs.getString("start"), rs.getString("end"));
                consSchedListToday.add(tr);
                
            }
            stmt.close();
            return consSchedListToday;
        }catch(SQLException se){
            System.out.println("consSched() SQL Exception: "+se);
        }
        
        return null;
    }
    
}
