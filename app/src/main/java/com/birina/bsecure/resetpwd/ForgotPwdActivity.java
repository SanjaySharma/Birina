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
import android.widget.TextView;
import android.widget.Toast;

import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.dashboard.DeshBoardActivity;
import com.birina.bsecure.login.LoginActivity;
import com.birina.bsecure.network.RestClient;
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


public class ForgotPwdActivity extends BaseActivity {


    private EditText mEdtForgotValue;
    private TextView mEdtForgotLabel;

    private boolean isOtpReqSuccess = false;
    private  String mobileNo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        isOtpReqSuccess = false;
       // navigateFlow();

        ActionBar supportActionBar = getSupportActionBar();


        if (supportActionBar != null)
            supportActionBar.hide();

        //  AddUser();
        initializeClick();

    }

    // Method to initialize id & ClickListener
    private void initializeClick() {


        mEdtForgotValue = (EditText) findViewById(R.id.textResPwd);
        mEdtForgotLabel = (TextView) findViewById(R.id.forgotTxtMobileNo);




        findViewById(R.id.btn_forgotPwdSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();

                if(ConnectionManager.isInternetAvailable(ForgotPwdActivity.this)){

                    if(isOtpReqSuccess){
                       validateOTP();
                    }else {
                        validateNo();
                    }

                }else {
                    dismissProgressDialog();
                    Snackbar.make(findViewById(R.id.forgotParent), getResources().getString(R.string.connection_manager_fail),
                            Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }


    private void validateNo() {

        if (!Validation.isFieldEmpty(mEdtForgotValue)) {

            if (Validation.isMobileValid(mEdtForgotValue.getText().toString())) {

                mobileNo = mEdtForgotValue.getText().toString();

                performPwdRecovery( mEdtForgotValue.getText().toString());

            } else {

                dismissProgressDialog();

                Snackbar.make(findViewById(R.id.forgotParent), getResources().getString(R.string.reg_invalid_mobile),
                        Snackbar.LENGTH_LONG).show();
            }
        } else {

            dismissProgressDialog();

            Snackbar.make(findViewById(R.id.forgotParent), getResources().getString(R.string.fill_mobile_number),
                    Snackbar.LENGTH_LONG).show();
        }

    }


    private void validateOTP() {

                    if (Validation.isFieldEmpty(mEdtForgotValue)) {

                        dismissProgressDialog();

                        Snackbar.make(findViewById(R.id.forgotParent), getResources().getString(R.string.fill_user_name),
                                Snackbar.LENGTH_LONG).show();

                    } else {

                        performVerifyOtp( mobileNo ,mEdtForgotValue.getText().toString());

                    }
        }



/*
* This method check is user loggedIn or not.
* Already loggedIn navigate to Dashboard.
* Not logged in show login UI.
* */

    public void navigateFlow() {

        if (BirinaPrefrence.isLoggedIn(ForgotPwdActivity.this)) {
            startDashBoardActivity();
        }
    }


    private void startDashBoardActivity() {
        startActivity(new Intent(ForgotPwdActivity.this, DeshBoardActivity.class));
        finish();
    }


    private void startResetPasswordActivity() {

        Intent intent = new Intent(ForgotPwdActivity.this, ResetPwdActivity.class);
        intent.putExtra(Constant.RESET_PWD_KEY, mobileNo);
        startActivity(intent);
        finish();
    }



    private void performPwdRecovery(String phone) {

        RestClient restClient = new RestClient();
        Observable<Response<ForgotPwdResponseModel>> recoveryResponsePayload
                = restClient.getApiService().performPwdRecovery(new ForgotPwdRequestModel( phone, "", ""));

        recoveryResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recoveryResponse ->
                        {
                            dismissProgressDialog();
                            if (null != recoveryResponse && recoveryResponse.isSuccessful() &&
                                    null != recoveryResponse.body() && null != recoveryResponse.body().getResponse()
                                    && Integer.parseInt(recoveryResponse.body().getResponse()) == Constant.LOGGED_SUCCESS) {

                                isOtpReqSuccess = true;
                                mEdtForgotLabel.setText(R.string.otp);
                                mEdtForgotValue.setText("");
                                Toast.makeText(ForgotPwdActivity.this, recoveryResponse.body().getMsg(), Toast.LENGTH_LONG).show();

                            } else {

                                throw new RuntimeException("Login Fail");
                            }
                        },
                        t -> {
                            dismissProgressDialog();
                            Toast.makeText(ForgotPwdActivity.this, R.string.forgot_reg_fail, Toast.LENGTH_LONG).show();
                        });
    }


    private void performVerifyOtp(String phone, String otp) {

        RestClient restClient = new RestClient();

        Observable<Response<ForgotPwdResponseModel>> verifyOtpResponsePayload
                = restClient.getApiService().performVerifyOtp(new ForgotPwdRequestModel( phone, otp, ""));

        verifyOtpResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recoveryResponse ->
                        {
                            dismissProgressDialog();
                            if (null != recoveryResponse && recoveryResponse.isSuccessful() &&
                                    null != recoveryResponse.body() && null != recoveryResponse.body().getResponse()
                                    && Integer.parseInt(recoveryResponse.body().getResponse()) == Constant.LOGGED_SUCCESS) {

                                isOtpReqSuccess = true;
/*
                                Toast.makeText(ForgotPwdActivity.this, recoveryResponse.body().getMsg(),
                                        Toast.LENGTH_LONG).show();*/

                                startResetPasswordActivity();

                            } else {

                                throw new RuntimeException("Login Fail");
                            }
                        },
                        t -> {
                            dismissProgressDialog();
                            Toast.makeText(ForgotPwdActivity.this, R.string.forgot_reg_fail, Toast.LENGTH_LONG).show();
                        });
    }


}
