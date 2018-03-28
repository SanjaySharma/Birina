package com.example.birina.track.simalert.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.birina.track.simalert.SimChangeService;

/**
 * Created by Admin on 2/20/2018.
 */

public class SimChangeReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, " SimChangeReciver ", Toast.LENGTH_LONG).show();
      //  Intent serviceIntent = new Intent(context, SimChangeService.class);
       // context.startService(serviceIntent);


    }
}
