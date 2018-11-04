package com.birina.bsecure.login;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.Toast;

import com.birina.bsecure.Base.BaseActivity;

import com.birina.bsecure.R;
import com.birina.bsecure.dashboard.DeshBoardActivity;
import com.birina.bsecure.dashboard.WebActivity;
import com.birina.bsecure.login.model.LogInRequestModel;
import com.birina.bsecure.login.model.LogInResponseModel;
import com.birina.bsecure.network.RestClient;
import com.birina.bsecure.registration.RegistrationActivity;
import com.birina.bsecure.resetpwd.ForgotPwdActivity;
import com.birina.bsecure.resetpwd.ReNewActivity;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.ConnectionManager;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginActivity extends BaseActivity {


    private EditText mEdtMobile, mEdtPwd;

    final int REQUEST_CODE_ASK_PERMISSIONS = 10001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //navigateFlow();

        ActionBar supportActionBar = getSupportActionBar();


        if (supportActionBar != null)
            supportActionBar.hide();

        initializeClick();

    }

    // Method to initialize id & ClickListener
    private void initializeClick() {


        mEdtMobile = (EditText) findViewById(R.id.mobileNumber);
        mEdtPwd = (EditText) findViewById(R.id.textPwd);


        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();

                if(ConnectionManager.isInternetAvailable(LoginActivity.this)){
                    validateNo();

                }else {
                    dismissProgressDialog();
                    Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.connection_manager_fail),
                            Snackbar.LENGTH_LONG).show();

                }
            }
        });



        findViewById(R.id.forgetPwd).setOnClickListener((View v) ->{

            Intent browserIntent = new Intent(LoginActivity.this, ForgotPwdActivity.class);
            startActivity(browserIntent);
        });

        findViewById(R.id.btn_signUp).setOnClickListener((View v) ->{

            Intent browserIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(browserIntent);
            finish();
        });


        findViewById(R.id.support).setOnClickListener((View v) ->{

            Intent webIntent = new Intent(LoginActivity.this, WebActivity.class);
            webIntent.putExtra(Constant.WEB_INTENT_KEY, Constant.URL_SUPPORT);
            startActivity(webIntent);

        });
    }


    private void validateNo() {

        if (!Validation.isFieldEmpty(mEdtMobile)) {

            if (Validation.isMobileValid(mEdtMobile.getText().toString())) {

                    if (Validation.isFieldEmpty(mEdtPwd)) {

                        dismissProgressDialog();

                        Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_pwd_number),
                                Snackbar.LENGTH_LONG).show();

                    } else {
                        callApi();
                    }

            } else {

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
* This method check is user loggedIn or not.
* Already loggedIn navigate to Dashboard.
* Not logged in show login UI.
* */

    public void navigateFlow() {

        if (BirinaPrefrence.isLoggedIn(LoginActivity.this)) {
            startDashBoardActivity();
        }
    }


    private void startDashBoardActivity() {
        Intent intent = new Intent(LoginActivity.this, DeshBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
  private void startReNewActivity() {
        startActivity(new Intent(LoginActivity.this, ReNewActivity.class));
        finish();
    }



    private void callApi(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS
                    ,Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS
                    ,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
        } else {

            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String imei =  telephonyManager.getDeviceId();
            LoginApi(mEdtPwd.getText().toString(), mEdtMobile.getText().toString(), imei);

        }

    }

    private void LoginApi(String pwd, String phone, String deviceId) {

        RestClient restClient = new RestClient();

        Observable<Response<LogInResponseModel>> loginResponsePayload
                = restClient.getApiService().performLogin(new LogInRequestModel(pwd, phone, deviceId));

        loginResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(elabelResponse ->
                        {
                            dismissProgressDialog();
                            if (null != elabelResponse && elabelResponse.isSuccessful() &&
                                    null != elabelResponse.body() && null != elabelResponse.body().getResponse()
                                    && elabelResponse.body().getResponse() != Constant.LOGGED_FAILURE) {

                                if(elabelResponse.body().getResponse() == Constant.LOGGED_SUCCESS){

                                    BirinaPrefrence.saveUserName(LoginActivity.this, elabelResponse.body().getUserName());
                                    BirinaPrefrence.updateLogInStatus(LoginActivity.this, true);
                                    BirinaPrefrence.updateTempLogInStatus(LoginActivity.this, true);
                                    BirinaPrefrence.saveRegisteredNumber(LoginActivity.this, elabelResponse.body().getUserMobile());
                                    BirinaPrefrence.saveExpireDate(LoginActivity.this, elabelResponse.body().getEndDate());
                                    BirinaPrefrence.saveLoginPwd(LoginActivity.this, pwd);
                                    BirinaPrefrence.saveDeviceId(LoginActivity.this, deviceId);
                                    BirinaPrefrence.saveSiNo(LoginActivity.this, elabelResponse.body().getSiNo());

                                    startDashBoardActivity();

                                }else if(elabelResponse.body().getResponse() == Constant.DEVICE_CHANGED) {
                                    startReNewActivity();
                                }

                            } else {

                                // startDashBoardActivity();

                                throw new RuntimeException("Login Fail");
                            }
                        },
                        t -> {
                            dismissProgressDialog();
                            // startDashBoardActivity();

                            Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_LONG).show();
                        });
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

}
