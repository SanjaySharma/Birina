package com.birina.bsecure.resetpwd.model;

/**
 * FileName : ForgotPwdResponseModel
 * Description :  User Login - response
 * Dependencies :  Internet , Server
 */
public class ReNewSerialKeyResponseModel {




    private String response;
    private String enddate;
    private String msg;



    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
