package com.birina.bsecure.track.disabledevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;


/**
 * Created by Admin on 2/18/2018.
 */

public class LockBootReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(Constant.TAG_TRACK, "LockBootReciver Called isTrackingActivated: "
                +BirinaPrefrence.isTrackingActivated(context));


        if(BirinaPrefrence.isTrackingActivated(context)) {
            Intent startLockscreenIntent = new Intent(context, LockscreenActivity.class);
            context.startActivity(startLockscreenIntent);
        }
    }
}
