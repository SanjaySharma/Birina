package com.birina.bsecure.pockettheft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;

public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

     if(BirinaPrefrence.isPocketTheftActive(context)) {
    if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        Intent i = new Intent(context, PocketTheftService.class);
        i.putExtra(Constant.SCREEN_STATE, Constant.SCREEN_ON);
        context.startService(i);
    }
}
    }
    }

