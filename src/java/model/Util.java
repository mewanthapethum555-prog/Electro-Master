/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Mewantha
 */
public class Util {
    
    public static String generateCode;

    public static String generateCode() {
        int r = (int) (Math.random() * 1000000);
        return String.format("%06d", r);
    }
    
    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }
    
    public static boolean isPasswordValid(String password){
        return password.matches("^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$");
    }
    
    public static boolean isCodeValid(String code){
        return code.matches("^\\d{4,5}$");
    }
    
    public static boolean isInteger(String value){
        return value.matches("^\\d+$");
    }
    
    public static boolean isDouble(String text) {
        return text.matches("^\\d+(\\.\\d{2})?$");
    }
    
    public static boolean isMobileValid(String mobile){
        return mobile.matches("^[0]{1}[7]{1}[01245678]{1}[0-9]{7}$");
    }
}
