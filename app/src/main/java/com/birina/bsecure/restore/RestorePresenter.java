package com.example.bsecure.restore;

import com.example.bsecure.backup.model.Request;
import com.example.bsecure.restore.model.RestoreInteractorImp;
import com.example.bsecure.restore.model.RestoreIntractor;
import com.example.bsecure.restore.model.RestoreRequest;

/**
 * Created by narendra on 27/02/18.
 */

public class RestorePresenter implements RestoreContract.Presenter, RestoreIntractor.RestoreInteractorCallback {

    private RestoreContract.View mView;
    private RestoreIntractor mRInteractor;

    public RestorePresenter(RestoreContract.View view){
        mView=view;
        mRInteractor =new RestoreInteractorImp();
        mView.setPresenter(this);
    }

    @Override
    public void releaseMemory() {
        mView=null;
        mRInteractor =null;
    }

    @Override
    public void checkNetwork() {

    }

    @Override
    public void restoreBackup(RestoreRequest request) {
        if(mView!=null) {
            mView.showDialog();
            mRInteractor.restoreBackupOnServer(request, this);
        }
    }

    @Override
    public void onSuccessRestoreFromServer(Request backupContactAndSMS) {
        if(mView!=null) {
            mView.onSuccess( backupContactAndSMS);
        }
    }

    @Override
    public void onFailureRestoreFromServer() {
        if(mView!=null) {
            mView.hideDialog();
            mView.onFailure();
        }
    }
}
