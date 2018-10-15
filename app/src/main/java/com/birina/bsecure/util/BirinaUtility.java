package com.birina.bsecure.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.birina.bsecure.backup.model.Request;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 3/29/2018.
 */

public class BirinaUtility {


    public static Request getBackUpData(Context mContext) {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("compliance_score1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Request request = null;

        Gson gson = new Gson();

        try {
            request = gson.fromJson(json, Request.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }


    public static boolean isLicenseExpire(Context context) {

        boolean isExpire = false;

        try {

            String dtStart = BirinaPrefrence.getExpireDate(context);

            if(null != dtStart && !dtStart.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");


                Date endDate = sdf.parse(dtStart);

                Date currentDate = Calendar.getInstance().getTime();

                long different = endDate.getTime() - currentDate.getTime();

                if (different <= 0) {
                    isExpire = true;
                } else {
                    isExpire = false;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isExpire;
    }



    public static String getDateDifference ( String enDate) {

        Date endDate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        try {
             endDate = sdf.parse(enDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date currentTime = Calendar.getInstance().getTime();

        //milliseconds
        long different = endDate.getTime() - currentTime.getTime();

        System.out.println("startDate : " + currentTime);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        StringBuilder timeRemaining = new StringBuilder(""+elapsedDays).append(" Days:")
                .append(""+elapsedHours).append(" Hours:").append(elapsedMinutes)
                .append(" Minutes").append(" Remaining");

        Log.d("timeRemaining "," is: "+timeRemaining);

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return timeRemaining.toString();
    }


  /*public String  getDeviceIMEI(Context context){

  }*/

}
