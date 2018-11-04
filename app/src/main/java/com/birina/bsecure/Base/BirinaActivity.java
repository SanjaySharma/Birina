package com.birina.bsecure.Base;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.birina.bsecure.login.LoginActivity;
import com.birina.bsecure.resetpwd.ReNewActivity;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.BirinaProgressDialog;
import com.birina.bsecure.util.BirinaUtility;

import java.util.List;


/**
 * Created by 911335 on 8/31/2016.
 */
public class BirinaActivity extends AppCompatActivity {


    // private static ProgressDialog progressDialog ;
    private BirinaProgressDialog progressDialog ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new BirinaProgressDialog(this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


       // checkExpireStatus();


    }



    @Override
    protected void onResume() {
        super.onResume();

       // checkExpireStatus();
    }



    public void checkExpireStatus(){

            if(BirinaUtility.isLicenseExpire(this)){
                BirinaPrefrence.updateLogInStatus(this, false);
                startActivity(new Intent(BirinaActivity.this, ReNewActivity.class));
                finish();
                return;
            }
    }

    public void showProgressDialog(){
        try {
            if (null != progressDialog) {
                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
                progressDialog.show();
            }
        }catch (Exception e){
            Log.e("Exception "," >>> "+e);
        }
    }

    public void dismissProgressDialog(){
        try {
            if(null != progressDialog){
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        }catch (Exception e){
            Log.e("Exception "," >>> "+e);
        }
    }



}
