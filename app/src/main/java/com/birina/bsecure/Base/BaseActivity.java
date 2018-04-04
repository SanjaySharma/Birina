package com.example.bsecure.Base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.bsecure.util.BirinaProgressDialog;


/**
 * Created by 911335 on 8/31/2016.
 */
public class BaseActivity extends AppCompatActivity {


    // private static ProgressDialog progressDialog ;
    private BirinaProgressDialog progressDialog ;



   /* public TCSCCTBaseActivity(){
        Log.d("TcsCCTBaseActivity","Tcs CCT base activity default constructor is called.");
        LocaleUtil.updateConfig(this);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new BirinaProgressDialog(this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



/*        UserSessionModel userSessionModel = UserSessionBO.getUserSessionDataFromTable(CCTControllerApplication.getInstance(), new EncryptionDecryptionUtil());

        if(userSessionModel != null) {
            CCTControllerApplication.getInstance().setLocale(userSessionModel.getUser().getmCountry(),
                    userSessionModel.getUser().getmLanguage());
        }*/

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
