package com.birina.bsecure.util;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * FileName : Validation.java
 * Dependencies :
 * Description : Validation on views.
 * Classes : Validation.java
 */
public class Validation {

    static String regexMMDDYYYY = "^([1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/](19|20)\\d\\d$";
    static String regexDDMMYYYY = "^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$";
    static final String PASSWORD_PATTERN = "((?=.*[a-z@#$%0-9A-Z]).{6,20})";
    static String namePattern = "[a-zA-z]{2,25}+([ '-][a-zA-Z]+)*";
    static String mobilePtrn = "[0-9]{10,13}$";

    // return true if the input field is valid, based on the parameter passed
    public static boolean isFieldEmpty(EditText ed) {
        if (ed.getText() != null) {
            String uname = ed.getText().toString().trim();
            if (uname.equals("") || uname.length() <= 0)
                return true;
        }
        return false;
    }

    // Begins with 0, +91 or 0091
    public static boolean isMobileValid(String mobile) {
        if (Pattern.matches(mobilePtrn, mobile)) {
            if (mobile.equals("") && mobile.length() < 0) {
                return true;
            }
            return true;
        }

        return false;

    }
//  Check for numeric value
    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    // Regular Expression
    // you can change the expression based on your need
    public static boolean isEmailValid(String mail) {

        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE); // pattern=/^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;

        Matcher m = p.matcher(mail);
        if (m.matches() && mail.trim().length() > 0) {
            return true;
        }

        return false;
    }

    // Check the Confirm password
    public static boolean isPasswordMatch(EditText password, EditText c_password) {

        String pass = password.getText().toString().trim();
        String c_pass = c_password.getText().toString().trim();
        if (pass.equals(c_pass)) {
            return true;
        }
        return false;
    }

    public static boolean isAValidDDMMYYYYDate(EditText editTextDOB) {
        String dob = editTextDOB.getText().toString().trim();
        if (Pattern.matches(regexDDMMYYYY, dob)) {
            return true;
        }
        return false;
    }

    /*	Be between 8 and 40 characters
        Contain at least one digit.
        Contain at least one lower case character.
    */
    public static boolean isPasswordValid(EditText password) {
        String pass = password.getText().toString().trim();
        if (Pattern.matches(PASSWORD_PATTERN, pass)) {
            return true;
        }
        return false;

    }



    // Check validation for User Name
    public static boolean validateUserName(EditText firstName) {
        String Fname = firstName.getText().toString().trim();
        if (Pattern.matches(namePattern, Fname)) {
            return true;
        }
        return false;
    }




}