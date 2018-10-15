package com.birina.bsecure.util;

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
    private static final String PREF_TRACKING_OTP = "TrackingOtp";

    private static final String PREF_IS_TRACKING_DATA_SET = "TrackingDataSet";

    private static final String PREF_IS_TRACKING_ACTIVATED = "TrackingActivated";
    private static final String PREF_TRACKING_OLD_PHONE = "TrackingOldPhone";

    private static final String PREF_USER_NAME = "UserName";
    private static final String PREF_BACK_UP_DATE = "backUpDate";
    private static final String PREF_RESTORE_DATE = "restoreDate";
    private static final String PREF_EXPIRE_DATE = "expireDate";
    private static final String PREF_REMAINING_DATE = "remainingDate";
    private static final String PREF_POCKET_THEFT = "pocketTheft";
    private static final String PREF_UNPLUG_CHARGER = "unplugCharger";
    private static final String PREF_REMOTE_SCREAMING = "remoteScreaming";
    private static final String PREF_REMOTE_SCREAMING_NUMBER = "remoteScreamingNumber";
    private static final String PREF_REMOTE_SCREAMING_PWD = "remoteScreamingPwd";
    private static final String PREF_IS_REMOTE_SCREAMING_DATA_SET = "remoteScreamingDataSet";


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



    public static void saveTrackingOtp( Context context, String otp){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TRACKING_OTP, otp);
        editor.commit();
    }



    public static String getTrackingOtp( Context context){

        String trackingPwd = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_TRACKING_OTP) )
        {
            trackingPwd = preferences.getString(PREF_TRACKING_OTP, null);
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




    public static void saveTrackOldPhone( Context context, String oldPhone){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TRACKING_OLD_PHONE, oldPhone);
        editor.commit();
    }



    public static String getTrackOldPhone( Context context){

        String trackingOldPhone = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_TRACKING_OLD_PHONE) )
        {
            trackingOldPhone = preferences.getString(PREF_TRACKING_OLD_PHONE, null);
        }

        return trackingOldPhone;
    }




    public static void saveUserName( Context context, String userName){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }


    public static String getUserName( Context context){

        String userName = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_USER_NAME) ) {
            userName = preferences.getString(PREF_USER_NAME, null);
        }

        return userName;
    }



    public static void saveBackUpDate( Context context, String backUpDate){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_BACK_UP_DATE, backUpDate);
        editor.commit();
    }


    public static String getBackUpDate( Context context){

        String backUpDate = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_BACK_UP_DATE) ) {
            backUpDate = preferences.getString(PREF_BACK_UP_DATE, null);
        }

        return backUpDate;
    }



    public static void saveRestoreDate( Context context, String backUpDate){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_RESTORE_DATE, backUpDate);
        editor.commit();
    }


    public static String getRestoreDate( Context context){

        String backUpDate = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_RESTORE_DATE) ) {
            backUpDate = preferences.getString(PREF_RESTORE_DATE, null);
        }

        return backUpDate;
    }



    public static void saveExpireDate( Context context, String backUpDate){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_EXPIRE_DATE, backUpDate);
        editor.commit();
    }


    public static String getExpireDate( Context context){

        String backUpDate = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_EXPIRE_DATE) ) {
            backUpDate = preferences.getString(PREF_EXPIRE_DATE, null);
        }

        return backUpDate;
    }



    public static void saveRemainingTime( Context context, String backUpDate){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_REMAINING_DATE, backUpDate);
        editor.commit();
    }


    public static String getRemainingTime( Context context){

        String backUpDate = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_REMAINING_DATE) ) {
            backUpDate = preferences.getString(PREF_REMAINING_DATE, null);
        }

        return backUpDate;
    }


    /*
     * This method check PocketTheft all ready active or not
     * */

    public  static boolean isPocketTheftActive(Context context){

        boolean isPocketTheftActive = false;
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(PREF_POCKET_THEFT) && preferences.getBoolean(PREF_POCKET_THEFT, false))
        {
            isPocketTheftActive = true;
        }else{
            isPocketTheftActive = false;
        }
        return isPocketTheftActive;
    }



    /*
     * This method update PocketTheft status.
     * */

    public static void updatePocketTheftStatus( Context context, boolean isPocketTheftActive ){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_POCKET_THEFT, isPocketTheftActive);
        editor.commit();
    }


    /*
     * This method check RemoteScreaming all ready active or not
     * */

    public  static boolean isRemoteScreamingActive(Context context){

        boolean isRemoteScreamingActive = false;
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(PREF_REMOTE_SCREAMING) && preferences.getBoolean(PREF_REMOTE_SCREAMING, false))
        {
            isRemoteScreamingActive = true;
        }else{
            isRemoteScreamingActive = false;
        }
        return isRemoteScreamingActive;
    }



    /*
     * This method update RemoteScreaming status.
     * */

    public static void updateRemoteScreamingStatus( Context context, boolean isRemoteScreaming ){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_REMOTE_SCREAMING, isRemoteScreaming);
        editor.commit();
    }


    /*
     * This method check unplug charger active or not
     * */

    public  static boolean isUnplugChargerActive(Context context){

        boolean isUnplugChargerActive = false;
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(PREF_UNPLUG_CHARGER) && preferences.getBoolean(PREF_UNPLUG_CHARGER, false))
        {
            isUnplugChargerActive = true;
        }else{
            isUnplugChargerActive = false;
        }
        return isUnplugChargerActive;
    }



    /*
     * This method update unplug charger status.
     * */

    public static void updateUnplugChargerStatus( Context context, boolean isUnplugChargerActive ){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_UNPLUG_CHARGER, isUnplugChargerActive);
        editor.commit();
    }





    public static void saveRemoteScreamingNumber( Context context, String number){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_REMOTE_SCREAMING_NUMBER, number);
        editor.commit();
    }


    public static String getRemoteScreamingNumber( Context context){

        String trackingNumber = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_REMOTE_SCREAMING_NUMBER) )
        {
            trackingNumber = preferences.getString(PREF_REMOTE_SCREAMING_NUMBER, null);
        }

        return trackingNumber;
    }



    public static void saveRemoteScreamingPwd( Context context, String password){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_REMOTE_SCREAMING_PWD, password);
        editor.commit();
    }



    public static String getRemoteScreamingPwd( Context context){

        String trackingPwd = null;

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if(preferences.contains(PREF_REMOTE_SCREAMING_PWD) )
        {
            trackingPwd = preferences.getString(PREF_REMOTE_SCREAMING_PWD, null);
        }

        return trackingPwd;
    }



    /*
     * This method check RemoteStreaming data set or not
     * */

    public static boolean isRemoteScreamingDataSet(Context context){

        boolean isRemoteStreamingDataSet = false;
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(PREF_IS_REMOTE_SCREAMING_DATA_SET) && preferences.getBoolean(PREF_IS_REMOTE_SCREAMING_DATA_SET, false))
        {
            isRemoteStreamingDataSet = true;
        }else{
            isRemoteStreamingDataSet = false;
        }
        return isRemoteStreamingDataSet;
    }



    /*
     * This method update RemoteStreaming status.
     * */

    public static void setRemoteScreamingDataStatus( Context context, boolean isRemoteStreamingDataSet ){

        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_IS_REMOTE_SCREAMING_DATA_SET, isRemoteStreamingDataSet);
        editor.commit();
    }
}
