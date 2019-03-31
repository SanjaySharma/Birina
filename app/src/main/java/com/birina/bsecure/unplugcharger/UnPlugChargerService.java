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

   private BroadcastReceiver mReceiver;
   private MediaPlayer player;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {

        if(null != intent){

        String screenState = intent.getStringExtra(Constant.UNPLUG_ALARM_STATE);

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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startAlarm(){
        player = MediaPlayer.create(this, R.raw.themes_sound);
        player.setLooping(true);
        player.start();
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
