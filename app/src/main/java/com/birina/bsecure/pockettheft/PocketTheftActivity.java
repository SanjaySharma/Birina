package com.birina.bsecure.pockettheft;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;


public class PocketTheftActivity extends BirinaActivity {

    private Button mPocketTheftActive;
    private Button mPocketTheftInActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pockettheft);
        checkExpireStatus();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.pockettheft);

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
        mPocketTheftActive = (Button) findViewById(R.id.pocket_theft_on);
        mPocketTheftActive.setOnClickListener(v ->handlePocketTheftActiveListener());
        mPocketTheftInActive = (Button) findViewById(R.id.pocket_theft_off);
        mPocketTheftInActive.setOnClickListener(v ->handlePocketTheftInActiveListener());
        findViewById(R.id.pocket_theft_alarm).setOnClickListener(v ->stopPocketTheftAlarm());
        findViewById(R.id.pocket_theft_back).setOnClickListener(v ->finish());
        setPocketTheftBtnStatus(BirinaPrefrence.isPocketTheftActive(PocketTheftActivity.this));
    }


 private void handlePocketTheftActiveListener(){

     setPocketTheftBtnStatus(true);
     BirinaPrefrence.updatePocketTheftStatus(PocketTheftActivity.this, true);
     startPocketTheftService();
 }

 private void handlePocketTheftInActiveListener(){

     setPocketTheftBtnStatus(false);
     BirinaPrefrence.updatePocketTheftStatus(PocketTheftActivity.this, false);
     stopPocketTheftService();
 }

    private void startPocketTheftService(){

        Intent i = new Intent(this, PocketTheftService.class);
        i.putExtra(Constant.SCREEN_STATE, "");
        startService(i);
    }

    private void stopPocketTheftService(){

        Intent i = new Intent(this, PocketTheftService.class);
        i.putExtra(Constant.SCREEN_STATE, Constant.UNREGISTER);
        startService(i);
    }

    private void stopPocketTheftAlarm(){

        Intent i = new Intent(this, PocketTheftService.class);
        i.putExtra(Constant.SCREEN_STATE, Constant.STOP_POCKET_THEFT_ALARM);
        startService(i);
    }

    private void setPocketTheftBtnStatus(boolean isPocketTheftActive){
        if(isPocketTheftActive){
            mPocketTheftInActive.setBackgroundResource(R.mipmap.button_unselect);
            mPocketTheftActive.setBackgroundResource(R.mipmap.button_select);
        }else {
            mPocketTheftInActive.setBackgroundResource(R.mipmap.button_select);
            mPocketTheftActive.setBackgroundResource(R.mipmap.button_unselect);
        }
    }
}
