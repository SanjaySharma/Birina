package com.birina.bsecure.Base;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.birina.bsecure.track.disabledevice.SmsReceiver;
import com.birina.bsecure.trackingrecovery.TrackingRecoverySmsReceiver;
import com.birina.bsecure.unplugcharger.UnPlugChargerService;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;

public class WatchManService extends Service {

   private final String TAG = "WatchManService";

    private BroadcastReceiver mReceiver;
    @Override
    public void onCreate() {
        Log.d(TAG," onCreate of WatchManService: ");
        super.onCreate();
        registerRemoteLockReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        if(null != intent){

            String state = intent.getStringExtra(Constant.WATCH_MAN_STATE);
            Log.d(TAG, " onStartCommand WatchManService Called ");

            switch (state) {
                case Constant.STOP_WATCH_MAN_STATE:
                    stopSelf();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy()
    {
        this.unRegisterRemoteLockReceiver();
    }

    private void registerRemoteLockReceiver(){
        Log.d(TAG,"Enter in registerRemoteLockReceiver of  WatchManService ");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
        mReceiver = new SmsReceiver();
        registerReceiver(mReceiver, intentFilter);
        Log.d(TAG,"Exit from registerRemoteLockReceiver of  WatchManService");

    }

    private void unRegisterRemoteLockReceiver(){
        Log.d(TAG,"Enter in unRegisterRemoteLockReceiver of  WatchManService ");

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        Log.d(TAG,"Exit from unRegisterRemoteLockReceiver of  WatchManService");

    }


}
