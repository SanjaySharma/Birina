package com.birina.bsecure.restore;

import com.birina.bsecure.Base.BasePresenter;
import com.birina.bsecure.Base.BaseView;
import com.birina.bsecure.backup.model.Request;
import com.birina.bsecure.restore.model.RestoreRequest;

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
