
package com.birina.bsecure.registration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationRequestModel {

    @SerializedName("email")
    private String email;
    @SerializedName("siNo")
    private String siNo;
    @SerializedName("phone")
    private String phone;
    @SerializedName("name")
    private String name;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("password")
    private String password;
    @SerializedName("dob")
    private String dob;
    @SerializedName("gender")
    private String gender;


    public RegistrationRequestModel(String email, String siNo, String phone, String name,
                                    String deviceId, String password, String dob, String gender) {
        this.email = email;
        this.siNo = siNo;
        this.phone = phone;
        this.name = name;
        this.deviceId = deviceId;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
