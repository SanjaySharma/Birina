package com.birina.bsecure.trackingrecovery;

import com.google.gson.annotations.SerializedName;

/**
 * FileName : ForgotPwdResponseModel
 * Description :  User Login - response
 * Dependencies :  Internet , Server
 */
public class LocationResponseModel {



    @SerializedName("response")
    private Integer response;


    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }


}
