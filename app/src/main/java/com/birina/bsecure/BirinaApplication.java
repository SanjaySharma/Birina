package com.birina.bsecure;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.birina.bsecure.Base.BaseService;
import com.birina.bsecure.notification.localnotification.AlarmGenerator;
import com.birina.bsecure.util.BirinaPrefrence;

public class BirinaApplication extends Application implements Application.ActivityLifecycleCallbacks {


    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, BaseService.class));
        new AlarmGenerator().startAlarm(this, true);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d("BirinaActivity "," onCreate() called ");

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
}
