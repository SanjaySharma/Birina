package com.birina.bsecure.network;

import com.birina.bsecure.backup.model.BackupResponse;
import com.birina.bsecure.backup.model.Request;
import com.birina.bsecure.login.model.LogInRequestModel;
import com.birina.bsecure.login.model.LogInResponseModel;
import com.birina.bsecure.restore.model.RestoreRequest;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 1244582 on 12/5/2016.
 */

    /*login --------------*/
public interface APIServices {

    @Headers("Content-Type:application/json")
    @POST("activation.php")
    Observable<Response<LogInResponseModel>> performLogin(@Body LogInRequestModel logInRequestModel);

    @POST("backup.php")
    Call<BackupResponse> createBackUp(@Body Request user);

    @POST("restore-backup.php")
    Call<Request> restoreBackUp(@Body RestoreRequest restoreRequest);

}
