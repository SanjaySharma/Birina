package com.birina.bsecure.resetpwd.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

/**
 * FileName : ForgotPwdRequestModel
 * Description :  User Login - request
 * Dependencies :  Internet , Server
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForgotPwdRequestModel {

    @SerializedName("Mobile")
    private String mobile;
    @SerializedName("password")
    private String password;
    @SerializedName("otp")
    private String otp;
    @SerializedName("sino")
    private String siNo;


    public ForgotPwdRequestModel(String siNo) {
        this.siNo = siNo;
    }

    public ForgotPwdRequestModel(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public ForgotPwdRequestModel(String mobile, String otp, String siNo) {
        this.mobile = mobile;
        this.otp = otp;
        this.siNo = siNo;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getSiNo() {
        return siNo;
    }

    public void setSiNo(String siNo) {
        this.siNo = siNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
