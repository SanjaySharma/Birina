package com.example.birina.login.model;


/**
 * FileName : LogInRequestModel
 * Description :  User Login - request
 * Dependencies :  Internet , Server
 */

public class LogInRequestModel {


    private String email;
    private String siNo;
    private String phone;


    /**
     * siNo : abc1235
     * phone : 8109243584
     */


    public LogInRequestModel(String siNo, String phone, String email) {
        this.email = email;
        this.siNo = siNo;
        this.phone = phone;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSiNo() {
        return siNo;
    }

    public void setSiNo(String siNo) {
        this.siNo = siNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
