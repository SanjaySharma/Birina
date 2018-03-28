package com.example.birina.restore.model;

import com.example.birina.backup.model.Request;

import java.util.List;

/**
 * Created by narendra on 28/02/18.
 */

public class BackupRequest {

    private String mobile;
    private List<com.example.birina.backup.model.Request.ContactBean> contactList;
    private List<com.example.birina.backup.model.Request.SmsBean> smsList;

    public List<com.example.birina.backup.model.Request.SmsBean> getSmsList() {
        return smsList;
    }

    public void setSmsList(List<com.example.birina.backup.model.Request.SmsBean> smsList) {
        this.smsList = smsList;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<com.example.birina.backup.model.Request.ContactBean> getContactList() {
        return contactList;
    }

    public void setContactList(List<Request.ContactBean> contactList) {
        this.contactList = contactList;
    }
}
