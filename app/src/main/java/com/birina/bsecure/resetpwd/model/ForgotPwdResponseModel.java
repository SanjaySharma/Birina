package com.birina.bsecure.resetpwd.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

/**
 * FileName : ForgotPwdResponseModel
 * Description :  User Login - response
 * Dependencies :  Internet , Server
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForgotPwdResponseModel {



    @SerializedName("response")
    private String response;
    @SerializedName("Otp")
    private String otp;
    @SerializedName("msg")
    private String msg;


    public ForgotPwdResponseModel(String response, String otp, String msg) {
        this.response = response;
        this.otp = otp;
        this.msg = msg;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
