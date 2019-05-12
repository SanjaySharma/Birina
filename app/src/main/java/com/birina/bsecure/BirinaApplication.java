package com.birina.bsecure;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;

import com.birina.bsecure.Base.BaseService;
import com.birina.bsecure.Base.WatchManService;
import com.birina.bsecure.notification.localnotification.AlarmGenerator;
import com.birina.bsecure.track.disabledevice.LockBootReciver;
import com.birina.bsecure.track.disabledevice.SmsReceiver;
import com.birina.bsecure.trackingrecovery.TrackingRecoveryService;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static android.content.Intent.ACTION_LOCKED_BOOT_COMPLETED;
import static android.content.Intent.ACTION_REBOOT;

public class BirinaApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private final String TAG = "BirinaApplication";

    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BirinaActivity "," onCreate() called ");


        if ( Build.VERSION_CODES.O >= Build.VERSION.SDK_INT ) {
            registerRemoteLockReceiver();
        }

       //  startService(new Intent(this, BaseService.class));

        new AlarmGenerator().startAlarm(this, true);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d("BirinaActivity "," onActivityCreated() called ");

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d("BirinaActivity "," onResume() called ");

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d("BirinaActivity "," onActivityStopped() called ");

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d("BirinaApplication   ","onActivityDestroyed: ");
        BirinaPrefrence.updateTempLogInStatus(this, false);
    }



    private void registerRemoteLockReceiver(){
        Log.d(TAG,"Enter in registerRemoteLockReceiver of  BirinaApplication ");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
        mReceiver = new SmsReceiver();
        registerReceiver(mReceiver, intentFilter);
        Log.d(TAG,"Exit from registerRemoteLockReceiver of  BirinaApplication");

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
