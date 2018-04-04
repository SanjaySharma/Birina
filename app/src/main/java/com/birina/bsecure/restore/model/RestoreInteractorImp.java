package com.example.bsecure.restore.model;

import com.example.bsecure.backup.model.Request;
import com.example.bsecure.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by narendra on 27/02/18.
 */

public class RestoreInteractorImp implements RestoreIntractor {

    @Override
    public void restoreBackupOnServer(RestoreRequest request, RestoreInteractorCallback callback) {
        Call<Request> call=new RestClient().getApiService().restoreBackUp(request);
        call.enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        Request backupResponse = response.body();
                        if (backupResponse != null) {
                                callback.onSuccessRestoreFromServer(backupResponse);
                        }
                    } else {
                        callback.onFailureRestoreFromServer();
                    }
                } else {
                    callback.onFailureRestoreFromServer();
                }
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                callback.onFailureRestoreFromServer();
            }
        });
    }
}
