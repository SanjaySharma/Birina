package com.example.bsecure.restore.model;

import com.example.bsecure.backup.model.Request;

/**
 * Created by narendra on 27/02/18.
 */

public interface RestoreIntractor {

    void restoreBackupOnServer(RestoreRequest request, RestoreInteractorCallback callback);

    interface RestoreInteractorCallback {
        void onSuccessRestoreFromServer(Request backupContactAndSMS);
        void onFailureRestoreFromServer();
    }
}
