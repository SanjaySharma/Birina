package com.example.birina.alarm;

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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static android.provider.Telephony.Mms.Part.FILENAME;

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
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {


            /*FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader in = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(in);
            String data = br.readLine();
            System.out.println("---Data Read From File is:" + data);*/


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            String newsiminfo = tManager.getSimSerialNumber();
            Log.e(TAG, "---New SIM no is:" + newsiminfo);
           // if (data.equals(newsiminfo)) {
                Log.e(TAG,"------Old sim Present:");

                Toast.makeText(this, "Old SIM", Toast.LENGTH_LONG).show();
           // } else {
                Toast.makeText(this, "New SIM", Toast.LENGTH_LONG).show();
                SmsManager smsMngr = SmsManager.getDefault();
                String destinationaddress = "9599385901";
                String scAddress = null;
                String text = "New Sim Is Inserted In Your Device";
                PendingIntent sentIntent = null;
                PendingIntent deliveryIntent = null;
                smsMngr.sendTextMessage(destinationaddress, scAddress, text,
                        sentIntent, deliveryIntent);
                System.out.println("-----SMS Send");

                stopSelf();
          //  }

        } catch (Exception e) {
            Log.e(TAG,"Execption:"+e);

        }
        return startId;
    }



}
