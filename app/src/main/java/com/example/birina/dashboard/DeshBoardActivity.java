package com.example.birina.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.birina.R;
import com.example.birina.junkcleaner.activity.CleanerActivity;
import com.example.birina.login.LoginPresenter;
import com.example.birina.login.LoginPresenterImp;
import com.example.birina.login.LoginView;
import com.example.birina.util.Constant;


public class DeshBoardActivity extends AppCompatActivity implements LoginView, View.OnClickListener {

    private LoginPresenter mLoginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        new LoginPresenterImp(this);

     //   mLoginPresenter.getData();

        ActionBar supportActionBar = getSupportActionBar();


        if (supportActionBar != null)
            supportActionBar.hide();

        setUpClickEvents();
    }


    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String error) {

    }

    @Override
    public void setData(String t) {

    }

    //private method
    public void login(View loginView){
        mLoginPresenter.doLoginValidate("user name","pass");
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void setPresenter(Object presenter) {

    }


    private void setUpClickEvents(){

        findViewById(R.id.cleanerParent).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

           case R.id.cleanerParent:
               startCleanerActivity();
               break;
        }
    }



    private void startCleanerActivity(){
        startActivity(new Intent(DeshBoardActivity.this, CleanerActivity.class));
    }
}
