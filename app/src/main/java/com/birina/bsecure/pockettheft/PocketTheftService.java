package com.birina.bsecure.pockettheft;

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
import com.birina.bsecure.util.Constant;

import java.io.IOException;

public class PocketTheftService extends Service {

   private BroadcastReceiver mReceiver;
   private MediaPlayer player;
    @Override
    public void onCreate() {
        super.onCreate();
        registerScreenReceiver();
    }

    @Override
    public void onStart(Intent intent, int startId) {

        if(null != intent) {

            String screenState = intent.getStringExtra(Constant.SCREEN_STATE);

            switch (screenState) {
                case Constant.SCREEN_ON:
                    if(player != null){
                        if(!player.isPlaying()){
                            startAlarm();
                        }
                    }else{
                        startAlarm();
                    }
                    break;

                case Constant.UNREGISTER:
                    unRegisterScreenReceiver();
                    stopAlarm();
                    stopSelf();
                    break;
                case Constant.STOP_POCKET_THEFT_ALARM:
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

    @Override
    public void onDestroy()
    {
        this.unRegisterScreenReceiver();
    }

    private void registerScreenReceiver(){
        // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    private void unRegisterScreenReceiver(){
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
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
