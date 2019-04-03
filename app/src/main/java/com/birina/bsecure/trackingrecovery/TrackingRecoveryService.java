package com.birina.bsecure.trackingrecovery;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import com.birina.bsecure.network.RestClient;
import com.birina.bsecure.remotescreaming.RemoteScreamingSmsReceiver;
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

    private final String TAG = "TrackingRecovery";
    private BroadcastReceiver mReceiver;
    @Override
    public void onCreate() {
        Log.d(TAG," onCreate of TrackingRecoveryService: ");
        super.onCreate();
        registerTrackingRecoveryReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        if(null != intent){

            String state = intent.getStringExtra(Constant.TRACKING_RECOVERY_ALARM_STATE);
            Log.d(Constant.TAG_TRACK, "TrackingRecoveryService Called ");

            switch (state) {
                case Constant.START_TRACKING_RECOVERY_ALARM:
                    sendSms();
                    sendLocationToCloude();
                    break;

                case Constant.STOP_TRACKING_RECOVERYALARM:
                    stopSelf();
                    break;
            }
        }
        return startId;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy()
    {
        this.unRegisterTrackingRecoveryReceiver();
    }

    private void registerTrackingRecoveryReceiver(){
        Log.d(TAG,"Enter in unRegisterTrackingRecoveryReceiver of  RemoteScreamingService ");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
        mReceiver = new TrackingRecoverySmsReceiver();
        registerReceiver(mReceiver, intentFilter);
        Log.d(TAG,"Exit from unRegisterTrackingRecoveryReceiver of  RemoteScreamingService");

    }

    private void unRegisterTrackingRecoveryReceiver(){
        Log.d(TAG,"Enter in unRegisterTrackingRecoveryReceiver of  RemoteScreamingService ");

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        Log.d(TAG,"Exit from unRegisterTrackingRecoveryReceiver of  RemoteScreamingService");

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
