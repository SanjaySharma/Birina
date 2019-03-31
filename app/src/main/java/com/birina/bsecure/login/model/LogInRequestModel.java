package com.birina.bsecure.login.model;


import com.google.gson.annotations.SerializedName;

/**
 * FileName : ForgotPwdRequestModel
 * Description :  User Login - request
 * Dependencies :  Internet , Server
 */

public class LogInRequestModel {
    @SerializedName("password")
    private String pwd;
    private String phone;
    private String deviceId;


    /**
     * siNo : abc12345
     * phone : 8109243584
     * 8920270491
     */


    public LogInRequestModel(String pwd, String phone, String deviceId) {
        this.pwd = pwd;
        this.phone = phone;
        this.deviceId = deviceId;
    }


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
