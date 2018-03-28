package com.example.birina.restore.model;

import com.example.birina.backup.model.Request;

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
