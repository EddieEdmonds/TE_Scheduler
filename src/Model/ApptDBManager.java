/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import static Model.UserDBManager.getCurrentUser;
import Utilities.DatabaseConn;
import Views.MainScreenController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.util.converter.LocalDateTimeStringConverter;

/**
 *
 * @author tyler
 */
public class ApptDBManager {
    
    String lastUpdatedByString;
    
    private static final ObservableList<Appt> allAppts = FXCollections.observableArrayList();
    private static final ObservableList<Appt> weekAppts = FXCollections.observableArrayList();
    private static final ObservableList<Appt> monthAppts = FXCollections.observableArrayList();
    private static final ObservableList<String> cityList = FXCollections.observableArrayList();
    
    public static ObservableList<Appt> getAllAppts() throws SQLException{
        //LocalDateTime now = LocalDateTime.now();
                
        allAppts.clear();
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT a.*, c.customerName "
                    + "FROM appointment a INNER JOIN customer c ON a.customerId = c.customerId "
                    + "ORDER BY a.start";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                Appt appt = new Appt(
                        rs.getString("customerName"), rs.getString("contact"), rs.getString("type"),
                        rs.getString("start"), rs.getString("end"), rs.getString("title"), rs.getString("description"), 
                        rs.getString("location"), rs.getInt("customerId"), rs.getInt("userId"));
                allAppts.add(appt);
                
            }
            stmt.close();
            return allAppts;
        }catch(SQLException se){
            System.out.println("getAllAppts() SQL Exception: "+se);
            return null;
        }
        //return allAppts;
    }
    
    public static ObservableList<Appt> getWeekAppts() throws SQLException{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        ZoneId zid = ZoneId.systemDefault();
        dtf.format(now);
        LocalDateTime end = LocalDateTime.now().plusWeeks(1);
        dtf.format(end);
        weekAppts.clear();
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            
            //BETWEENT STATEMENT to get appointments between now and 1 week in the future. 
            String sqlQuery = "SELECT a.*, c.customerName FROM appointment a INNER JOIN customer c ON a.customerId = c.customerId "
                    + "WHERE a.start BETWEEN '"+now+"' AND '"+end+"'";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                //Collects start and end times from DB. 
                Timestamp tsStart = rs.getTimestamp("start");
                Timestamp tsEnd = rs.getTimestamp("end");
                
                //Converts the times from the DB to UTC. 
                ZonedDateTime zdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime zdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                
                //Converts times from UTC to local time zone,
                ZonedDateTime localZdtStart = zdtStart.withZoneSameInstant(zid);
                ZonedDateTime localZdtEnd = zdtEnd.withZoneSameInstant(zid);
                
                //Strips the extra UTC information from the end of the times to make them look a bit prettier. 
                LocalDateTime localStart;
                LocalDateTime localEnd;
                localStart = localZdtStart.toLocalDateTime();
                localEnd = localZdtEnd.toLocalDateTime();
                
                //Formats the time as a string and removes the T between date and time for a cleaner look. 
                String localStartString = localStart.toString().replace("T", " ");
                String localEndString = localEnd.toString().replace("T", " ");
                
                //New way. Using zoneDateTime of end and start with toString() to add that to the weekAppts observable list. 
                Appt appt = new Appt(
                    rs.getInt("appointmentId"), rs.getString("customerName"), rs.getString("contact"), rs.getString("type"),
                    localStartString, localEndString, rs.getString("title"), rs.getString("description"), 
                    rs.getString("location"), rs.getInt("customerId"), rs.getInt("userId"));
//                
                weekAppts.add(appt);
            }
            stmt.close();
            return weekAppts;
        }catch(SQLException se){
            String errorMessage = se.getMessage();
            Logger.getLogger(ApptDBManager.class.getName()).log(Level.SEVERE, null, se);
            System.out.println("getWeekAppts() SQL Exception:" + errorMessage);
            return null;
        }
    }
    
    public static ObservableList<Appt> getMonthAppts() throws SQLException{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        ZoneId zid = ZoneId.systemDefault();
        dtf.format(now);
        LocalDateTime end = LocalDateTime.now().plusMonths(1).plusDays(1);
        dtf.format(end);
        monthAppts.clear();
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT a.*, c.customerName FROM appointment a INNER JOIN customer c ON a.customerId = c.customerId "
                    + "WHERE a.start BETWEEN '"+now+"' AND '"+end+"'";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                 //Collects start and end times from DB. 
                Timestamp tsStart = rs.getTimestamp("start");
                Timestamp tsEnd = rs.getTimestamp("end");
                
                //Converts the times from the DB to UTC. 
                ZonedDateTime zdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime zdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                
                //Converts times from UTC to local time zone,
                ZonedDateTime localZdtStart = zdtStart.withZoneSameInstant(zid);
                ZonedDateTime localZdtEnd = zdtEnd.withZoneSameInstant(zid);
                
                //Strips the extra UTC information from the end of the times to make them look a bit prettier. 
                LocalDateTime localStart;
                LocalDateTime localEnd;
                localStart = localZdtStart.toLocalDateTime();
                localEnd = localZdtEnd.toLocalDateTime();
                
                //Formats the time as a string and removes the T between date and time for a cleaner look. 
                String localStartString = localStart.toString().replace("T", " ");
                String localEndString = localEnd.toString().replace("T", " ");
                
                
                Appt appt = new Appt(
                    rs.getInt("appointmentId"), rs.getString("customerName"), rs.getString("contact"), rs.getString("type"),
                    localStartString, localEndString, rs.getString("title"), rs.getString("description"), 
                    rs.getString("location"), rs.getInt("customerId"), rs.getInt("userId"));
                monthAppts.add(appt);
            }
            stmt.close();
            return monthAppts;
        }catch(SQLException se){
            System.out.println("getMonthAppts() SQL Exception: "+se);
            return null;
        }
        //return allAppts;
    }
    
    String printName(){
        return lastUpdatedByString;
    }
    
    public static boolean saveAppt(String customer, String consultant, LocalDate date, String startTime, String endTime, String location, String type, String desc) throws SQLException{
        
        int customerId=0;
        int userId=0;
        
        String title = "Appointment: " + type;
        
        
        
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dtStart = date +" "+ startTime;
        String dtEnd = date +" "+endTime;
        
        
        LocalDateTime ldtStart = LocalDateTime.parse(dtStart, df);
        LocalDateTime ldtEnd = LocalDateTime.parse(dtEnd, df);
        
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdtStart = ldtStart.atZone(zid);
        ZonedDateTime zdtEnd = ldtEnd.atZone(zid);
        
        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("UTC: " + utcStart +" *** "+utcEnd);
        
        LocalDateTime ltdStart2 = utcStart.toLocalDateTime();
        System.out.println("To local date time: " + ltdStart2);
        LocalDateTime ltdEnd2 = utcEnd.toLocalDateTime();
        System.out.println("To local date time: " + ltdEnd2);
        
        Timestamp sqlStart = Timestamp.valueOf(ltdStart2);
        Timestamp sqlEnd = Timestamp.valueOf(ltdEnd2);
        
        if(checkOverlapAppt(sqlStart, sqlEnd)==true){
            System.out.println("RS is empty. No appt with this start time: "+sqlStart);
        }else{
            System.out.println("RS is not empty. There is an appt with this time: "+sqlStart);
        }
        
        
        try {
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlGetCustId = "SELECT customerId FROM customer WHERE customerName = '"+customer+"'";
            ResultSet rs = stmt.executeQuery(sqlGetCustId);
            while(rs.next()){
                customerId = rs.getInt("customerId");
            }
            
            String sqlGetUserId = "SELECT userId FROM user WHERE userName = '"+consultant+"'";
            ResultSet rs2 = stmt.executeQuery(sqlGetUserId);
            while(rs2.next()){
                userId = rs2.getInt("userId");
            }
            stmt.close();
            
        } catch (SQLException sqlex) {
            Logger.getLogger(ApptDBManager.class.getName()).log(Level.SEVERE, null, sqlex);
        }
        
        try{
            User user = getCurrentUser();
 
            String createdBy = user.getUsername();
            String lastUpdateBy = user.getUsername();

            DatabaseConn.getDbConnection().setAutoCommit(false);
            
            String sqlOne = "INSERT INTO appointment (customerId, userId, start, end, location, type, description, title, contact, url, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,now(),?,now(),?);";
            
            try (PreparedStatement ps1 = DatabaseConn.getDbConnection().prepareStatement(sqlOne)) {
                ps1.setInt(1, customerId);
                ps1.setInt(2, userId);
                ps1.setTimestamp(3, sqlStart); //converted to UTC and then the time zone is stripped in order to store it in DB.
                ps1.setTimestamp(4, sqlEnd);
                ps1.setString(5, location);
                ps1.setString(6, type);
                ps1.setString(7, desc);
                ps1.setString(8, title);
                ps1.setString(9, consultant);
                ps1.setString(10, customer);
                ps1.setString(11, createdBy);
                ps1.setString(12, lastUpdateBy);
                
                ps1.execute();
            }
            
            DatabaseConn.getDbConnection().commit();
            
            ApptDBManager.getAllAppts();
            
        }catch(SQLException sqlex){
            DatabaseConn.getDbConnection().rollback();
            Logger.getLogger(ApptDBManager.class.getName()).log(Level.SEVERE, null, sqlex);
        }
        
        
        return false;
        
    }
    
    public static ObservableList<String> getCities(){
        cityList.clear();
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
    
    public static boolean modAppt(int apptId, String customer, String consultant, LocalDate date, String startTime, String endTime, String location, String type, String desc) throws SQLException{
        
        int customerId=0;
        int userId=0;
        
        String title = "Appointment: " + type;
        
        
        
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dtStart = date +" "+ startTime;
        String dtEnd = date +" "+endTime;
        
        
        LocalDateTime ldtStart = LocalDateTime.parse(dtStart, df);
        LocalDateTime ldtEnd = LocalDateTime.parse(dtEnd, df);
        
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdtStart = ldtStart.atZone(zid);
        ZonedDateTime zdtEnd = ldtEnd.atZone(zid);
        
        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("UTC: " + utcStart +" *** "+utcEnd);
        
        LocalDateTime ltdStart2 = utcStart.toLocalDateTime();
        System.out.println("To local date time: " + ltdStart2);
        LocalDateTime ltdEnd2 = utcEnd.toLocalDateTime();
        System.out.println("To local date time: " + ltdEnd2);
        
        Timestamp sqlStart = Timestamp.valueOf(ltdStart2);
        Timestamp sqlEnd = Timestamp.valueOf(ltdEnd2);
        
        String startNoMili = sqlStart.toString().substring(0, 19);
        String endNoMili = sqlStart.toString().substring(0, 19);
        
        try {
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlGetCustId = "SELECT customerId FROM customer WHERE customerName = '"+customer+"'";
            ResultSet rs = stmt.executeQuery(sqlGetCustId);
            while(rs.next()){
                customerId = rs.getInt("customerId");
            }
            
            String sqlGetUserId = "SELECT userId FROM user WHERE userName = '"+consultant+"'";
            ResultSet rs2 = stmt.executeQuery(sqlGetUserId);
            while(rs2.next()){
                userId = rs2.getInt("userId");
            }
            stmt.close();
            
        } catch (SQLException sqlex) {
            Logger.getLogger(ApptDBManager.class.getName()).log(Level.SEVERE, null, sqlex);
        }
        
        if(checkOverlapAppt(sqlStart, sqlEnd)==true){
            System.out.println("RS is empty. No appt overlap: "+sqlStart+" "+sqlEnd+" Good to go.");
            try{
                User user = getCurrentUser();
                String lastUpdateBy = user.getUsername();

                DatabaseConn.getDbConnection().setAutoCommit(false);
                String sqlUpdate = "UPDATE appointment "
                        + "SET start=?, end=?, location=?, type=?, description=?, "
                        + "title=?, contact=?, url=?, lastUpdate=now(), lastUpdateBy=? "
                        + "WHERE appointmentId=?;"; 

                PreparedStatement ps1 = DatabaseConn.getDbConnection().prepareStatement(sqlUpdate);
                ps1.setTimestamp(1, sqlStart);
                ps1.setTimestamp(2, sqlEnd);
                ps1.setString(3, location);
                ps1.setString(4, type);
                ps1.setString(5, desc);
                ps1.setString(6, title);
                ps1.setString(7, consultant);
                ps1.setString(8, customer);
                ps1.setString(9, lastUpdateBy);
                ps1.setInt(10, apptId);
                ps1.execute();   
                ps1.close();

                DatabaseConn.getDbConnection().commit();

                System.out.println("Appointment Id: "+apptId);
                return true;

            }catch(Exception sqlex){
                DatabaseConn.getDbConnection().rollback();
                Logger.getLogger(ApptDBManager.class.getName()).log(Level.SEVERE, null, sqlex); 
            }
        }else{
            System.out.println("RS is not empty. There is overlap with a scheduled appt: "+sqlStart+" "+sqlEnd);

        }
        
        
        
        return false;
    }
    
    public static boolean checkOverlapAppt(Timestamp startTime, Timestamp endTime) throws SQLException{
        try{
            Statement stmt = DatabaseConn.getDbConnection().createStatement();
            String sqlQuery = "SELECT start, end "
                    + "FROM appointment";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            
            System.out.println("startTime: "+startTime+" and endTime: "+endTime);
            
            
            while(rs.next()){
                Timestamp rsStart = rs.getTimestamp("start");
                Timestamp rsEnd = rs.getTimestamp("end");
                System.out.println("RS start: "+rsStart+" RS end: "+rsEnd);
                if(startTime.after(rsStart) && startTime.before(rsEnd)){
                    return false;
                }else if(endTime.after(rsStart)&&endTime.before(rsEnd)){
                    return false;
                }else if(startTime.equals(rsStart)){
                    System.out.println("Start matches another start.");
                    return false;
                }else if(endTime.equals(rsEnd)){
                    System.out.println("End matches another end.");
                    return false;
                }else if(rsStart.after(startTime)&&rsEnd.before(endTime)){
                    System.out.println("Appt within this time frame");
                    return false;
                }
                                
            }
            stmt.close();
            
            
        }
        catch(SQLException se){
            System.out.println("checkOverlapAppt() SQL Error: " + se.getMessage());
        }
        System.out.println("Outside true path of checkOverlapAppt. Nothing failed checks.");
        return true;
        
    }
    
    public static boolean deleteAppt(int apptId) throws SQLException{
        try{
            DatabaseConn.getDbConnection().setAutoCommit(false);

            //Deleting appointments from customer table where appointmentId = the ID we pass into this function. 
            String sqlDeleteCustomer = "DELETE FROM appointment "
                    + "WHERE appointmentId = ?";
            PreparedStatement ps2 = DatabaseConn.getDbConnection().prepareStatement(sqlDeleteCustomer);
            ps2.setInt(1, apptId);
            ps2.executeUpdate();
            ps2.close();
            
            DatabaseConn.getDbConnection().commit();
            
        }catch(SQLException se){
            DatabaseConn.getDbConnection().rollback();
            System.out.println("Failure, query rolled back. SQL Error: " + se.getMessage());
            
        }
        
        
        
        return true;
    }
    
    public static boolean apptReminder(){
        
//        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
//        Timestamp now15 = Timestamp.valueOf(LocalDateTime.now().plusMinutes(15));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now15 = LocalDateTime.now();
        
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdtNow = now.atZone(zid);
        ZonedDateTime zdtNow15 = now15.atZone(zid);
        
        ZonedDateTime utcNow = zdtNow.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcNow15 = zdtNow15.withZoneSameInstant(ZoneId.of("UTC"));
        
        LocalDateTime lNow = utcNow.toLocalDateTime();
        LocalDateTime lNow15 = utcNow15.toLocalDateTime().plusMinutes(15);
        
        Timestamp tsNow = Timestamp.valueOf(lNow);
        Timestamp tsNow15 = Timestamp.valueOf(lNow15);
        
        System.out.println("Now: "+tsNow+" Now15: "+tsNow15);
        
        try (Statement stmt = DatabaseConn.getDbConnection().createStatement()) {
            String sqlQuery = "SELECT start FROM appointment";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()){
                System.out.println(rs.getTimestamp("start"));
                if(rs.getTimestamp("start").after(tsNow) && rs.getTimestamp("start").before(tsNow15)){
                    System.out.println("apptReminder true");
                    return true;
                }else{
                    System.out.println("apptReminder false");
                } 
            }
        }catch(SQLException se){
            Logger.getLogger(ApptDBManager.class.getName()).log(Level.SEVERE, null, se); 
            System.out.println("ApptReminder() SQL Error: " + se);

        }
        
        return false;
    }
    

    
    
}
