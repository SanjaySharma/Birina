package com.birina.bsecure.track.simalert.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.birina.bsecure.track.simalert.SimChangeService;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;

/**
 * Created by Admin on 2/20/2018.
 */

public class SimChangeReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(Constant.TAG_TRACK, "SimChangeReciver Called ");

              Intent serviceIntent = new Intent(context, SimChangeService.class);
             context.startService(serviceIntent);


    }
}
