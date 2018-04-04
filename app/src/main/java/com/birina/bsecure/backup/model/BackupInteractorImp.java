package com.example.bsecure.backup.model;

import com.example.bsecure.network.RestClient;
import com.example.bsecure.network.ServerEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by narendra on 27/02/18.
 */

public class BackupInteractorImp implements BackupIntractor{

    @Override
    public void sendBackupOnServer(Request request, BackupInteractorCallback callback) {
        Call<BackupResponse> call=new RestClient().getApiService().createBackUp(request);
        call.enqueue(new Callback<BackupResponse>() {
            @Override
            public void onResponse(Call<BackupResponse> call, Response<BackupResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        BackupResponse backupResponse = response.body();
                        if (backupResponse != null) {
                            int statusCode = backupResponse.getBackup();
                            if (statusCode == ServerEntity.SUCCESS_CODE) {
                                callback.onSuccessBackupFromServer();
                            } else {
                                callback.onFailureBackupFromServer();
                            }
                        }
                    } else {
                        callback.onFailureBackupFromServer();
                    }
                } else {
                    callback.onFailureBackupFromServer();
                }
            }

            @Override
            public void onFailure(Call<BackupResponse> call, Throwable t) {
                callback.onFailureBackupFromServer();


            }
        });



    }





}
