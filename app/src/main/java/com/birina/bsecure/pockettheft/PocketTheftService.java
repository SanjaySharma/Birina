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
import android.widget.Toast;

import com.birina.bsecure.R;
import com.birina.bsecure.util.Constant;

import java.io.IOException;

public class PocketTheftService extends Service {

   private BroadcastReceiver mReceiver;
   private MediaPlayer player;
   private Ringtone ringtone;
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
                    if(ringtone != null){
                        if(!ringtone.isPlaying()){
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
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null){
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null){
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alert);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        if(volume == 0){
            volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        ringtone.setStreamType(AudioManager.STREAM_ALARM);

        if(ringtone != null){
            ringtone.play();
        }

    }


    private void stopAlarm(){
        if(ringtone != null && ringtone.isPlaying()){
            ringtone.stop();
            ringtone = null;
        }
    }


/*
    private void startAlarm1(){

        player = MediaPlayer.create(this, R.raw.themes_sound); *//* set tone or use setDataSource method *//*
        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        player.setVolume(100, 100);
        // player.setLooping(true);
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer player){
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                player.release();
                player.stop();  }
        });
    }


    private void stopAlarm1(){
        if (mReceiver != null) {
            player.release();
            player.stop();
        }
    }*/

}
