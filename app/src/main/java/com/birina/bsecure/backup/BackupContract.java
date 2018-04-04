package com.birina.bsecure.backup;

import com.birina.bsecure.Base.BasePresenter;
import com.birina.bsecure.Base.BaseView;
import com.birina.bsecure.backup.model.Request;

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
