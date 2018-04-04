package com.example.bsecure.backup;

import com.example.bsecure.Base.BasePresenter;
import com.example.bsecure.Base.BaseView;
import com.example.bsecure.backup.model.Request;

/**
 * Created by narendra on 27/02/18.
 */

public interface BackupContract {
    interface View extends BaseView<Presenter>{
        void onSuccess();
        void onFailure();
    }
    interface Presenter extends BasePresenter {
        void createBackup(Request request);

    }
}
