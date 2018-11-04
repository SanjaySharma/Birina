package com.birina.bsecure.simAlert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.birina.bsecure.Base.LocationActivity;
import com.birina.bsecure.simAlert.SimChangeService;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;

/**
 * Created by Admin on 2/20/2018.
 */

public class SimChangeReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(Constant.TAG_TRACK, "SimChangeReciver Called ");

        if(BirinaPrefrence.isSimChangeActive(context)) {

            Intent serviceIntent = new Intent(context, LocationActivity.class);
            serviceIntent.putExtra(Constant.LOCATION_ACTIVITY_KEY, Constant.SIM_CHANGE_KEY);
            context.startActivity(serviceIntent);
        }

    }
}
