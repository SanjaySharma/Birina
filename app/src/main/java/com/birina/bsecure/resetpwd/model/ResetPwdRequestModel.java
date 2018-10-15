package com.birina.bsecure.resetpwd.model;


/**
 * FileName : ForgotPwdRequestModel
 * Description :  User Login - request
 * Dependencies :  Internet , Server
 */

public class ResetPwdRequestModel {


    private String password;
    private String phone;
    private String deviceId;



    /**
     * siNo : abc12345
     * phone : 8109243584
     * 8920270491
     */

    public ResetPwdRequestModel(String password, String phone, String deviceId) {
        this.password = password;
        this.phone = phone;
        this.deviceId = deviceId;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
