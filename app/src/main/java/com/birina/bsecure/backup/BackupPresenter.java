package com.birina.bsecure.backup;

import com.birina.bsecure.backup.model.BackupInteractorImp;
import com.birina.bsecure.backup.model.BackupIntractor;
import com.birina.bsecure.backup.model.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by narendra on 27/02/18.
 */

public class BackupPresenter implements BackupContract.Presenter, BackupIntractor.BackupInteractorCallback {

    private BackupContract.View mView;
    private BackupIntractor mBInteractor;

    BackupPresenter(BackupContract.View view){
        mView=view;
        mBInteractor=new BackupInteractorImp();
        mView.setPresenter(this);
    }

    @Override
    public void releaseMemory() {
        mView=null;
        mBInteractor=null;
    }

    @Override
    public void checkNetwork() {

    }

    @Override
    public void createBackup(Request request) {
        if(mView!=null) {

            mBInteractor.sendBackupOnServer(request, this);
        }
    }

    @Override
    public void onSuccessBackupFromServer() {
        if(mView!=null) {
            mView.hideDialog();
            mView.onSuccess();
        }
    }

    @Override
    public void onFailureBackupFromServer() {
        if(mView!=null) {
            mView.hideDialog();
            mView.onFailure();
        }
    }
}
