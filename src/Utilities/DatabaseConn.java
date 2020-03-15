/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;
import java.sql.*;


/**
 *
 * @author Tyler.Edmonds
 */
/*
My DB info
Server name:  52.206.157.109 
Database name:  U04Mw3
Username:  U04Mw3
Password:  53688282563
*/
public class DatabaseConn {
    //connection information. If using a different db, just update these.
    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DBNAME = "U04Mw3";
    static final String URL = "jdbc:mysql://52.206.157.109/" + DBNAME;
    static final String USERNAME = "U04Mw3";
    static final String PASSWORD = "53688282563";
    private static Connection conn;
    
    public DatabaseConn(){}
    
    public static void dbConnect(){
        try{
            //conn = null;
            Class.forName(DRIVER);
            System.out.println("Opening connection...");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected");
        }catch(SQLException se){
            System.out.println("dbConnect() SQLException: " + se);
            se.printStackTrace();
        }catch(Exception e){
            System.out.println("dbConnect() Exception: " + e);
            e.printStackTrace();
        }
    }
    
    public static void dbDisconnect(){
        try{
            System.out.println("Disconnecting...");
            conn.close();
            System.out.println("Disconnected successfully.");
        }catch(SQLException se2){
            System.out.println("SQLException on Close: " + se2);
            se2.printStackTrace();
        }
    }
    
    public static Connection getDbConnection(){
        return conn;
    }
    
}
