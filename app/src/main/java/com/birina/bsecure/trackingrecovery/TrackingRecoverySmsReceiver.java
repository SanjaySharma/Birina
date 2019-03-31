package com.birina.bsecure.trackingrecovery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;

import com.birina.bsecure.Base.LocationActivity;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;


public class TrackingRecoverySmsReceiver extends BroadcastReceiver {

 

    @Override
    public void onReceive(Context context, Intent intent) {

        if(BirinaPrefrence.isTrackingRecoveryActive(context)) {

            Bundle data = intent.getExtras();

            Object[] pdus = (Object[]) data.get("pdus");

            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

                //Check the sender to filter messages which we require to read
                    String messageBody = smsMessage.getMessageBody();
                    String recoveryPwd = BirinaPrefrence.getTrackingRecoveryPwd(context);
                    if (messageBody.contains(recoveryPwd)) {
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        BirinaPrefrence.saveTrackingRecoveryNumber(context, sender);
                        startLocationActivity(context);
                        break;
                    }

            }
        }
    }


    private void startLocationActivity(Context context){

        Intent i = new Intent(context, LocationActivity.class);
        i.putExtra(Constant.LOCATION_ACTIVITY_KEY, Constant.TRACKING_RECOVERY_KEY);
        context.startActivity(i);
    }
}
