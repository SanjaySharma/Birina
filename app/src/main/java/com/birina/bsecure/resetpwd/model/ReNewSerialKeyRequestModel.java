package com.birina.bsecure.resetpwd.model;


/**
 * FileName : ForgotPwdRequestModel
 * Description :  User Login - request
 * Dependencies :  Internet , Server
 */

public class ReNewSerialKeyRequestModel {


    private String mobile;
    private String siNo;
    private String deviceId;


    public ReNewSerialKeyRequestModel(String mobile, String siNo, String deviceId) {
        this.mobile = mobile;
        this.siNo = siNo;
        this.deviceId = deviceId;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSiNo() {
        return siNo;
    }

    public void setSiNo(String siNo) {
        this.siNo = siNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
