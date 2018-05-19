package com.birina.bsecure.Base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.BirinaProgressDialog;
import com.birina.bsecure.util.BirinaUtility;


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


        Log.d("BirinaActivity "," onCreate() called ");

       // checkExpireStatus();


    }



    @Override
    protected void onResume() {
        super.onResume();

        Log.d("BirinaActivity "," onResume() called ");

       // checkExpireStatus();
    }

    public void checkExpireStatus(){

            if(BirinaUtility.isLicenseExpire(this)){
                BirinaPrefrence.updateLogInStatus(this, false);
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
