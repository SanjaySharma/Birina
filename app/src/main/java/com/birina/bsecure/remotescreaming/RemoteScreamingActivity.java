package com.birina.bsecure.remotescreaming;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.track.disabledevice.SharedPreferencesUtil;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RemoteScreamingActivity extends BirinaActivity {


    private EditText  mEdtPhone,mEdtOtp;

    private Button mRemoteStreamingActive;
    private Button mRemoteSteamingInActive;

   private LinearLayout mRegParent;
   private RelativeLayout mReSetParent ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkExpireStatus();
        setContentView(R.layout.activity_remote_streaming);
        SharedPreferencesUtil.init(this);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.remote_screaming);



        initializeClick();

        navigateFlow();

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

        mEdtPhone = (EditText) findViewById(R.id.trackPhone);
        mEdtOtp  = (EditText) findViewById(R.id.trackOtp);
        mRegParent = (LinearLayout)findViewById(R.id.reg_parent) ;
        mReSetParent = (RelativeLayout) findViewById(R.id.alert_details);

        mRemoteStreamingActive = (Button) findViewById(R.id.remote_screaming_on);
        mRemoteStreamingActive.setOnClickListener(v ->handleRemoteScreamingActiveListener());
        mRemoteSteamingInActive = (Button) findViewById(R.id.remote_screaming_off);
        mRemoteSteamingInActive.setOnClickListener(v ->handleRemoteScreamingInActiveListener());
        findViewById(R.id.remote_screaming_alarm).setOnClickListener(v ->stopRemoteScreamingAlarm());
        findViewById(R.id.remote_screaming_back).setOnClickListener(v ->finish());
        setRemoteScreamingBtnStatus(BirinaPrefrence.isRemoteScreamingActive(RemoteScreamingActivity.this));


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

                displayRemoteScreamingDataNotSetView();
            }
        });

        findViewById(R.id.remote_screaming_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }


    private void validateNo() {

        if (!Validation.isFieldEmpty(mEdtPhone)) {

            if (Validation.isMobileValid(mEdtPhone.getText().toString())) {

                    if(Validation.isFieldEmpty(mEdtOtp)){

                        dismissProgressDialog();

                        Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_otp),
                                Snackbar.LENGTH_LONG).show();
                    }else {


                        Observable.just( saveRemoteScreamingData(
                                mEdtPhone.getText().toString(), mEdtOtp.getText().toString()) )
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe( obj ->
                                        {
                                            Toast.makeText(RemoteScreamingActivity.this, "Details save successfully", Toast.LENGTH_LONG).show();
                                            dismissProgressDialog();
                                            displayRemoteStreamingDataSetView();
                                        }
                                );
                    }

                }

             else {

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

        if(BirinaPrefrence.isRemoteScreamingDataSet(RemoteScreamingActivity.this)){
            displayRemoteStreamingDataSetView();
        }
    }




    private void displayRemoteStreamingDataSetView(){
        mReSetParent.setVisibility(View.VISIBLE);
        mRegParent.setVisibility(View.GONE);
    }



    private void displayRemoteScreamingDataNotSetView(){
        mReSetParent.setVisibility(View.GONE);
        mRegParent.setVisibility(View.VISIBLE);
    }



private boolean saveRemoteScreamingData(String phone, String otp){

    BirinaPrefrence.saveRemoteScreamingNumber(RemoteScreamingActivity.this, phone);
    BirinaPrefrence.saveRemoteScreamingPwd(RemoteScreamingActivity.this, otp);
    BirinaPrefrence.setRemoteScreamingDataStatus(RemoteScreamingActivity.this, true);

    return true;
}






    private void handleRemoteScreamingActiveListener(){

        setRemoteScreamingBtnStatus(true);
        BirinaPrefrence.updateRemoteScreamingStatus(RemoteScreamingActivity.this, true);
        startRemoteScreamingService();
    }

    private void handleRemoteScreamingInActiveListener(){

        setRemoteScreamingBtnStatus(false);
        BirinaPrefrence.updateRemoteScreamingStatus(RemoteScreamingActivity.this, false);
        stopRemoteScreamingService();
    }

    private void startRemoteScreamingService(){

        Intent i = new Intent(this, RemoteScreamingService.class);
        i.putExtra(Constant.REMOTE_SCREAMING_ALARM_STATE, "");
        startService(i);
    }

    private void stopRemoteScreamingService(){

        Intent i = new Intent(this, RemoteScreamingService.class);
        i.putExtra(Constant.REMOTE_SCREAMING_ALARM_STATE, Constant.UNREGISTER);
        startService(i);
    }

    private void stopRemoteScreamingAlarm(){

        Intent i = new Intent(this, RemoteScreamingService.class);
        i.putExtra(Constant.REMOTE_SCREAMING_ALARM_STATE, Constant.STOP_REMOTE_SCREAMING_ALARM);
        startService(i);
    }

    private void setRemoteScreamingBtnStatus(boolean isUnPlugChargerActive){
        if(isUnPlugChargerActive){
            mRemoteSteamingInActive.setBackgroundResource(R.mipmap.button_unselect);
            mRemoteStreamingActive.setBackgroundResource(R.mipmap.button_select);
        }else {
            mRemoteSteamingInActive.setBackgroundResource(R.mipmap.button_select);
            mRemoteStreamingActive.setBackgroundResource(R.mipmap.button_unselect);
        }
    }

}
