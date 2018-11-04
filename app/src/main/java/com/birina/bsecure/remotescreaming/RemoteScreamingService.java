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

import com.birina.bsecure.util.Constant;

public class RemoteScreamingService extends Service {

   private MediaPlayer player;
   private Ringtone ringtone;
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
                if(ringtone != null){
                    if(!ringtone.isPlaying()){
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
