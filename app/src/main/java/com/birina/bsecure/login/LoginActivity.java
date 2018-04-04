package com.example.bsecure.login;


import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.View;

import android.widget.EditText;

import android.widget.Toast;

import com.example.bsecure.Base.BaseActivity;

import com.example.bsecure.R;
import com.example.bsecure.dashboard.DeshBoardActivity;
import com.example.bsecure.login.model.LogInRequestModel;
import com.example.bsecure.login.model.LogInResponseModel;
import com.example.bsecure.network.RestClient;
import com.example.bsecure.util.BirinaPrefrence;
import com.example.bsecure.util.ConnectionManager;
import com.example.bsecure.util.Constant;
import com.example.bsecure.util.Validation;

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
