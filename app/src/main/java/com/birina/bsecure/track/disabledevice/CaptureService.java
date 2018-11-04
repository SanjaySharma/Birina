/*
 * Copyright 2016 Keval Patel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.birina.bsecure.track.disabledevice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.birina.bsecure.R;
import com.birina.bsecure.login.LoginActivity;
import com.birina.bsecure.login.model.LogInRequestModel;
import com.birina.bsecure.login.model.LogInResponseModel;
import com.birina.bsecure.network.RestClient;
import com.birina.bsecure.track.camera.CameraConfig;
import com.birina.bsecure.track.camera.CameraError;
import com.birina.bsecure.track.camera.HiddenCameraService;
import com.birina.bsecure.track.camera.HiddenCameraUtils;
import com.birina.bsecure.track.camera.config.CameraFacing;
import com.birina.bsecure.track.camera.config.CameraFocus;
import com.birina.bsecure.track.camera.config.CameraImageFormat;
import com.birina.bsecure.track.camera.config.CameraResolution;
import com.birina.bsecure.track.disabledevice.model.UploadImageRequest;
import com.birina.bsecure.track.disabledevice.model.UploadImageResponse;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.BirinaUtility;
import com.birina.bsecure.util.Constant;


import java.io.ByteArrayOutputStream;
import java.io.File;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CaptureService extends HiddenCameraService {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {
                CameraConfig cameraConfig = new CameraConfig()
                        .getBuilder(this)
                        .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                        .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                        .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                        .setCameraFocus(CameraFocus.AUTO)
                        .build();

                startCamera(cameraConfig);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        takePicture();
                    }
                }, 2000L);
            } else {

                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
        } else {

            //TODO Ask your parent activity for providing runtime permission
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {
         sendImageToRemote(imageFile);

    }

    @Override
    public void onCameraError(@CameraError.CameraErrorCodes int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(this, R.string.error_cannot_open, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(this, R.string.error_cannot_write, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Toast.makeText(this, R.string.error_cannot_get_permission, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText(this, R.string.error_not_having_camera, Toast.LENGTH_LONG).show();
                break;
        }

        stopSelf();
    }


    private void sendImageToRemote(File file){

        Bitmap bm =  BirinaUtility.decodeSampledBitmapFromResource(CaptureService.this,
                BitmapFactory.decodeFile(file.getPath()), file.getPath());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);


        UploadImageRequest uploadImageRequest = new UploadImageRequest(encodedImage,
                BirinaPrefrence.getTrackingNumber(CaptureService.this));


        RestClient restClient = new RestClient();

        Observable<Response<UploadImageResponse>> imageResponsePayload
                = restClient.getApiService().uploadImage(uploadImageRequest);

        imageResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageResponse ->
                        {
                            if (null != imageResponse && imageResponse.isSuccessful() &&
                                    null != imageResponse.body()
                                    && imageResponse.body().getResponse() != Constant.LOGGED_FAILURE) {

                                try {
                                       bm.recycle();
                                       boolean deleted = file.delete();
                                       Log.e("ImageUpload "," is deleted: "+deleted);

                                    stopSelf();
                                }catch (Exception e){
                                    Log.e("","");
                                }

                            } else {
                                stopSelf();
                                throw new RuntimeException("Login Fail");
                            }
                        },
                        t -> {
                            stopSelf();
                        });


    }
}
