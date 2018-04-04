package com.birina.bsecure.restore.model;

import com.birina.bsecure.backup.model.Request;

import java.util.List;

/**
 * Created by narendra on 28/02/18.
 */

public class BackupRequest {

    private String mobile;
    private List<com.birina.bsecure.backup.model.Request.ContactBean> contactList;
    private List<com.birina.bsecure.backup.model.Request.SmsBean> smsList;

    public List<com.birina.bsecure.backup.model.Request.SmsBean> getSmsList() {
        return smsList;
    }

    public void setSmsList(List<com.birina.bsecure.backup.model.Request.SmsBean> smsList) {
        this.smsList = smsList;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<com.birina.bsecure.backup.model.Request.ContactBean> getContactList() {
        return contactList;
    }

    public void setContactList(List<Request.ContactBean> contactList) {
        this.contactList = contactList;
    }
}
