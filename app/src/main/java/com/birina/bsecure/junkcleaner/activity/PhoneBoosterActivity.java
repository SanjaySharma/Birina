package com.birina.bsecure.junkcleaner.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.birina.bsecure.R;
import com.birina.bsecure.login.LoginActivity;
import com.birina.bsecure.login.TempLoginActivity;
import com.birina.bsecure.registration.RegistrationActivity;
import com.birina.bsecure.util.BirinaPrefrence;

import java.io.File;
import java.text.DecimalFormat;


public class PhoneBoosterActivity extends AppCompatActivity {

    private TextView mTxtRamUsage, mTxtRamPercentage, mTxtExternalUsage, mTxtExternalPercentage,
            mTxtInternalUsage, mTxtInternalPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigateFlow();

        setContentView(R.layout.activity_phonebooster);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.phone_booster);


        initializeView();

        setMemory();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void initializeView(){

        mTxtRamUsage = (TextView) findViewById(R.id.ram_usage);
        mTxtRamPercentage = (TextView) findViewById(R.id.ram_percentage);
        mTxtExternalUsage = (TextView) findViewById(R.id.external_usage);
        mTxtExternalPercentage = (TextView) findViewById(R.id.external_percentage);
        mTxtInternalUsage = (TextView) findViewById(R.id.internal_usage);
        mTxtInternalPercentage = (TextView) findViewById(R.id.internal_percentage);


        findViewById(R.id.btn_boost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(PhoneBoosterActivity.this, CleanerActivity.class));
            }
        });


    }


    private void setMemory(){

        long totalRamValue = totalRamMemorySize();
        long freeRamValue = freeRamMemorySize();
        long usedRamValue = totalRamValue - freeRamValue;
        long ramPercentage = (usedRamValue*100)/totalRamValue;



        long totalInternalValue = getTotalInternalMemorySize();
        long freeInternalValue = getAvailableInternalMemorySize();
        long usedInternalValue = totalInternalValue - freeInternalValue;
        long internalPercentage = (usedInternalValue*100 )/totalInternalValue;


        long totalExternalValue = getTotalExternalMemorySize();
        long freeExternalValue = getAvailableExternalMemorySize();
        long usedExternalValue = totalExternalValue - freeExternalValue;
        long externalPercentage = (usedExternalValue*100)/totalExternalValue;



        mTxtRamUsage.setText( formatSize(usedRamValue)+ "/" + formatSize(totalRamValue));

        mTxtExternalUsage.setText(formatSize(usedExternalValue)+ "/" + formatSize(totalExternalValue));

        mTxtInternalUsage.setText(formatSize(usedInternalValue)+ "/" + formatSize(totalInternalValue));

        mTxtRamPercentage.setText(""+ramPercentage+"%");
        mTxtInternalPercentage.setText(""+internalPercentage+"%");
        mTxtExternalPercentage.setText(""+externalPercentage+"%");

        manageExternalVariables(totalInternalValue, freeInternalValue, totalExternalValue, freeExternalValue);
    }


    private void manageExternalVariables(long totalInternalValue, long freeInternalValue,
                                         long totalExternalValue, long freeExternalValue){

        Boolean isSDPresent = android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();

        if(isSDSupportedDevice && isSDPresent)
        {
            // yes SD-card is present
        }
        else
        {
            mTxtExternalUsage.setVisibility(View.VISIBLE);
            mTxtExternalPercentage.setVisibility(View.VISIBLE);
            findViewById(R.id.external_heading).setVisibility(View.VISIBLE);
        }
      /*  if((totalInternalValue != totalExternalValue) &&(freeInternalValue != freeExternalValue)){
            mTxtExternalUsage.setVisibility(View.VISIBLE);
            mTxtExternalPercentage.setVisibility(View.VISIBLE);
            findViewById(R.id.external_heading).setVisibility(View.VISIBLE);
        }*/
    }

    private long freeRamMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem ;

        return availableMegs;
    }

    private long totalRamMemorySize() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.totalMem ;
        return availableMegs;
    }


    public  boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public  long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }


    public  long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public  long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

    public  long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return 0;
        }
    }



    public  String formatSize(float size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;

                if (size >= 1024) {
                    suffix = " GB";
                    size /= 1024;
                }
            }
        }
        StringBuilder resultBuffer = new StringBuilder(returnToDecimalPlaces(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            commaOffset -= 3;
        }
        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    private String returnToDecimalPlaces(float values){
        DecimalFormat df = new DecimalFormat("#.0");
        String angleFormated = df.format(values);
        return angleFormated;
    }




    public void navigateFlow() {

        if (!BirinaPrefrence.isLoggedIn(PhoneBoosterActivity.this) ) {
            startRegistrationActivity();
        }if (BirinaPrefrence.isLoggedIn(PhoneBoosterActivity.this)
                && !BirinaPrefrence.isTempLoggedIn(PhoneBoosterActivity.this) ) {
            startTempLoginActivity();
        }
    }



    private void startRegistrationActivity() {
        startActivity(new Intent(PhoneBoosterActivity.this, RegistrationActivity.class));
        finish();
    }

    private void startTempLoginActivity() {
        startActivity(new Intent(PhoneBoosterActivity.this, TempLoginActivity.class));
        finish();
    }

}
