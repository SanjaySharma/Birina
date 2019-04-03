package com.birina.bsecure.remotescreaming;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birina.bsecure.R;
import com.birina.bsecure.unplugcharger.PlugInReceiver;
import com.birina.bsecure.util.Constant;

import java.io.IOException;

public class RemoteScreamingService extends Service {
    private final String TAG = "RemoteScreaming";
    private BroadcastReceiver mReceiver;
   private MediaPlayer player;
    @Override
    public void onCreate() {
        Log.d(TAG," onCreate of RemoteScreamingService: ");
        super.onCreate();
        registerRemoteScreamingReceiver();
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


    @Override
    public void onDestroy()
    {
        this.unRegisterRemoteScreamingReceiver();
    }

    private void registerRemoteScreamingReceiver(){
        Log.d(TAG,"Enter in registerRemoteScreamingReceiver of  RemoteScreamingService ");

        // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
        mReceiver = new RemoteScreamingSmsReceiver();
        registerReceiver(mReceiver, intentFilter);
        Log.d(TAG,"Exit from registerRemoteScreamingReceiver of  RemoteScreamingService");

    }

    private void unRegisterRemoteScreamingReceiver(){
        Log.d(TAG,"Enter in unRegisterRemoteScreamingReceiver of  RemoteScreamingService ");

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        Log.d(TAG,"Exit from unRegisterRemoteScreamingReceiver of  RemoteScreamingService");

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
            Log.e("RemoteScreamingService ", "Exception in stopAlarm() of RemoteScreamingService"+e);
        }
    }

}
