package com.example.bsecure.backup.model;

/**
 * Created by narendra on 27/02/18.
 */

public interface BackupIntractor {

    void sendBackupOnServer(Request request, BackupInteractorCallback callback);

    interface BackupInteractorCallback{
        void onSuccessBackupFromServer();
        void onFailureBackupFromServer();
    }
}
