package com.birina.bsecure.remotescreaming;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.pockettheft.PocketTheftActivity;
import com.birina.bsecure.track.disabledevice.SharedPreferencesUtil;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RemoteScreamingActivity extends BirinaActivity {

    TextView mActiveInactive;


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


    private void initializeClick(){
        findViewById(R.id.back_parent).setVisibility(View.GONE);
        TextView desc = (TextView) findViewById(R.id.pocket_theft_description);
        desc.setText(R.string.remote_screaming_description);

        ((Switch)findViewById(R.id.pocket_theft_on)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    handleRemoteScreamingActiveListener();
                }else{
                    handleRemoteScreamingInActiveListener();
                }
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });

        mActiveInactive = (TextView) findViewById(R.id.pocket_theft_active_inactive);

        findViewById(R.id.pocket_theft_alarm).setOnClickListener(v ->stopRemoteScreamingAlarm());

        setInitialActiveValue();


    }





    private void setInitialActiveValue(){

        boolean initialValue = BirinaPrefrence.isRemoteScreamingActive(RemoteScreamingActivity.this);

        if(initialValue){
            mActiveInactive.setTextColor(getResources().getColor(R.color.colorAccent));

        }else {
            mActiveInactive.setTextColor(getResources().getColor(R.color.gray_stroke));
        }

        ((Switch)findViewById(R.id.pocket_theft_on)).setChecked(initialValue);
    }








    private void handleRemoteScreamingActiveListener(){

        mActiveInactive.setTextColor(getResources().getColor(R.color.colorAccent));
        BirinaPrefrence.updateRemoteScreamingStatus(RemoteScreamingActivity.this, true);
        startRemoteScreamingService();
    }

    private void handleRemoteScreamingInActiveListener(){

        mActiveInactive.setTextColor(getResources().getColor(R.color.gray_stroke));
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



}
