package com.example.birina.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 2/28/2018.
 */

public class BirinaPrefrence {

    private static final String PREF_NAME = "birinaPref";

    public static final String IS_LOGGED_IN = "isLoggedIn";


    private static final String PREF_REGISTERED_NUMBER = "RegisteredNumber";
    private static final String PREF_TRACKING_NUMBER = "TrackingNumber";
    private static final String PREF_TRACKING_PWD = "TrackingPwd";

    private static final String PREF_IS_TRACKING_DATA_SET = "TrackingDataSet";

    private static final String PREF_IS_TRACKING_ACTIVATED = "TrackingActivated";



     /*
    * This method check all ready login or not
    * */

  public  static boolean isLoggedIn(Context context){

        boolean isLoggedIn = false;
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(IS_LOGGED_IN) && preferences.getBoolean(IS_LOGGED_IN, false))
        {
            isLoggedIn = true;
        }else{
            isLoggedIn = false;
        }
        return isLoggedIn;
    }



        /*
    * This method update login status.
    * */

    public static void updateLogInStatus( Context context, boolean isLoggedIn ){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }


    public static void saveRegisteredNumber( Context context, String number){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_REGISTERED_NUMBER, number);
        editor.commit();
    }

    public static String getRegisteredNumber( Context context){

        String registeredNumber = null;

       SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

       if(preferences.contains(PREF_REGISTERED_NUMBER) )
       {
           registeredNumber = preferences.getString(PREF_REGISTERED_NUMBER, null);
       }

       return registeredNumber;
   }



    public static void saveTrackingNumber( Context context, String number){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TRACKING_NUMBER, number);
        editor.commit();
    }


    public static String getTrackingNumber( Context context){

        String trackingNumber = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_TRACKING_NUMBER) )
        {
            trackingNumber = preferences.getString(PREF_TRACKING_NUMBER, null);
        }

        return trackingNumber;
    }



    public static void saveTrackingPwd( Context context, String password){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TRACKING_PWD, password);
        editor.commit();
    }



    public static String getTrackingPwd( Context context){

        String trackingPwd = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_TRACKING_PWD) )
        {
            trackingPwd = preferences.getString(PREF_TRACKING_PWD, null);
        }

        return trackingPwd;
    }



     /*
    * This method check tracking data set or not
    * */

    public static boolean isTrackingDataSet(Context context){

        boolean isTrackingDataSet = false;
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(PREF_IS_TRACKING_DATA_SET) && preferences.getBoolean(PREF_IS_TRACKING_DATA_SET, false))
        {
            isTrackingDataSet = true;
        }else{
            isTrackingDataSet = false;
        }
        return isTrackingDataSet;
    }



        /*
    * This method update tracking status.
    * */

    public static void saveTrackingStatus( Context context, boolean isTrackingDataSet ){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_IS_TRACKING_DATA_SET, isTrackingDataSet);
        editor.commit();
    }





     /*
    * This method check tracking data set or not
    * */

    public static boolean isTrackingActivated(Context context){

        boolean isTrackingDataSet = false;
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(PREF_IS_TRACKING_ACTIVATED) && preferences.getBoolean(PREF_IS_TRACKING_ACTIVATED, false))
        {
            isTrackingDataSet = true;
        }else{
            isTrackingDataSet = false;
        }
        return isTrackingDataSet;
    }



         /*
    * This method update tracking  status.
    * */

    public static void updateTrackingStatus( Context context, boolean isActivated ){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_IS_TRACKING_ACTIVATED, isActivated);
        editor.commit();
    }
}
