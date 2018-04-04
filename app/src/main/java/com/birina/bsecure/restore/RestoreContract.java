package com.example.bsecure.restore;

import com.example.bsecure.Base.BasePresenter;
import com.example.bsecure.Base.BaseView;
import com.example.bsecure.backup.model.Request;
import com.example.bsecure.restore.model.RestoreRequest;

/**
 * Created by narendra on 27/02/18.
 */

public interface RestoreContract {
    interface View extends BaseView<Presenter>{
        void onSuccess(Request backup);
        void onFailure();
    }
    interface Presenter extends BasePresenter {
        void restoreBackup(RestoreRequest request);
    }
}
