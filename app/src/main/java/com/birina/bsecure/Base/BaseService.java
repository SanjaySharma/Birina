package com.birina.bsecure.Base;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birina.bsecure.pockettheft.ScreenReceiver;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;

public class BaseService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Log.d("onTaskRemoved ","  onTaskRemoved");
        BirinaPrefrence.updateTempLogInStatus( this,false);
        stopSelf();
    }


}
