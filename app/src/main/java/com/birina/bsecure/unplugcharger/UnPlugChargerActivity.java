package com.birina.bsecure.unplugcharger;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.pockettheft.PocketTheftActivity;
import com.birina.bsecure.pockettheft.PocketTheftService;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;


public class UnPlugChargerActivity extends BirinaActivity {

    TextView mActiveInactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unplugcharger);
        checkExpireStatus();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.unplug_charger);

        setListeners();
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


    @Override
    protected void onResume() {
        super.onResume();
        checkExpireStatus();
    }



    private void setListeners(){

        findViewById(R.id.back_parent).setVisibility(View.GONE);

        ImageView headerIcon = (ImageView) findViewById(R.id.header_icon);
        headerIcon.setBackgroundResource(R.mipmap.ic_unplug);

        TextView header = (TextView) findViewById(R.id.pocket_theft_header);
        header.setText(R.string.unplug_charger);

        TextView desc = (TextView) findViewById(R.id.pocket_theft_description);
        desc.setText(R.string.pocket_theft_description);

        ((Switch)findViewById(R.id.pocket_theft_on)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    handleUnPlugChargerActiveListener();
                }else{
                    handleUnPlugChargerInActiveListener();
                }

            }
        });

        mActiveInactive = (TextView) findViewById(R.id.pocket_theft_active_inactive);

        findViewById(R.id.pocket_theft_alarm).setOnClickListener(v ->stopUnPlugChargerAlarm());
        findViewById(R.id.pocket_theft_back).setOnClickListener(v ->finish());
        setInitialActiveValue();
    }



    private void setInitialActiveValue(){

        boolean initialValue = BirinaPrefrence.isUnplugChargerActive(UnPlugChargerActivity.this);

        if(initialValue){
            mActiveInactive.setTextColor(getResources().getColor(R.color.colorAccent));

        }else {
            mActiveInactive.setTextColor(getResources().getColor(R.color.gray_stroke));
        }

        ((Switch)findViewById(R.id.pocket_theft_on)).setChecked(initialValue);
    }


    private void handleUnPlugChargerActiveListener(){

        mActiveInactive.setTextColor(getResources().getColor(R.color.colorAccent));

        BirinaPrefrence.updateUnplugChargerStatus(UnPlugChargerActivity.this, true);
    }

    private void handleUnPlugChargerInActiveListener(){

        mActiveInactive.setTextColor(getResources().getColor(R.color.gray_stroke));
        BirinaPrefrence.updateUnplugChargerStatus(UnPlugChargerActivity.this, false);
        stopUnPlugChargerService();
    }



    private void stopUnPlugChargerService(){

        Intent i = new Intent(this, UnPlugChargerService.class);
        i.putExtra(Constant.UNPLUG_ALARM_STATE, Constant.UNREGISTER);
        startService(i);
    }

    private void stopUnPlugChargerAlarm(){

        Intent i = new Intent(this, UnPlugChargerService.class);
        i.putExtra(Constant.UNPLUG_ALARM_STATE, Constant.STOP_UNPLUG_ALARM);
        startService(i);
    }


}
