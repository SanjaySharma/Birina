package com.example.birina.track.disabledevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.birina.util.BirinaPrefrence;

/**
 * Created by Admin on 3/19/2018.
 */

public class SmsReceiver  extends BroadcastReceiver {

    //interface
    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //Check the sender to filter messages which we require to read

            if (sender.equals(BirinaPrefrence.getTrackingNumber(context)))
            {

                String messageBody = smsMessage.getMessageBody();

                if(messageBody.contains(BirinaPrefrence.getTrackingPwd(context)))
                {
                    BirinaPrefrence.updateTrackingStatus(context, true);

                    SharedPreferencesUtil.init(context);
                    SharedPreferencesUtil.setBoolean(Lockscreen.ISLOCK, true);
                    Lockscreen.getInstance(context).startLockscreenService();

                    break;

                }
                //Pass the message text to interface
                mListener.messageReceived(messageBody);

            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
