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
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.dashboard.DeshBoardActivity;
import com.birina.bsecure.login.LoginActivity;
import com.birina.bsecure.network.RestClient;
import com.birina.bsecure.registration.RegistrationActivity;
import com.birina.bsecure.resetpwd.model.ForgotPwdRequestModel;
import com.birina.bsecure.resetpwd.model.ForgotPwdResponseModel;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.ConnectionManager;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ResetPwdActivity extends BaseActivity {


    private EditText mEdtResPwd,mEdtResConfPwd;
    private  String mobileNo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        Intent intent = getIntent();
        if(null != intent){
            mobileNo = intent.getStringExtra(Constant.RESET_PWD_KEY);
        }
       // navigateFlow();

        ActionBar supportActionBar = getSupportActionBar();


        if (supportActionBar != null)
            supportActionBar.hide();

        //  AddUser();
        initializeClick();

    }

    // Method to initialize id & ClickListener
    private void initializeClick() {


        mEdtResPwd = (EditText) findViewById(R.id.textResPwd);
        mEdtResConfPwd = (EditText) findViewById(R.id.textResConfPwd);


        findViewById(R.id.btn_reset_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();

                if(ConnectionManager.isInternetAvailable(ResetPwdActivity.this)){
                    validateNo();

                }else {
                    dismissProgressDialog();
                    Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.connection_manager_fail),
                            Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }


    private void validateNo() {

        if (Validation.isFieldEmpty(mEdtResPwd)) {

            dismissProgressDialog();

            Snackbar.make(findViewById(R.id.resetPwdParent), getResources()
                            .getString(R.string.fill_pwd),
                    Snackbar.LENGTH_LONG).show();

        } else {

            if (Validation.isFieldEmpty(mEdtResConfPwd)) {

                dismissProgressDialog();

                Snackbar.make(findViewById(R.id.resetPwdParent), getResources()
                                .getString(R.string.fill_conf_pwd),
                        Snackbar.LENGTH_LONG).show();

            } else {
                if (!mEdtResPwd.getText().toString().equals(mEdtResConfPwd.getText().toString())) {

                    dismissProgressDialog();

                    Snackbar.make(findViewById(R.id.resetPwdParent), getResources()
                                    .getString(R.string.password_not_match),
                            Snackbar.LENGTH_LONG).show();

                } else {
                    resetPwd(mobileNo, mEdtResPwd.getText().toString());
                }

            }
        }
    }


/*
* This method check is user loggedIn or not.
* Already loggedIn navigate to Dashboard.
* Not logged in show login UI.
* */

    public void navigateFlow() {

        if (BirinaPrefrence.isLoggedIn(ResetPwdActivity.this)) {
            startDashBoardActivity();
        }
    }


    private void startDashBoardActivity() {
        startActivity(new Intent(ResetPwdActivity.this, DeshBoardActivity.class));
        finish();
    }

    private void startLoginActivity() {
        startActivity(new Intent(ResetPwdActivity.this, LoginActivity.class));
        finish();
    }


    private void resetPwd(String phone, String pwd) {

        RestClient restClient = new RestClient();

        Observable<Response<ForgotPwdResponseModel>> reSetResponsePayload
                = restClient.getApiService().performResetPwd(new ForgotPwdRequestModel( phone, pwd));

        reSetResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reSetResponse ->
                        {
                            dismissProgressDialog();
                            if (null != reSetResponse && reSetResponse.isSuccessful() &&
                                    null != reSetResponse.body() && null != reSetResponse.body().getResponse()
                                    && Integer.parseInt(reSetResponse.body().getResponse()) == Constant.LOGGED_SUCCESS) {

                                startLoginActivity();

                            } else {

                                throw new RuntimeException("Login Fail");
                            }
                        },
                        t -> {
                            dismissProgressDialog();
                            Toast.makeText(ResetPwdActivity.this, R.string.reset_pwd_fail, Toast.LENGTH_LONG).show();
                        });
    }



}
