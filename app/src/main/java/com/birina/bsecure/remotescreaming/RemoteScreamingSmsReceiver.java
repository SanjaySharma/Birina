package com.birina.bsecure.remotescreaming;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;

import com.birina.bsecure.unplugcharger.UnPlugChargerService;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;


public class RemoteScreamingSmsReceiver extends BroadcastReceiver {

    private final String TAG = "RemoteScreaming";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"Enter in onReceive of RemoteScreamingSmsReceiver ");

        if(BirinaPrefrence.isRemoteScreamingActive(context)) {

            Bundle data = intent.getExtras();

            Object[] pdus = (Object[]) data.get("pdus");

            for (int i = 0; i < pdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

              //  String sender = smsMessage.getDisplayOriginatingAddress();
                //Check the sender to filter messages which we require to read
                //
                //"AX-bytwoo".equalsIgnoreCase(sender)
                  String messageBody = smsMessage.getMessageBody();
                Log.d(TAG," onReceive of RemoteScreamingSmsReceiver messageBody is:"+messageBody);

                    if (messageBody.contains(BirinaPrefrence.getRemoteScreamingPwd(context))) {
                        startRemoteScreamingService(context);
                        break;
                    }
            }
        }
        Log.d(TAG,"Exit from onReceive of RemoteScreamingSmsReceiver");

    }


    private void startRemoteScreamingService(Context context){

        Intent i = new Intent(context, RemoteScreamingService.class);
        i.putExtra(Constant.REMOTE_SCREAMING_ALARM_STATE, Constant.START_REMOTE_SCREAMING_ALARM);
        context.startService(i);
    }
}
