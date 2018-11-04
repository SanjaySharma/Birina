package com.birina.bsecure.resetpwd;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.dashboard.DeshBoardActivity;
import com.birina.bsecure.login.LoginActivity;
import com.birina.bsecure.network.RestClient;
import com.birina.bsecure.resetpwd.model.ForgotPwdRequestModel;
import com.birina.bsecure.resetpwd.model.ForgotPwdResponseModel;
import com.birina.bsecure.resetpwd.model.ReNewSerialKeyRequestModel;
import com.birina.bsecure.resetpwd.model.ReNewSerialKeyResponseModel;
import com.birina.bsecure.resetpwd.model.ResetPwdResponseModel;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.ConnectionManager;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ReNewActivity extends BaseActivity {

    final int REQUEST_CODE_ASK_PERMISSIONS = 10044;

    private EditText mEdtReNewValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew);
        //navigateFlow();

        ActionBar supportActionBar = getSupportActionBar();


        if (supportActionBar != null)
            supportActionBar.hide();

        //  AddUser();
        initializeClick();

    }

    // Method to initialize id & ClickListener
    private void initializeClick() {


        mEdtReNewValue = (EditText) findViewById(R.id.textReNew);

        findViewById(R.id.btn_reNewSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();

                if(ConnectionManager.isInternetAvailable(ReNewActivity.this)){

                    validateKey();

                }else {
                    dismissProgressDialog();
                    Snackbar.make(findViewById(R.id.reNewParent), getResources().getString(R.string.connection_manager_fail),
                            Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }


    private void validateKey() {

                    if (Validation.isFieldEmpty(mEdtReNewValue)) {

                        dismissProgressDialog();

                        Snackbar.make(findViewById(R.id.reNewParent), getResources().getString(R.string.fill_si_number),
                                Snackbar.LENGTH_LONG).show();

                    } else {

                        callApi();
                    }
        }



/*
* This method check is user loggedIn or not.
* Already loggedIn navigate to Dashboard.
* Not logged in show login UI.
* */

    public void navigateFlow() {

        if (BirinaPrefrence.isLoggedIn(ReNewActivity.this)) {
            startDashBoardActivity();
        }
    }


    private void startDashBoardActivity() {
        startActivity(new Intent(ReNewActivity.this, DeshBoardActivity.class));
        finish();
    }



    private void startLoginActivity() {
        startActivity(new Intent(ReNewActivity.this, LoginActivity.class));
        finish();
    }




    private void callApi(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS
                    ,Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS,Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
        } else {

            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String imei =  telephonyManager.getDeviceId();
            resetKey(mEdtReNewValue.getText().toString(), imei);

        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    callApi();
                }
                break;

            default:
                break;
        }
    }



    private void resetKey(String siNo, String deviceId) {

        RestClient restClient = new RestClient();

        Observable<Response<ReNewSerialKeyResponseModel>> resetKeyResponsePayload
                = restClient.getApiService().performRenewKey(new ReNewSerialKeyRequestModel(siNo, deviceId, ""));

        resetKeyResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resetKeyResponse ->
                        {
                            dismissProgressDialog();
                            if (null != resetKeyResponse && resetKeyResponse.isSuccessful() &&
                                    null != resetKeyResponse.body() && null != resetKeyResponse.body().getResponse()
                                    && Integer.parseInt(resetKeyResponse.body().getResponse()) == Constant.LOGGED_SUCCESS) {

                                startLoginActivity();

                            } else {

                                throw new RuntimeException("Login Fail");
                            }
                        },
                        t -> {
                            dismissProgressDialog();
                            Toast.makeText(ReNewActivity.this, R.string.renew_key_fail, Toast.LENGTH_LONG).show();
                        });
    }


}
