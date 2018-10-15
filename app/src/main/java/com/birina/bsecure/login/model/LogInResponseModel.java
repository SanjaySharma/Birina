package com.birina.bsecure.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * FileName : ForgotPwdResponseModel
 * Description :  User Login - response
 * Dependencies :  Internet , Server
 */
public class LogInResponseModel {



    @SerializedName("response")
    private Integer response;
    @SerializedName("siNo")
    private String siNo;
    @SerializedName("productname")
    private String productName;
    @SerializedName("enddate")
    private String endDate;
    @SerializedName("username")
    private String userName;
    @SerializedName("useremail")
    private String userEmail;
    @SerializedName("usermobile")
    private String userMobile;
    @SerializedName("msg")
    private String msg;

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public String getSiNo() {
        return siNo;
    }

    public void setSiNo(String siNo) {
        this.siNo = siNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
