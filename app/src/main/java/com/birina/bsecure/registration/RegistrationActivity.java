package com.birina.bsecure.registration;


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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.dashboard.DeshBoardActivity;
import com.birina.bsecure.login.LoginActivity;
import com.birina.bsecure.network.RestClient;
import com.birina.bsecure.registration.model.RegistrationRequestModel;
import com.birina.bsecure.registration.model.RegistrationResponseModel;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.ConnectionManager;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RegistrationActivity extends BaseActivity {


    private EditText mEdtName,mEdtSiNo, mEdtPhone, mEdtEmail,mEdtCreatePwd,mEdtConfPwd,mEdtGender,
            mEdtDob;

    final int REQUEST_CODE_ASK_PERMISSIONS = 10009;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        navigateFlow();

        ActionBar supportActionBar = getSupportActionBar();


        if (supportActionBar != null)
            supportActionBar.hide();

        //  AddUser();
        initializeClick();

    }

    // Method to initialize id & ClickListener
    private void initializeClick() {


        mEdtName = (EditText) findViewById(R.id.userName);
        mEdtSiNo = (EditText) findViewById(R.id.siNo);
        mEdtPhone = (EditText) findViewById(R.id.textPhone);
        mEdtEmail = (EditText) findViewById(R.id.textEmail);
        mEdtCreatePwd = (EditText) findViewById(R.id.password);
        mEdtConfPwd = (EditText) findViewById(R.id.confPassword);
        mEdtGender = (EditText) findViewById(R.id.gender);
        mEdtDob = (EditText) findViewById(R.id.textdob);

        findViewById(R.id.btn_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();

                if(ConnectionManager.isInternetAvailable(RegistrationActivity.this)){
                    validateNo();

                }else {
                    dismissProgressDialog();
                    Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.connection_manager_fail),
                            Snackbar.LENGTH_LONG).show();

                }
            }
        });

        findViewById(R.id.login).setOnClickListener((View v) ->{

            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }


    private void validateNo() {

        if (!Validation.isFieldEmpty(mEdtPhone)) {

            if (Validation.isMobileValid(mEdtPhone.getText().toString())) {
                if (Validation.isFieldEmpty(mEdtSiNo)) {

                    dismissProgressDialog();

                    Snackbar.make(findViewById(R.id.registrationParent), getResources().getString(R.string.fill_si_number),
                            Snackbar.LENGTH_LONG).show();

                } else {

                    if (Validation.isFieldEmpty(mEdtName)) {

                        dismissProgressDialog();

                        Snackbar.make(findViewById(R.id.registrationParent), getResources().getString(R.string.fill_user_name),
                                Snackbar.LENGTH_LONG).show();

                    } else {

                        if (Validation.isFieldEmpty(mEdtGender)) {

                            dismissProgressDialog();

                            Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                            .getString(R.string.fill_gender),
                                    Snackbar.LENGTH_LONG).show();

                        } else {

                            if (Validation.isFieldEmpty(mEdtDob)) {

                                dismissProgressDialog();

                                Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                                .getString(R.string.fill_dob),
                                        Snackbar.LENGTH_LONG).show();

                            } else {
                                if (Validation.isFieldEmpty(mEdtCreatePwd)) {

                                    dismissProgressDialog();

                                    Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                                    .getString(R.string.fill_pwd),
                                            Snackbar.LENGTH_LONG).show();

                                } else {

                                    if (Validation.isFieldEmpty(mEdtConfPwd)) {

                                        dismissProgressDialog();

                                        Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                                        .getString(R.string.fill_conf_pwd),
                                                Snackbar.LENGTH_LONG).show();

                                    } else {
                                        if (!mEdtConfPwd.getText().toString().equals(mEdtCreatePwd.getText().toString())) {

                                            dismissProgressDialog();

                                            Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                                            .getString(R.string.password_not_match),
                                                    Snackbar.LENGTH_LONG).show();

                                        } else {
                                             callApi();
                                        }

                                    }

                                }

                            }

                        }
                    }
                }

            } else {

                dismissProgressDialog();

                Snackbar.make(findViewById(R.id.registrationParent), getResources().getString(R.string.reg_invalid_mobile),
                        Snackbar.LENGTH_LONG).show();
            }


        } else {

            dismissProgressDialog();

            Snackbar.make(findViewById(R.id.registrationParent), getResources().getString(R.string.fill_mobile_number),
                    Snackbar.LENGTH_LONG).show();


        }

    }

/*
* This method check is user loggedIn or not.
* Already loggedIn navigate to Dashboard.
* Not logged in show login UI.
* */

    public void navigateFlow() {

        if (BirinaPrefrence.isLoggedIn(RegistrationActivity.this)) {
            startDashBoardActivity();
        }
    }


    private void startLoginActivity() {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        finish();
    }

    private void startDashBoardActivity() {
        startActivity(new Intent(RegistrationActivity.this, DeshBoardActivity.class));
        finish();
    }

    private void callApi(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS
                    ,Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
        } else {

            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String imei =  telephonyManager.getDeviceId();
            RegistrationApi(mEdtSiNo.getText().toString(), mEdtPhone.getText().toString(),
                    (mEdtEmail.getText() != null ? mEdtEmail.getText().toString() : " "), imei
                    , mEdtName.getText().toString(), mEdtCreatePwd.getText().toString(),
                    mEdtDob.getText().toString());

        }

    }

    private void RegistrationApi(String siNo, String phone, String email,  String imei,  String name
            , String password, String dob) {

        RestClient restClient = new RestClient();

        Observable<Response<RegistrationResponseModel>> registrationResponsePayload
                = restClient.getApiService().performRegistration(
                        new RegistrationRequestModel( email,siNo, phone, name, imei, password, dob));


        registrationResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(elabelResponse ->
                        {
                            dismissProgressDialog();
                            if (null != elabelResponse && elabelResponse.isSuccessful() &&
                                    null != elabelResponse.body() && null != elabelResponse.body().getResponse()
                                    && elabelResponse.body().getResponse() == Constant.LOGGED_SUCCESS) {

                                BirinaPrefrence.saveUserName(RegistrationActivity.this, mEdtName.getText().toString());
                                BirinaPrefrence.saveRegisteredNumber(RegistrationActivity.this, mEdtPhone.getText().toString());
                                BirinaPrefrence.saveExpireDate(RegistrationActivity.this, elabelResponse.body().getEnddate());

                                startLoginActivity();

                            } else {

                                // startDashBoardActivity();

                                throw new RuntimeException("Registration Fail");
                            }
                        },
                        t -> {
                            dismissProgressDialog();
                            // startDashBoardActivity();

                            Toast.makeText(RegistrationActivity.this, "Registration Fail", Toast.LENGTH_LONG).show();
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
