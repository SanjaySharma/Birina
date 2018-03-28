package com.example.birina.login;


import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.provider.Telephony;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.EditText;

import android.widget.Toast;

import com.example.birina.Base.BaseActivity;

import com.example.birina.R;
import com.example.birina.backup.BackupActivity;
import com.example.birina.dashboard.DeshBoardActivity;
import com.example.birina.login.model.LogInRequestModel;
import com.example.birina.login.model.LogInResponseModel;
import com.example.birina.network.RestClient;
import com.example.birina.util.BirinaPrefrence;
import com.example.birina.util.ConnectionManager;
import com.example.birina.util.Constant;
import com.example.birina.util.Validation;

import java.util.Collections;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginActivity extends BaseActivity {


    private EditText mEdtSiNo, mEdtPhone, mEdtEmail;



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


        mEdtSiNo = (EditText) findViewById(R.id.siNo);
        mEdtPhone = (EditText) findViewById(R.id.textPhone);

        mEdtEmail = (EditText) findViewById(R.id.textEmail);


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
    }


    private void validateNo() {

        if (!Validation.isFieldEmpty(mEdtPhone)) {

            if (Validation.isMobileValid(mEdtPhone.getText().toString())) {
                if (Validation.isFieldEmpty(mEdtSiNo)) {

                    dismissProgressDialog();

                    Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_si_number),
                            Snackbar.LENGTH_LONG).show();

                } else {

                    LoginApi(mEdtSiNo.getText().toString(), mEdtPhone.getText().toString(), (mEdtEmail.getText() != null ? mEdtEmail.getText().toString() : " "));
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


                                BirinaPrefrence.updateLogInStatus(LoginActivity.this, true);
                                BirinaPrefrence.saveRegisteredNumber(LoginActivity.this, mEdtPhone.getText().toString());

                                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
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
