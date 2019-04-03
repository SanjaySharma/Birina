package com.birina.bsecure.trackingrecovery;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import com.birina.bsecure.remotescreaming.RemoteScreamingService;
import com.birina.bsecure.track.disabledevice.SharedPreferencesUtil;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TrackingRecoveryActivity extends BirinaActivity {


    TextView mActiveInactive;




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

         findViewById(R.id.back_parent).setVisibility(View.GONE);
        TextView desc = (TextView) findViewById(R.id.pocket_theft_description);
        desc.setText(R.string.track_warning);

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



    private void handleTrackingRecoveryActiveListener(){

        mActiveInactive.setTextColor(getResources().getColor(R.color.colorAccent));
        BirinaPrefrence.updateTrackingRecoveryActiveStatus(TrackingRecoveryActivity.this, true);
        startTrackingRecoveryService();
    }

    private void handleTrackingRecoveryInActiveListener(){

        mActiveInactive.setTextColor(getResources().getColor(R.color.gray_stroke));
        BirinaPrefrence.updateTrackingRecoveryActiveStatus(TrackingRecoveryActivity.this, false);
        stopTrackingRecoveryService();
    }


    private void startTrackingRecoveryService(){

        Intent i = new Intent(this, TrackingRecoveryService.class);
        i.putExtra(Constant.TRACKING_RECOVERY_ALARM_STATE, "");
        startService(i);
    }

    private void stopTrackingRecoveryService(){

        Intent i = new Intent(this, TrackingRecoveryService.class);
        i.putExtra(Constant.TRACKING_RECOVERY_ALARM_STATE, Constant.STOP_TRACKING_RECOVERYALARM);
        startService(i);
    }

    
}
