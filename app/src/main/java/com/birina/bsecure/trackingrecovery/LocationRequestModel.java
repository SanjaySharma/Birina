package com.birina.bsecure.trackingrecovery;


/**
 * FileName : ForgotPwdRequestModel
 * Description :  User Login - request
 * Dependencies :  Internet , Server
 */

public class LocationRequestModel {

    private String mobile;
    private String deviceId;
    private String siNo;
    private String address;
    private String longt;
    private String lat;


    public LocationRequestModel(String mobile, String deviceId, String siNo, String address, String longt, String lat) {
        this.mobile = mobile;
        this.deviceId = deviceId;
        this.siNo = siNo;
        this.address = address;
        this.longt = longt;
        this.lat = lat;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSiNo() {
        return siNo;
    }

    public void setSiNo(String siNo) {
        this.siNo = siNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
