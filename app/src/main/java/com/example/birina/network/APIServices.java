package com.example.birina.network;

import com.example.birina.login.model.LogInRequestModel;
import com.example.birina.login.model.LogInResponseModel;


import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 1244582 on 12/5/2016.
 */

    /*login --------------*/
public interface APIServices {

    @Headers("Content-Type:application/json")
    @POST("activation.php")
    Observable<Response<LogInResponseModel>> performLogin( @Body LogInRequestModel logInRequestModel);



}
