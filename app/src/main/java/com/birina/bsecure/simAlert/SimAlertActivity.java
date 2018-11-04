package com.birina.bsecure.simAlert;


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
import com.birina.bsecure.pockettheft.PocketTheftActivity;
import com.birina.bsecure.remotescreaming.RemoteScreamingActivity;
import com.birina.bsecure.track.disabledevice.SharedPreferencesUtil;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Validation;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SimAlertActivity  extends BirinaActivity {

    TextView mActiveInactive;

    private EditText mEdtPhone,mEdtOtp;
    private RelativeLayout mRegParent;
    private RelativeLayout mReSetParent ;
    final int REQUEST_CODE_ASK_PERMISSIONS = 10001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkExpireStatus();
        setContentView(R.layout.activity_simalert);
        SharedPreferencesUtil.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.sim_alert);
        initializeViews();

        navigateFlow();

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



    private void initializeViews(){


        findViewById(R.id.alarm_parent).setVisibility(View.GONE);

        ImageView headerIcon = (ImageView) findViewById(R.id.header_icon);
        headerIcon.setBackgroundResource(R.drawable.ic_sim_change);

        TextView header = (TextView) findViewById(R.id.pocket_theft_header);
        header.setText(R.string.sim_alert);

        TextView desc = (TextView) findViewById(R.id.pocket_theft_description);
        desc.setText(R.string.pocket_theft_description);

        ((Switch)findViewById(R.id.pocket_theft_on)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    handleSimChangeActiveListener();
                }else{
                    handleSimChangeInActiveListener();
                }
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });

        mActiveInactive = (TextView) findViewById(R.id.pocket_theft_active_inactive);

        findViewById(R.id.pocket_theft_back).setOnClickListener(v ->finish());





        mEdtPhone = (EditText) findViewById(R.id.trackPhone);
        mEdtOtp  = (EditText) findViewById(R.id.trackOtp);
        mRegParent = (RelativeLayout)findViewById(R.id.reg_parent) ;
        mReSetParent = (RelativeLayout) findViewById(R.id.reset_parent);


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

                displaySimChangeNotSetView();
            }
        });

        setInitialActiveValue();
    }


    private void setInitialActiveValue(){

        boolean initialValue = BirinaPrefrence.isSimChangeActive(SimAlertActivity.this);

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


                        Observable.just( saveSimChange(mEdtPhone.getText().toString(), mEdtOtp.getText().toString()) )
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe( obj ->
                                        {
                                            Toast.makeText(SimAlertActivity.this, "Details save successfully", Toast.LENGTH_LONG).show();
                                            dismissProgressDialog();
                                            displaySimChangeSetView();
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

        if(BirinaPrefrence.isSimChangeDataSet(SimAlertActivity.this)){
            displaySimChangeSetView();
        }
    }




    private void displaySimChangeSetView(){
        mReSetParent.setVisibility(View.VISIBLE);
        mRegParent.setVisibility(View.GONE);
    }



    private void displaySimChangeNotSetView(){
        mReSetParent.setVisibility(View.GONE);
        mRegParent.setVisibility(View.VISIBLE);
    }



    private boolean saveSimChange( String phone, String otp){

        BirinaPrefrence.saveSimChangeNumber(SimAlertActivity.this, phone);
        BirinaPrefrence.saveSimChangeOtp(SimAlertActivity.this, otp);
        BirinaPrefrence.saveSimChangeDataStatus(SimAlertActivity.this, true);


        return true;
    }


    private void handleSimChangeActiveListener(){

        mActiveInactive.setTextColor(getResources().getColor(R.color.colorAccent));
        BirinaPrefrence.updateSimChangeActiveStatus(SimAlertActivity.this, true);
    }

    private void handleSimChangeInActiveListener(){

        mActiveInactive.setTextColor(getResources().getColor(R.color.gray_stroke));
        BirinaPrefrence.updateSimChangeActiveStatus(SimAlertActivity.this, false);

    }



    private void saveOldSimNumber(){

        if(null == BirinaPrefrence.getTrackOldPhone(SimAlertActivity.this) ||
                BirinaPrefrence.getTrackOldPhone(SimAlertActivity.this).isEmpty()) {

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
                    BirinaPrefrence.saveTrackOldPhone(SimAlertActivity.this, msisdn);

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

        if(BirinaPrefrence.isSimChangeDataSet(SimAlertActivity.this)){
            displaySimChangeSetView();
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
