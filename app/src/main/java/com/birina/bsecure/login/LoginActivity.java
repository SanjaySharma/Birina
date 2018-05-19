package com.birina.bsecure.login;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.View;

import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.Toast;

import com.birina.bsecure.Base.BaseActivity;

import com.birina.bsecure.R;
import com.birina.bsecure.dashboard.DeshBoardActivity;
import com.birina.bsecure.login.model.LogInRequestModel;
import com.birina.bsecure.login.model.LogInResponseModel;
import com.birina.bsecure.network.RestClient;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.BirinaUtility;
import com.birina.bsecure.util.ConnectionManager;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginActivity extends BaseActivity {


    private EditText mEdtName,mEdtSiNo, mEdtPhone, mEdtEmail;
    private CheckBox mPrivacyCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        mPrivacyCheck = (CheckBox) findViewById(R.id.privacyCheck);

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

        findViewById(R.id.privacyLink).setOnClickListener((View v) ->{

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://birinagroup.com/privacy-policy.html"));
            startActivity(browserIntent);
        });
    }


    private void validateNo() {

        if (!Validation.isFieldEmpty(mEdtPhone)) {

            if (Validation.isMobileValid(mEdtPhone.getText().toString())) {
                if (Validation.isFieldEmpty(mEdtSiNo)) {

                    dismissProgressDialog();

                    Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_si_number),
                            Snackbar.LENGTH_LONG).show();

                } else {

                    if (Validation.isFieldEmpty(mEdtName)) {

                        dismissProgressDialog();

                        Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_user_name),
                                Snackbar.LENGTH_LONG).show();

                    } else {

                        if (!mPrivacyCheck.isChecked()) {

                            dismissProgressDialog();

                            Snackbar.make(findViewById(R.id.loginperant), getResources()
                                            .getString(R.string.fill_check_privacy),
                                    Snackbar.LENGTH_LONG).show();

                        } else {

                            LoginApi(mEdtSiNo.getText().toString(), mEdtPhone.getText().toString(), (mEdtEmail.getText() != null ? mEdtEmail.getText().toString() : " "));
                        }
                    }
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
        startActivity(new Intent(LoginActivity.this, DeshBoardActivity.class));
        finish();
    }


    private void LoginApi(String siNo, String phone, String email) {

        RestClient restClient = new RestClient();

        Observable<Response<LogInResponseModel>> loginResponsePayload
                = restClient.getApiService().performLogin(new LogInRequestModel(siNo, phone, email));

        loginResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(elabelResponse ->
                        {
                            dismissProgressDialog();
                            if (null != elabelResponse && elabelResponse.isSuccessful() &&
                                    null != elabelResponse.body() && null != elabelResponse.body().getResponse()
                                    && Integer.parseInt(elabelResponse.body().getResponse()) == Constant.LOGGED_SUCCESS) {

                                BirinaPrefrence.saveUserName(LoginActivity.this, mEdtName.getText().toString());
                                BirinaPrefrence.updateLogInStatus(LoginActivity.this, true);
                                BirinaPrefrence.saveRegisteredNumber(LoginActivity.this, mEdtPhone.getText().toString());
                                BirinaPrefrence.saveExpireDate(LoginActivity.this, elabelResponse.body().getEnddate());


                                startDashBoardActivity();

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




}
