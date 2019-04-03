package com.birina.bsecure.unplugcharger;

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
import android.widget.Toast;

import com.birina.bsecure.R;
import com.birina.bsecure.pockettheft.ScreenReceiver;
import com.birina.bsecure.util.Constant;

public class UnPlugChargerService extends Service {
    private final String TAG = "UnPlugCharger";
   private BroadcastReceiver mReceiver;
   private MediaPlayer player;
    @Override
    public void onCreate() {
        Log.d(TAG," onCreate of UnPlugChargerService: ");

        super.onCreate();
        registerScreenReceiver();
    }

    @Override
    public void onStart(Intent intent, int startId) {

        if(null != intent){

        String screenState = intent.getStringExtra(Constant.UNPLUG_ALARM_STATE);
            Log.d(TAG,"Enter in onStart of UnPlugChargerService screenState is: "+screenState);

        switch (screenState)
        {
            case Constant.START_UNPLUG_ALARM:
                if(player != null){
                    if(!player.isPlaying()){
                        startAlarm();
                    }
                }else{
                    startAlarm();
                }
                break;

            case Constant.UNREGISTER:
                stopAlarm();
                stopSelf();
                break;
            case Constant.STOP_UNPLUG_ALARM:
                stopAlarm();
                break;
        }
        }
        Log.d(TAG,"Exit from in onStart of UnPlugChargerService");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy()
    {
        this.unRegisterScreenReceiver();
    }

    private void registerScreenReceiver(){
        Log.d(TAG,"Enter in registerScreenReceiver of  UnPlugChargerService ");

        // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        mReceiver = new PlugInReceiver();
        registerReceiver(mReceiver, filter);
        Log.d(TAG,"Exit from registerScreenReceiver of  UnPlugChargerService");

    }

    private void unRegisterScreenReceiver(){
        Log.d(TAG,"Enter in unRegisterScreenReceiver of  UnPlugChargerService ");

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        Log.d(TAG,"Exit from unRegisterScreenReceiver of  UnPlugChargerService");

    }


    private void startAlarm(){
        Log.d(TAG,"Enter in startAlarm of  UnPlugChargerService ");

        player = MediaPlayer.create(this, R.raw.themes_sound);
        player.setLooping(true);
        player.start();
        Log.d(TAG,"Exit from startAlarm of  UnPlugChargerService");

    }


    private void stopAlarm(){
        try {
            if (player != null && player.isPlaying()) {
                player.stop();
                player.release();
                player = null;
            }
        }catch (Exception e){
            player = null;
            Log.e("PocketTheftService ", "Exception in stopAlarm() of PocketTheftService"+e);
        }
    }

}
