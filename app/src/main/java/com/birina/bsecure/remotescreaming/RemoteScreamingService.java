package com.birina.bsecure.remotescreaming;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birina.bsecure.R;
import com.birina.bsecure.util.Constant;

import java.io.IOException;

public class RemoteScreamingService extends Service {

   private MediaPlayer player;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {

        if(null != intent){

            String state = intent.getStringExtra(Constant.REMOTE_SCREAMING_ALARM_STATE);

        switch (state) {
            case Constant.START_REMOTE_SCREAMING_ALARM:
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

            case Constant.STOP_REMOTE_SCREAMING_ALARM:
                stopAlarm();
                stopSelf();
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
