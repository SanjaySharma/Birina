package com.birina.bsecure.track.disabledevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;

import com.birina.bsecure.util.BirinaPrefrence;

/**
 * Created by Admin on 3/19/2018.
 */

public class SmsReceiver  extends BroadcastReceiver {

 

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //Check the sender to filter messages which we require to read
            //PhoneNumberUtils.compare(context, BirinaPrefrence.getTrackingNumber(context), sender)

            if (PhoneNumberUtils.compare(context, BirinaPrefrence.getTrackingNumber(context), sender) )
            {
                String messageBody = smsMessage.getMessageBody();

                if(messageBody.contains(BirinaPrefrence.getTrackingOtp(context)))
                {
                    BirinaPrefrence.updateTrackingStatus(context, true);

                    SharedPreferencesUtil.init(context);
                    SharedPreferencesUtil.setBoolean(Lockscreen.ISLOCK, true);
                    Lockscreen.getInstance(context).startLockscreenService();

                    break;

                }


            }
        }

    }

}
