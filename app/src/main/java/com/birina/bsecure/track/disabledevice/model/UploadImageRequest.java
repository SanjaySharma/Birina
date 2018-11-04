package com.birina.bsecure.track.disabledevice.model;

import com.google.gson.annotations.SerializedName;

public class UploadImageRequest {

    private String image;
    private String mobile;


    public UploadImageRequest(String image, String mobile) {
        this.image = image;
        this.mobile = mobile;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
