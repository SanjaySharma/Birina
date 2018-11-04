package com.birina.bsecure.trackingrecovery;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import com.birina.bsecure.network.RestClient;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Admin on 2/20/2018.
 */

public class TrackingRecoveryService extends Service {

    private final String TAG = TrackingRecoveryService.class.getName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        Log.d(Constant.TAG_TRACK, "TrackingRecoveryService Called ");

        try {

            sendSms();
            sendLocationToCloude();

        } catch (Exception e) {
            Log.e(TAG,"Execption:"+e);

        }
        return startId;
    }


    private void sendSms(){

        SmsManager smsMngr = SmsManager.getDefault();
        String destinationAddress = BirinaPrefrence.getTrackingRecoveryNumber(getApplicationContext());
        String scAddress = null;
        String text = "The Device location is: \n"+BirinaPrefrence.getLastLocation(this);
        PendingIntent sentIntent = null;
        PendingIntent deliveryIntent = null;
        smsMngr.sendTextMessage(destinationAddress, scAddress, text,
                sentIntent, deliveryIntent);
        System.out.println("-----SMS Send");
    }





    private void sendLocationToCloude() {

        RestClient restClient = new RestClient();
        Observable<Response<LocationResponseModel>> trackingRecoveryResponsePayload
                = restClient.getApiService().trackingRecovery(new LocationRequestModel(
                        BirinaPrefrence.getRegisteredNumber(TrackingRecoveryService.this),
                        BirinaPrefrence.getDeviceId(TrackingRecoveryService.this),
                        BirinaPrefrence.getSiNo(TrackingRecoveryService.this),
                        BirinaPrefrence.getLastLocation(TrackingRecoveryService.this),
                        BirinaPrefrence.getLongitude(TrackingRecoveryService.this),
                        BirinaPrefrence.getLatitude(TrackingRecoveryService.this)));

        trackingRecoveryResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(elabelResponse ->
                        {
                            stopSelf();
                        },
                        t -> {
                            Log.e(TAG,"Execption to send location in TrackingRecoveryService:"+t);
                            stopSelf();

                        });
    }
}
