package com.birina.bsecure.simAlert;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;

/**
 * Created by Admin on 2/20/2018.
 */

public class SimChangeService extends Service {

    private final String TAG = SimChangeService.class.getName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {


        Log.d(Constant.TAG_TRACK, "TrackingRecoveryService Called ");

        Log.d(TAG, "location is!"
                +BirinaPrefrence.getLastLocation(this));

        Toast.makeText(getApplicationContext(), "location is!"
                +BirinaPrefrence.getLastLocation(this), Toast.LENGTH_SHORT).show();



        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String oldSimInfo = BirinaPrefrence.getTrackOldPhone(getApplicationContext());

            if(oldSimInfo != null && !oldSimInfo.isEmpty()) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                String newSimInfo = tManager.getSimSerialNumber();
                Log.e(TAG, "---New SIM no is:" + newSimInfo + " Old sim " + oldSimInfo);

                if (!newSimInfo.equalsIgnoreCase(oldSimInfo)) {

                    sendSms(newSimInfo);

                    //BirinaPrefrence.saveTrackOldPhone(this, newSimInfo);

                }
                stopSelf();
            }else{
                stopSelf();
            }
        } catch (Exception e) {
            Log.e(TAG,"Execption:"+e);

        }
        return startId;
    }


    private void sendSms(String newNumber){

        SmsManager smsMngr = SmsManager.getDefault();
        String destinationAddress = BirinaPrefrence.getSimChangeNumber(getApplicationContext());
        String scAddress = null;
        String text = "New sim is inserted in your device. The new simSerialNumber is: "+newNumber;
        PendingIntent sentIntent = null;
        PendingIntent deliveryIntent = null;
        smsMngr.sendTextMessage(destinationAddress, scAddress, text,
                sentIntent, deliveryIntent);
        System.out.println("-----SMS Send");
    }


}
