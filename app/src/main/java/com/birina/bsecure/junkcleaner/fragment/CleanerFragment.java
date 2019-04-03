package com.birina.bsecure.junkcleaner.fragment;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.birina.bsecure.R;
import com.birina.bsecure.junkcleaner.model.AppsListAdapter;
import com.birina.bsecure.junkcleaner.model.AppsListItem;
import com.birina.bsecure.junkcleaner.model.CleanerService;

import java.util.ArrayList;
import java.util.List;

public class CleanerFragment extends Fragment implements CleanerService.OnActionListener {
    public  static  String TAG = "Cache_Clear";
    private static final int REQUEST_STORAGE = 0;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private CleanerService mCleanerService;
    private AppsListAdapter mAppsListAdapter;
    //private TextView mEmptyView;
    private SharedPreferences mSharedPreferences;
   // private View mProgressBar;
    //private TextView mProgressBarText;
    private LinearLayoutManager mLayoutManager;


    private boolean mAlreadyScanned = false;
    private boolean mAlreadyCleaned = false;

    private String mCleanOnAppStartupKey;
    private String mExitAfterCleanKey;

    private ProgressBar mProgressBar;
    private TextView mProgressBarText;
    private double mProgressRate;
    private int mProgressCount = 0;
    private TextView mJunkAmount, mJunkMeter;
    private RelativeLayout cleanCircle;
    private TextView mCleanAmount, mCleanMeter;
    private ProgressBar mCleanProgressBar;
    private TextView mCleanProgressBarText;

    private RelativeLayout scanningButton;
    private RelativeLayout mCleanView, mScanView;


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"Enter in onServiceConnected of  CleanerFragment ");
            mCleanerService = ((CleanerService.CleanerServiceBinder) service).getService();
            mCleanerService.setOnActionListener(CleanerFragment.this);

            if (!mCleanerService.isCleaning() && !mCleanerService.isScanning()) {
                if (mSharedPreferences.getBoolean(mCleanOnAppStartupKey, false) &&
                        !mAlreadyCleaned) {
                    mAlreadyCleaned = true;

                    cleanCache();
                } else if (!mAlreadyScanned) {
                    mCleanerService.scanCache();
                }
            }
            Log.d(TAG,"Exit from onServiceConnected of  CleanerFragment ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"Enter in onServiceDisconnected of  CleanerFragment ");
            mCleanerService.setOnActionListener(null);
            mCleanerService = null;
            Log.d(TAG,"Exit from onServiceDisconnected of  CleanerFragment ");
        }
    };




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Enter in onCreate of  CleanerFragment ");

        setHasOptionsMenu(true);
        setRetainInstance(true);

        mCleanOnAppStartupKey = getString(R.string.clean_on_app_startup_key);
        mExitAfterCleanKey = getString(R.string.exit_after_clean_key);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mAppsListAdapter = new AppsListAdapter(new ArrayList<AppsListItem>());



        getActivity().getApplication().bindService(new Intent(getActivity(), CleanerService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG,"Exit from onCreate of  CleanerFragment ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cleaner_fragment, container, false);
        Log.d(TAG,"Enter in onServiceDisconnected of  CleanerFragment ");


        mLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAppsListAdapter);

       // recyclerView.setEmptyView(mEmptyView);
        //recyclerView.addItemDecoration(new DividerDecoration(getActivity()));



        mProgressBar = rootView.findViewById(R.id.cacheProgress);
        mProgressBarText = (TextView) rootView.findViewById(R.id.cacheProgressText);
        mJunkAmount = (TextView)rootView.findViewById(R.id.junk_amount);
        mJunkMeter = (TextView)rootView.findViewById(R.id.junk_meter);
        scanningButton = (RelativeLayout)rootView.findViewById(R.id.progressBar);
        mCleanView = (RelativeLayout)rootView.findViewById(R.id.cleanView);
        mScanView = (RelativeLayout)rootView.findViewById(R.id.scan_view);

        mCleanProgressBar = rootView.findViewById(R.id.cleanProgress);
        mCleanProgressBarText = (TextView) rootView.findViewById(R.id.cleanProgressText);
        mCleanAmount = (TextView)rootView.findViewById(R.id.clean_amount);
        mCleanMeter = (TextView)rootView.findViewById(R.id.clean_meter);
        cleanCircle = rootView.findViewById(R.id.btn_clean);

        scanningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanCache();
            }
        });

        deActivateScanningButton();
        Log.d(TAG,"Exit from onCreate of  CleanerFragment ");

        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clean:
                if (mCleanerService != null && !mCleanerService.isScanning() &&
                        !mCleanerService.isCleaning() && mCleanerService.getCacheSize() > 0) {
                    mAlreadyCleaned = false;

                    cleanCache();
                }
                return true;

            case R.id.action_refresh:
                if (mCleanerService != null && !mCleanerService.isScanning() &&
                        !mCleanerService.isCleaning()) {
                    mCleanerService.scanCache();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onResume() {

      /*  if (mCleanerService != null) {
            if (mCleanerService.isScanning() && !isProgressBarVisible()) {
                showProgressBar(true);
            } else if (!mCleanerService.isScanning() && isProgressBarVisible()) {
                showProgressBar(false);
            }

            if (mCleanerService.isCleaning() && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }*/

        super.onResume();
    }



    @Override
    public void onDestroy() {
        getActivity().getApplication().unbindService(mServiceConnection);

        super.onDestroy();
    }








    private void showStorageRationale() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle(R.string.rationale_title);
        dialog.setMessage(getString(R.string.rationale_storage));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialog.show();
    }

    private void cleanCache() {
        if (!CleanerService.canCleanExternalCache(getActivity())) {
            if (shouldShowRequestPermissionRationale(PERMISSIONS_STORAGE[0])) {
                showStorageRationale();
            } else {
                requestPermissions(PERMISSIONS_STORAGE, REQUEST_STORAGE);
            }
        } else {
            mCleanerService.cleanCache();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCleanerService.cleanCache();
            } else {
                showStorageRationale();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onScanStarted(Context context) {
        Log.d(TAG,"Enter in onServiceDisconnected of  CleanerFragment ");

        mProgressRate = 0;
        mProgressCount = 0;
        if(null != getActivity()){
            hideCleanView();
            deActivateScanningButton();
        }
        Log.d(TAG,"Exit from onScanStarted of  CleanerFragment ");

    }




    @Override
    public void onScanProgressUpdated(Context context, List<AppsListItem> apps) {
        Log.d(TAG,"Enter in onServiceDisconnected of  CleanerFragment ");

        if(null != getActivity()) {
            mProgressCount++;
            mAppsListAdapter.updateList(apps);
            manageScanProgress(apps);
            setCacheAmount();
        }
        Log.d(TAG,"Exit from onScanProgressUpdated of  CleanerFragment ");

    }

    @Override
    public void onScanCompleted(Context context, List<AppsListItem> apps) {

        Log.d(TAG," Enter in onScanCompleted ");

        if(null != getActivity()) {

            mAppsListAdapter.updateList(apps);

            mAlreadyScanned = true;

            setCacheAmount();
        }
        Log.d(TAG," Exit from onScanCompleted mTotalCacheAmount");

    }


    @Override
    public void onCleanStarted(Context context) {
        Log.d(TAG,"Enter in onServiceDisconnected of  CleanerFragment ");

        if(null != getActivity()) {

            mProgressRate = 0;
            mProgressCount = 0;
            setCleanValues();
            showCleanView();
            deActivateScanningButton();
            startAnimation();
        }

        Log.e(TAG," onCleanStarted");

    }

    @Override
    public void onCleanProgressUpdated(Context context, int[] cleanData) {

        if(null != getActivity()) {

            mProgressCount++;

            Log.e("onCleanProgressUpdated ", " mProgressCount: " + mProgressCount + " totalFile: " + cleanData[1]);


            manageCleanProgress(cleanData[1]);
        }
    }

    @Override
    public void onCleanCompleted(Context context, boolean succeeded) {
        Log.d(TAG,"Enter in onServiceDisconnected of  CleanerFragment ");

        if(null != getActivity()) {

            if (succeeded) {
                mAppsListAdapter.updateList(new ArrayList<AppsListItem>());
            }

            Toast.makeText(context, succeeded ? R.string.cleaned : R.string.toast_could_not_clean,
                    Toast.LENGTH_LONG).show();

            if (succeeded && getActivity() != null && !mAlreadyCleaned &&
                    mSharedPreferences.getBoolean(mExitAfterCleanKey, false)) {
                // getActivity().finish();
            }
        }
    }


 private void manageScanProgress(List<AppsListItem> itemList){

        if(null != itemList && itemList.size()> 0){

            if(mProgressRate <=0) {
                mProgressRate = (double)100/itemList.get(0).getMax();
            }
            updateProgress(itemList.get(0).getMax());
        }

 }


 @SuppressLint("StringFormatInvalid")
 private void updateProgress(int maxItemCount){


      int mProgressValue = (int)(mProgressCount * mProgressRate);

     mProgressBarText.setText(getString(R.string.scanning, mProgressValue));

     if(mProgressCount == maxItemCount){
          mProgressValue = 100;
         mProgressBarText.setText("CLEAN JUNK "+Formatter.formatShortFileSize(getActivity(), mCleanerService.getCacheSize()));
         activateScanningButton();
     }

     Log.e(TAG," updateProgress mProgressCount: "+mProgressCount + " mProgressRate: "+mProgressRate+" mProgressValue:"+mProgressValue);


     mProgressBar.setProgress(mProgressValue);
 }


 private void manageCleanProgress(int maxSize){

        if(maxSize> 0){

            if(mProgressRate <=0) {
                mProgressRate = (double)100/maxSize;
            }
            updateCleanProgress(maxSize);
        }

 }


 @SuppressLint("StringFormatInvalid")
 private void updateCleanProgress(int maxItemCount){


      int mProgressValue = (int)(mProgressCount * mProgressRate);

     mCleanProgressBarText.setText(getString(R.string.cleaning, mProgressValue));

     if(mProgressCount >= maxItemCount){
          mProgressValue = 100;
         mCleanProgressBarText.setText("CLEAN JUNK  OF "+Formatter.formatShortFileSize(getActivity(), mCleanerService.getCacheSize()));
         activateScanningButton();
     }

     Log.e(TAG," updateCleanProgress mProgressCount: "+mProgressCount + " mProgressRate: "+mProgressRate+" mProgressValue:"+mProgressValue);


     mCleanProgressBar.setProgress(mProgressValue);
 }


 public void setCacheAmount(){

        String amount = Formatter.formatShortFileSize(getActivity(), mCleanerService.getCacheSize());

        if(amount.length()>2) {
            mJunkMeter.setText(amount.substring(amount.length() - 1));
            mJunkAmount.setText(amount.substring(0, amount.length() - 1));
        }else{
            mJunkMeter.setText("0");
            mJunkAmount.setText("B");
        }

 }



 public void setCleanValues(){

        String amount = Formatter.formatShortFileSize(getActivity(), mCleanerService.getCacheSize());

        if(amount.length()>2) {
            mCleanMeter.setText(amount.substring(amount.length() - 1));
            mCleanAmount.setText(amount.substring(0, amount.length() - 1));
        }else{
            mCleanMeter.setText("0");
            mCleanAmount.setText("B");
        }

 }

 private void activateScanningButton(){
     scanningButton.setEnabled(true);

 }

    private void deActivateScanningButton(){
        scanningButton.setEnabled(false);
    }

    private void showCleanView(){
      mCleanView.setVisibility(View.VISIBLE);
      mScanView.setVisibility(View.GONE);
    }

    private void hideCleanView(){
      mCleanView.setVisibility(View.GONE);
      mScanView.setVisibility(View.VISIBLE);
    }

    private void startAnimation(){

        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.rotate_anim);
        anim.setTarget(cleanCircle);
        anim.setDuration(3000);
        anim.start();
    }
}
