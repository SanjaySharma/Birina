package com.example.bsecure.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.bsecure.R;
import com.example.bsecure.antivirus.AntivirusActivity;
import com.example.bsecure.backup.BackupActivity;
import com.example.bsecure.junkcleaner.activity.CleanerActivity;
import com.example.bsecure.login.LoginPresenter;
import com.example.bsecure.login.LoginPresenterImp;
import com.example.bsecure.login.LoginView;
import com.example.bsecure.restore.RestoreActivity;


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
        findViewById(R.id.textAntivirus).setOnClickListener(this);
        findViewById(R.id.textTrap).setOnClickListener(this);
        findViewById(R.id.textBackup).setOnClickListener(this);
        findViewById(R.id.textRestoreData).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

           case R.id.cleanerParent:
               startCleanerActivity();
               break;

            case R.id.textAntivirus:
                startAntivirusActivity();
                break;

            case R.id.textTrap:
                startTrackActivity();
                break;

            case R.id.textBackup:
                startBackUpActivity();
                break;

            case R.id.textRestoreData:
                startRestoreActivity();
                break;
        }
    }



    private void startCleanerActivity(){
        startActivity(new Intent(DeshBoardActivity.this, CleanerActivity.class));
    }



    private void startAntivirusActivity(){
        startActivity(new Intent(DeshBoardActivity.this, AntivirusActivity.class));
    }

    private void startTrackActivity(){


        //ContactUtility contactUtility = new ContactUtility(this);
        //contactUtility.deleteContact();
       // contactUtility.deleteSms();

        //startActivity(new Intent(DeshBoardActivity.this, TrackActivity.class));
    }

    private void startBackUpActivity(){
        startActivity(new Intent(DeshBoardActivity.this, BackupActivity.class));
    }

    private void startRestoreActivity(){
        startActivity(new Intent(DeshBoardActivity.this, RestoreActivity.class));
    }




}
