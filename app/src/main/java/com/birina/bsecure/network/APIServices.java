package com.birina.bsecure.network;

import com.birina.bsecure.backup.model.BackupResponse;
import com.birina.bsecure.backup.model.Request;
import com.birina.bsecure.login.model.LogInRequestModel;
import com.birina.bsecure.login.model.LogInResponseModel;
import com.birina.bsecure.notification.TokenRegistrationRequest;
import com.birina.bsecure.notification.TokenRegistrationResponse;
import com.birina.bsecure.registration.model.RegistrationRequestModel;
import com.birina.bsecure.registration.model.RegistrationResponseModel;
import com.birina.bsecure.resetpwd.model.ForgotPwdRequestModel;
import com.birina.bsecure.resetpwd.model.ForgotPwdResponseModel;
import com.birina.bsecure.resetpwd.model.ReNewSerialKeyRequestModel;
import com.birina.bsecure.resetpwd.model.ReNewSerialKeyResponseModel;
import com.birina.bsecure.restore.model.RestoreRequest;
import com.birina.bsecure.track.disabledevice.model.UploadImageRequest;
import com.birina.bsecure.track.disabledevice.model.UploadImageResponse;
import com.birina.bsecure.trackingrecovery.LocationRequestModel;
import com.birina.bsecure.trackingrecovery.LocationResponseModel;


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
    @POST("applogin.php")
    Observable<Response<LogInResponseModel>> performLogin(@Body LogInRequestModel logInRequestModel);

    @POST("backup.php")
    Call<BackupResponse> createBackUp(@Body Request user);

    @POST("restore-backup.php")
    Call<Request> restoreBackUp(@Body RestoreRequest restoreRequest);

    @Headers("Content-Type:application/json")
    @POST("pwd-recovery.php")
    Observable<Response<ForgotPwdResponseModel>> performPwdRecovery(@Body ForgotPwdRequestModel
                                                                                forgotPwdRequestModel );
    @Headers("Content-Type:application/json")
    @POST("varify-otp.php")
    Observable<Response<ForgotPwdResponseModel>> performVerifyOtp(@Body ForgotPwdRequestModel
                                                                             forgotPwdRequestModel );

    @Headers("Content-Type:application/json")
    @POST("reset-pwd.php")
    Observable<Response<ForgotPwdResponseModel>> performResetPwd(@Body ForgotPwdRequestModel
                                                                             forgotPwdRequestModel );

    @Headers("Content-Type:application/json")
    @POST("renew.php")
    Observable<Response<ReNewSerialKeyResponseModel>> performRenewKey(@Body ReNewSerialKeyRequestModel
                                                                            reNewSerialKeyRequestModel );
    @Headers("Content-Type:application/json")
    @POST("newuser.php")
    Observable<Response<RegistrationResponseModel>> performRegistration(@Body RegistrationRequestModel
                                                                            registrationRequestModel );

    @Headers("Content-Type:application/json")
    @POST("track_device.php")
    Observable<Response<LocationResponseModel>> trackingRecovery(@Body LocationRequestModel
                                                                            locationRequestModel );


    @Headers("Content-Type:application/json")
    @POST("save_token.php")
    Observable<Response<TokenRegistrationResponse>> registrationToken(@Body TokenRegistrationRequest
                                                                            tokenRegistrationRequest );

    @Headers("Content-Type:application/json")
    @POST("take_pic.php")
    Observable<Response<UploadImageResponse>> uploadImage(@Body UploadImageRequest uploadImageRequest);
}
