/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 *
 * @author tyler
 */
public class Validator {
    
    public static boolean validatePhone(String phone){
        Scanner sc = new Scanner(phone);
        String phonePattern = "[0-9-]+";
        if(sc.hasNext(phonePattern)==true){
            return true;
        }
        sc.close();
        return false;
    }
    
    public static boolean validateText(String text){
        Scanner sc = new Scanner(text);
        String textPattern = "[a-zA-Z]+";
        if(sc.hasNext(textPattern)==true){
            return true;
        }
        sc.close();
        return false;
    }
    
    public static boolean validateAddr(String text){
        Scanner sc = new Scanner(text);
        String textPattern = "[a-zA-Z0-9-]+";
        if(sc.hasNext(textPattern)==true){
            return true;
        }
        sc.close();
        return false;
    }
    
    public static boolean validateDate(LocalDate date){
        //Scanner sc = new Scanner(text);
        LocalDate now = LocalDate.now().minusDays(1);
        if(date.isAfter(now)){
            System.out.println("true validateDate");
            return true;
        }else{
            System.out.println("false validateDate");
            return false;
        }
    }
    
    public static boolean validateStartEndTime(Object start, Object end) throws ParseException{
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String startString = start.toString();
        String endString = end.toString();
        Date start1 = dateFormat.parse(startString);
        Date end1 = dateFormat.parse(endString);
        if(start1.before(end1)||end1.after(start1)){
            System.out.println("validateStartEndTime True branch");
            return true;    
        }else{
            System.out.println("validateStartEndTime False branch");
            return false;
        }
    }
    
}
