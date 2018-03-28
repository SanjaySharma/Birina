package com.example.birina.restore;

import com.example.birina.Base.BasePresenter;
import com.example.birina.Base.BaseView;
import com.example.birina.backup.model.Request;
import com.example.birina.restore.model.RestoreRequest;

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
