package com.example.birina.network;

import com.example.birina.backup.model.BackupResponse;
import com.example.birina.backup.model.Request;
import com.example.birina.login.model.LogInRequestModel;
import com.example.birina.login.model.LogInResponseModel;
import com.example.birina.restore.model.RestoreRequest;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
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
    Observable<Response<LogInResponseModel>> performLogin( @Body LogInRequestModel logInRequestModel);

    @POST("backup.php")
    Call<BackupResponse> createBackUp(@Body Request user);

    @POST("restore-backup.php")
    Call<Request> restoreBackUp(@Body RestoreRequest restoreRequest);

}
