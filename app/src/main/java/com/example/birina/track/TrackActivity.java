package com.example.birina.track;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.birina.Base.BaseActivity;
import com.example.birina.R;
import com.example.birina.track.disabledevice.LockscreenUtil;
import com.example.birina.track.disabledevice.SharedPreferencesUtil;
import com.example.birina.util.BirinaPrefrence;
import com.example.birina.util.Constant;
import com.example.birina.util.Validation;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TrackActivity extends BaseActivity {


    private EditText mEdtTrackPwd, mEdtPhone,mEdtOtp;

   private LinearLayout mRegParent;
   private RelativeLayout mReSetParent ;
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        SharedPreferencesUtil.init(this);


        ActionBar supportActionBar = getSupportActionBar();


        if (supportActionBar != null)
            supportActionBar.hide();

        initializeClick();

        navigateFlow();

        getTrackingPermissions();

    }

    // Method to initialize id & ClickListener
    private void initializeClick() {


        mEdtTrackPwd = (EditText) findViewById(R.id.trackPwd);
        mEdtPhone = (EditText) findViewById(R.id.trackPhone);
        mEdtOtp : findViewById(R.id.trackOtp);
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

        if (!Validation.isFieldEmpty(mEdtPhone)) {

            if (Validation.isMobileValid(mEdtPhone.getText().toString())) {
                if (!Validation.isFieldEmpty(mEdtTrackPwd)) {

                    if(Validation.isFieldEmpty(mEdtOtp)){

                        dismissProgressDialog();

                        Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_otp),
                                Snackbar.LENGTH_LONG).show();
                    }else {


                        Observable.just( saveTrackData(mEdtTrackPwd.getText().toString(), mEdtPhone.getText().toString()) )
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe( obj ->
                                        {
                                            Toast.makeText(TrackActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
                                            dismissProgressDialog();
                                            displayTrackDataSetView();
                                        }
                                );
                    }

                } else {

                    dismissProgressDialog();

                    Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_pwd_number),
                            Snackbar.LENGTH_LONG).show();

                }

            }  else {

                dismissProgressDialog();

                Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.reg_invalid_mobile),
                        Snackbar.LENGTH_LONG).show();


            }


        } else {

            dismissProgressDialog();

            Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_mobile_number),
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



private boolean saveTrackData(String siNo, String phone){

    BirinaPrefrence.saveTrackingNumber(TrackActivity.this, phone);
    BirinaPrefrence.saveTrackingPwd(TrackActivity.this, siNo);
    BirinaPrefrence.saveTrackingStatus(TrackActivity.this, true);

    return true;
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


       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
               != PackageManager.PERMISSION_GRANTED
               && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
               != PackageManager.PERMISSION_GRANTED) {

           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                   Manifest.permission.SEND_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
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

                  //  startActivity(new Intent(TrackActivity.this, MainActivity.class));

                   // finish();
                } else {
                    //
                }
                break;
        }
    }
}
