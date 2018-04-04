package com.birina.bsecure.track.simalert;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.birina.bsecure.LockActivity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Admin on 2/17/2018.
 */

public class SaveMyAppsService extends Service {

    String CURRENT_PACKAGE_NAME = "com.whatsapp";
    String lastAppPN = "";
    boolean noDelay = false;
    public static SaveMyAppsService instance;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        scheduleMethod();
        CURRENT_PACKAGE_NAME = getApplicationContext().getPackageName();
        Log.e("Current PN", "" + CURRENT_PACKAGE_NAME);

        instance = this;

        return START_STICKY;
    }

    private void scheduleMethod() {
        // TODO Auto-generated method stub

        ScheduledExecutorService scheduler = Executors
                .newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                checkRunningApps();
                // This method will check for the Running apps after every 100ms
               /* if(30 minutes spent){
                    stop();
                }else{
                    checkRunningApps();
                }*/
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void checkRunningApps() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop = ar.topActivity.getPackageName();
        Log.e("activity on TOp", "" + activityOnTop);

        // Provide the packagename(s) of apps here, you want to show password activity
        if (activityOnTop.contains("whatsapp")  // you can make this check even better
                || activityOnTop.contains(CURRENT_PACKAGE_NAME)) {
            // Show Password Activity
            startActivity(new Intent(SaveMyAppsService.this, LockActivity.class));
        } else {
            // DO nothing
        }
    }

    public static void stop() {
        if (instance != null) {
            instance.stopSelf();
        }
    }
}
