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
import android.telephony.TelephonyManager;
import android.view.View;
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


public class TempLoginActivity extends BaseActivity {


    private EditText  mEdtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templogin);

        navigateFlow();

        ActionBar supportActionBar = getSupportActionBar();


        if (supportActionBar != null)
            supportActionBar.hide();

        initializeClick();

    }

    // Method to initialize id & ClickListener
    private void initializeClick() {


        mEdtPwd = (EditText) findViewById(R.id.temp_textPwd);


        findViewById(R.id.btn_temp_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ConnectionManager.isInternetAvailable(TempLoginActivity.this)){
                    validateNo();

                }else {

                    Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.connection_manager_fail),
                            Snackbar.LENGTH_LONG).show();

                }
            }
        });



        findViewById(R.id.tempForgetPwd).setOnClickListener((View v) ->{

            Intent browserIntent = new Intent(TempLoginActivity.this, ForgotPwdActivity.class);
            startActivity(browserIntent);
        });

    }


    private void validateNo() {

                    if (Validation.isFieldEmpty(mEdtPwd)) {

                        dismissProgressDialog();

                        Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.fill_login_pwd),
                                Snackbar.LENGTH_LONG).show();

                    } else {
                        LoginApi(mEdtPwd.getText().toString());
                    }




    }

/*
* This method check is user loggedIn or not.
* Already loggedIn navigate to Dashboard.
* Not logged in show login UI.
* */

    public void navigateFlow() {

        if (BirinaPrefrence.isLoggedIn(TempLoginActivity.this)
                && BirinaPrefrence.isTempLoggedIn(TempLoginActivity.this) ) {
            startDashBoardActivity();
        }
    }


    private void startDashBoardActivity() {
        startActivity(new Intent(TempLoginActivity.this, DeshBoardActivity.class));
        finish();
    }



    private void LoginApi(String pwd) {

        if(pwd.equals(BirinaPrefrence.getLoginPwd(TempLoginActivity.this))){
            BirinaPrefrence.updateTempLogInStatus(TempLoginActivity.this, true);
            startDashBoardActivity();
        }else{
            Toast.makeText(TempLoginActivity.this, R.string.wrong_pwd, Toast.LENGTH_LONG).show();
        }
    }




}
