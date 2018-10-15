package com.birina.bsecure.unplugcharger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.birina.bsecure.pockettheft.PocketTheftService;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;

public class PlugInReceiver extends BroadcastReceiver {

    public void onReceive(Context context , Intent intent) {

        if(BirinaPrefrence.isUnplugChargerActive(context)) {
            startUnPlugChargerService(context);

        }

    }




    private void startUnPlugChargerService(Context context){

        Intent i = new Intent(context, UnPlugChargerService.class);
        i.putExtra(Constant.UNPLUG_ALARM_STATE, Constant.START_UNPLUG_ALARM);
        context.startService(i);
    }

}
