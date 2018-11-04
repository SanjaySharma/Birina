package com.birina.bsecure.pockettheft;


import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;


public class PocketTheftActivity extends BirinaActivity {

      TextView mActiveInactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pockettheft);
        checkExpireStatus();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.pockettheft);

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
        headerIcon.setBackgroundResource(R.drawable.ic_pocket_theft);

        TextView header = (TextView) findViewById(R.id.pocket_theft_header);
        header.setText(R.string.pocket_theft);

        TextView desc = (TextView) findViewById(R.id.pocket_theft_description);
        desc.setText(R.string.pocket_theft_description);

        ((Switch)findViewById(R.id.pocket_theft_on)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    handlePocketTheftActiveListener();
                }else{
                    handlePocketTheftInActiveListener();
                }
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });

        mActiveInactive = (TextView) findViewById(R.id.pocket_theft_active_inactive);

        findViewById(R.id.pocket_theft_alarm).setOnClickListener(v ->stopPocketTheftAlarm());
        findViewById(R.id.pocket_theft_back).setOnClickListener(v ->finish());
        setInitialActiveValue();
    }


 private void handlePocketTheftActiveListener(){

     mActiveInactive.setTextColor(getResources().getColor(R.color.colorAccent));
     BirinaPrefrence.updatePocketTheftStatus(PocketTheftActivity.this, true);
     startPocketTheftService();
 }

 private void handlePocketTheftInActiveListener(){

     mActiveInactive.setTextColor(getResources().getColor(R.color.gray_stroke));

     BirinaPrefrence.updatePocketTheftStatus(PocketTheftActivity.this, false);
     stopPocketTheftService();
 }


 private void setInitialActiveValue(){

        boolean initialValue = BirinaPrefrence.isPocketTheftActive(PocketTheftActivity.this);

        if(initialValue){
            mActiveInactive.setTextColor(getResources().getColor(R.color.colorAccent));

        }else {
            mActiveInactive.setTextColor(getResources().getColor(R.color.gray_stroke));
        }

     ((Switch)findViewById(R.id.pocket_theft_on)).setChecked(initialValue);
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


}
