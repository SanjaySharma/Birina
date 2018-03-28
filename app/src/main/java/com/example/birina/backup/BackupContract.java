package com.example.birina.backup;

import com.example.birina.Base.BasePresenter;
import com.example.birina.Base.BaseView;
import com.example.birina.backup.model.Request;

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
