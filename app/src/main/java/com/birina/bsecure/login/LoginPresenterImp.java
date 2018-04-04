package com.example.bsecure.login;

import android.text.TextUtils;

/**
 * Created by Admin on 7/12/2017.
 */

public class LoginPresenterImp implements LoginPresenter {

    private LoginView mLoginView;

    public LoginPresenterImp(LoginView loginView){
        this.mLoginView = loginView;
        mLoginView.setPresenter(this);
    }

    @Override
    public void doLoginValidate(String email, String pass) {
            if(TextUtils.isEmpty(email)){
                mLoginView.onFailure("error");
            }else{
                mLoginView.onSuccess();
            }
    }



    @Override
    public void getData() {

        mLoginView.setData("user");
    }
}
