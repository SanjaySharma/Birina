package com.birina.bsecure.track;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.track.disabledevice.LockscreenUtil;
import com.birina.bsecure.track.disabledevice.SharedPreferencesUtil;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;


import java.io.FileOutputStream;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TrackActivity extends BirinaActivity {


    private EditText mEdtTrackPwd;

   private LinearLayout mRegParent;
   private RelativeLayout mReSetParent ;
   final int REQUEST_CODE_ASK_PERMISSIONS = 10001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkExpireStatus();
        setContentView(R.layout.activity_track);
        SharedPreferencesUtil.init(this);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(null != intent && null != intent.getStringExtra(Constant.TRACK_INTENT_KEY)
                && !intent.getStringExtra(Constant.TRACK_INTENT_KEY).isEmpty()){

            getSupportActionBar().setTitle(intent.getStringExtra(Constant.TRACK_INTENT_KEY));
            ((TextView)findViewById(R.id.textDeviceStorage)).setText(intent.getStringExtra(Constant.TRACK_INTENT_DEC_KEY));
        }else {
            getSupportActionBar().setTitle(R.string.app_name);

        }



        initializeClick();

        navigateFlow();

        getTrackingPermissions();

        saveOldSimNumber();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkExpireStatus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    // Method to initialize id & ClickListener
    private void initializeClick() {

       // mCountryCode = (Spinner) findViewById(R.id.phone_cc);
        mEdtTrackPwd = (EditText) findViewById(R.id.trackPwd);
        mRegParent = (LinearLayout)findViewById(R.id.reg_parent) ;
        mReSetParent = (RelativeLayout) findViewById(R.id.reset_parent);



        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();
                validateNo();
            }
        });


        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayTrackDataNotSetView();
            }
        });

    }


    private void validateNo() {

                if (!Validation.isFieldEmpty(mEdtTrackPwd)) {

                        Observable.just( saveTrackData(mEdtTrackPwd.getText().toString()) )
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe( obj ->
                                        {
                                            Toast.makeText(TrackActivity.this, "Details save successfully", Toast.LENGTH_LONG).show();
                                            dismissProgressDialog();
                                            displayTrackDataSetView();
                                        }
                                );


                } else {

                    dismissProgressDialog();

                    Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_pwd_number),
                            Snackbar.LENGTH_LONG).show();

                }



    }

/*
* This method check is tracking data is set or not.
* Already set open reset view.
*
* */

    public void navigateFlow(){

        if(BirinaPrefrence.isTrackingDataSet(TrackActivity.this)){
            displayTrackDataSetView();
        }
    }


    private void displayTrackDataSetView(){
        mReSetParent.setVisibility(View.VISIBLE);
        mRegParent.setVisibility(View.GONE);
    }



    private void displayTrackDataNotSetView(){
        mReSetParent.setVisibility(View.GONE);
        mRegParent.setVisibility(View.VISIBLE);
    }



private boolean saveTrackData(String siNo){

    BirinaPrefrence.saveTrackingPwd(TrackActivity.this, siNo);
    BirinaPrefrence.saveTrackingStatus(TrackActivity.this, true);

    return true;
}



private void saveOldSimNumber(){

        if(null == BirinaPrefrence.getTrackOldPhone(TrackActivity.this) ||
                BirinaPrefrence.getTrackOldPhone(TrackActivity.this).isEmpty()) {

            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (tManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
                System.out.println("--------SIM Present:" + tManager.getSimState());
                if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED) &&
                        (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                                != PackageManager.PERMISSION_GRANTED) &&
                        (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
                                != PackageManager.PERMISSION_GRANTED) &&
                        (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_ASK_PERMISSIONS);
                } else {

                    Log.d(" ELSE part msisdn ", "ber");

                    String msisdn = tManager.getSimSerialNumber();
                    Log.d("msisdn " + msisdn, "ber");
                    BirinaPrefrence.saveTrackOldPhone(TrackActivity.this, msisdn);

                }

            }
        }
}




/*
* This method check is tracking activated or not.
* Already set open reset view.
*
* */

    public void checkTrackingActivated(){

        if(BirinaPrefrence.isTrackingDataSet(TrackActivity.this)){
            displayTrackDataSetView();
        }
    }


   private void getTrackingPermissions(){


       if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
               != PackageManager.PERMISSION_GRANTED
               && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
               != PackageManager.PERMISSION_GRANTED
               && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
               != PackageManager.PERMISSION_GRANTED
               && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
               != PackageManager.PERMISSION_GRANTED) {

           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                   Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS
                   ,Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
       }


       getLockPermission();

   }




    private void getLockPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 123);
            }
        }

    }





    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(Constant.TAG, "Enter in onActivityResult of PermissionActivity requestCode: "+requestCode);

        switch (requestCode) {
            case 123:
                if (Settings.canDrawOverlays(this)) {
                    LockscreenUtil.getInstance(this).getPermissionCheckSubject()
                            .onNext(true);

                  //  startActivity(new Intent(TrackActivity.this, AlarmGenerator.class));

                   // finish();
                } else {
                    //
                }
                break;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    saveOldSimNumber();
                }
                break;

            default:
                break;
        }
    }
}
