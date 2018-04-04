package com.example.bsecure.backup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 3/21/2018.
 */

public class BackupResponse {

    @SerializedName("backup")
    private Integer backup;

    public Integer getBackup() {
        return backup;
    }

    public void setBackup(Integer backup) {
        this.backup = backup;
    }
}
