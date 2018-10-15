package com.birina.bsecure.resetpwd;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
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
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.ConnectionManager;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ReNewActivity extends BaseActivity {


    private EditText mEdtReNewValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew);
        navigateFlow();

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

                        resetKey(mEdtReNewValue.getText().toString());
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


    private void resetKey(String siNo) {

        RestClient restClient = new RestClient();

        Observable<Response<ForgotPwdResponseModel>> resetKeyResponsePayload
                = restClient.getApiService().performResetKey(new ForgotPwdRequestModel(siNo));

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
