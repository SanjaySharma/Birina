package com.birina.bsecure.trackingrecovery;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.remotescreaming.RemoteScreamingActivity;
import com.birina.bsecure.track.disabledevice.SharedPreferencesUtil;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Validation;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TrackingRecoveryActivity extends BirinaActivity {


    private EditText mEdtPhone,mEdtOtp;
    TextView mActiveInactive;
    private RelativeLayout mRegParent;
    private RelativeLayout mReSetParent ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkExpireStatus();
        setContentView(R.layout.activity_tracking_recovery);
        SharedPreferencesUtil.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.tracking_recover);
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

        // mCountryCode = (Spinner) findViewById(R.id.phone_cc);
        mEdtPhone = (EditText) findViewById(R.id.trackPhone);
        mEdtOtp  = (EditText) findViewById(R.id.trackOtp);
        mRegParent = (RelativeLayout)findViewById(R.id.reg_parent) ;
        mReSetParent = (RelativeLayout) findViewById(R.id.reset_parent);


        TextView desc = (TextView) findViewById(R.id.pocket_theft_description);
        desc.setText(R.string.tracking_recovery_description);

        ((Switch)findViewById(R.id.pocket_theft_on)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    handleTrackingRecoveryActiveListener();
                }else{
                    handleTrackingRecoveryInActiveListener();
                }
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });

        findViewById(R.id.alarm_parent).setVisibility(View.GONE);
        mActiveInactive = (TextView) findViewById(R.id.pocket_theft_active_inactive);
       // findViewById(R.id.pocket_theft_back).setOnClickListener(v ->finish());


        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();
                validateNo();
            }
        });


        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayTrackingRecoveryNotSetView();
            }
        });

        setInitialActiveValue();
    }



    private void setInitialActiveValue(){

        boolean initialValue = BirinaPrefrence.isTrackingRecoveryActive(TrackingRecoveryActivity.this);

        if(initialValue){
            mActiveInactive.setTextColor(getResources().getColor(R.color.colorAccent));

        }else {
            mActiveInactive.setTextColor(getResources().getColor(R.color.gray_stroke));
        }

        ((Switch)findViewById(R.id.pocket_theft_on)).setChecked(initialValue);
    }




    private void validateNo() {

        if (!Validation.isFieldEmpty(mEdtPhone)) {

            if (Validation.isMobileValid(mEdtPhone.getText().toString())) {

                    if(Validation.isFieldEmpty(mEdtOtp)){

                        dismissProgressDialog();

                        Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_otp),
                                Snackbar.LENGTH_LONG).show();
                    }else {


                        Observable.just( saveTrackingRecovery(mEdtPhone.getText().toString(), mEdtOtp.getText().toString()) )
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe( obj ->
                                        {
                                            Toast.makeText(TrackingRecoveryActivity.this, "Details save successfully", Toast.LENGTH_LONG).show();
                                            dismissProgressDialog();
                                            displayTrackingRecoverySetView();
                                        }
                                );
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

        if(BirinaPrefrence.isTrackingRecoveryDataSet(TrackingRecoveryActivity.this)){
            displayTrackingRecoverySetView();
        }
    }




    private void displayTrackingRecoverySetView(){
        mReSetParent.setVisibility(View.VISIBLE);
        mRegParent.setVisibility(View.GONE);
    }



    private void displayTrackingRecoveryNotSetView(){
        mReSetParent.setVisibility(View.GONE);
        mRegParent.setVisibility(View.VISIBLE);
    }



    private boolean saveTrackingRecovery( String phone, String otp){

        BirinaPrefrence.saveTrackingRecoveryNumber(TrackingRecoveryActivity.this, phone);
        BirinaPrefrence.saveTrackingRecoveryOtp(TrackingRecoveryActivity.this, otp);
        BirinaPrefrence.saveTrackingRecoveryDataStatus(TrackingRecoveryActivity.this, true);


        return true;
    }


    private void handleTrackingRecoveryActiveListener(){

        mActiveInactive.setTextColor(getResources().getColor(R.color.colorAccent));
        BirinaPrefrence.updateTrackingRecoveryActiveStatus(TrackingRecoveryActivity.this, true);
    }

    private void handleTrackingRecoveryInActiveListener(){

        mActiveInactive.setTextColor(getResources().getColor(R.color.gray_stroke));
        BirinaPrefrence.updateTrackingRecoveryActiveStatus(TrackingRecoveryActivity.this, false);

    }




    
}
