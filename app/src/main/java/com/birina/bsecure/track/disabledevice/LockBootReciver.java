package com.example.bsecure.track.disabledevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * Created by Admin on 2/18/2018.
 */

public class LockBootReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Reciver call", Toast.LENGTH_LONG).show();
        Intent startLockscreenIntent = new Intent(context, LockscreenActivity.class);
        context.startActivity(startLockscreenIntent);
    }
}
