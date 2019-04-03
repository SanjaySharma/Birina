package com.birina.bsecure;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.birina.bsecure.Base.BaseService;
import com.birina.bsecure.Base.WatchManService;
import com.birina.bsecure.notification.localnotification.AlarmGenerator;
import com.birina.bsecure.trackingrecovery.TrackingRecoveryService;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;

public class BirinaApplication extends Application implements Application.ActivityLifecycleCallbacks {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BirinaActivity "," onCreate() called ");

       // startService(new Intent(this, BaseService.class));
      //  startWatchManService();
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
        stopWatchManService();
    }


    private void startWatchManService(){
        Log.d("BirinaApplication   ","Enter in startWatchManService: ");
        Intent i = new Intent(this, WatchManService.class);
        i.putExtra(Constant.WATCH_MAN_STATE, "");
        startService(i);
        Log.d("BirinaApplication   ","Exit from startWatchManService: ");
    }

    private void stopWatchManService(){
        Log.d("BirinaApplication   ","Enter in stopWatchManService: ");

        Intent i = new Intent(this, WatchManService.class);
        i.putExtra(Constant.WATCH_MAN_STATE, Constant.STOP_WATCH_MAN_STATE);
        startService(i);
        Log.d("BirinaApplication   ","Exit from stopWatchManService: ");

    }

}
