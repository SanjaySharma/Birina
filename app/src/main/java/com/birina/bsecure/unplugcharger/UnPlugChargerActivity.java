package com.birina.bsecure.unplugcharger;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.pockettheft.PocketTheftService;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;


public class UnPlugChargerActivity extends BirinaActivity {

    private Button mUnPlugChargerActive;
    private Button mUnPlugChargerInActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unplugcharger);
        checkExpireStatus();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.unplug_charger);

        initializeView();
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


    private void initializeView(){


    }

    private void setListeners(){
        mUnPlugChargerActive = (Button) findViewById(R.id.unPlug_charger_on);
        mUnPlugChargerActive.setOnClickListener(v ->handleUnPlugChargerActiveListener());

        mUnPlugChargerInActive = (Button) findViewById(R.id.unPlug_charger_off);
        mUnPlugChargerInActive.setOnClickListener(v ->handleUnPlugChargerInActiveListener());

        findViewById(R.id.unPlug_charger_alarm).setOnClickListener(v ->stopUnPlugChargerAlarm());
        findViewById(R.id.unPlug_charger_back).setOnClickListener(v ->finish());

        setPocketTheftBtnStatus(BirinaPrefrence.isUnplugChargerActive(UnPlugChargerActivity.this));
    }


    private void handleUnPlugChargerActiveListener(){

        setPocketTheftBtnStatus(true);
        BirinaPrefrence.updateUnplugChargerStatus(UnPlugChargerActivity.this, true);
    }

    private void handleUnPlugChargerInActiveListener(){

        setPocketTheftBtnStatus(false);
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

    private void setPocketTheftBtnStatus(boolean isUnPlugChargerActive){
        if(isUnPlugChargerActive){
            mUnPlugChargerInActive.setBackgroundResource(R.mipmap.button_unselect);
            mUnPlugChargerActive.setBackgroundResource(R.mipmap.button_select);
        }else {
            mUnPlugChargerInActive.setBackgroundResource(R.mipmap.button_select);
            mUnPlugChargerActive.setBackgroundResource(R.mipmap.button_unselect);
        }
    }



}
