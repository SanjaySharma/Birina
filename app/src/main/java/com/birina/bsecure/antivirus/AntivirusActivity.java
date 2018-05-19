package com.birina.bsecure.antivirus;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.antivirus.shimmer.Shimmer;

import java.util.ArrayList;
import java.util.Random;


public class AntivirusActivity extends BirinaActivity {

    private TextView  mProgressText, mScanningCompleteText,mTextDeviceSequre;
    private com.birina.bsecure.antivirus.shimmer.ShimmerTextView mScanningText;
    AnimatorSet mAnimatorSet;
    RippleBackground mRippleBackground;

    Shimmer mShimmer;


    Handler mHandler;
     Random mRand;
     private RelativeLayout mThreadDetactParent,mNoThreadParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antivirus);
        checkExpireStatus();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.antivirus);

        initializeView();
        setListeners();

        startScanning();
        handleProgress();
        toggleAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkExpireStatus();
    }


    private void initializeView(){
        mRippleBackground =(RippleBackground)findViewById(R.id.content);
        mScanningCompleteText = (TextView) findViewById(R.id.textScanningComplete);
        mScanningCompleteText.setVisibility(View.GONE);
        mTextDeviceSequre = (TextView) findViewById(R.id.textDeviceSequre);
        mTextDeviceSequre.setVisibility(View.GONE);
        mThreadDetactParent = (RelativeLayout) findViewById(R.id.threadDetactParent);
        mNoThreadParent = (RelativeLayout) findViewById(R.id.noThreadParent);
        mNoThreadParent.setVisibility(View.GONE);

        mScanningText =(com.birina.bsecure.antivirus.shimmer.ShimmerTextView) findViewById(R.id.foundDevice);
        mProgressText =(TextView)findViewById(R.id.centerImage);


        mHandler=new Handler();
        mRand = new Random();

    }

    private void setListeners(){
        findViewById(R.id.btnOk).setOnClickListener(v ->finish());
        findViewById(R.id.btnCancel).setOnClickListener(v ->finish());

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



    private void startScanning(){
        mRippleBackground.startRippleAnimation();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                foundDevice();
            }
        },3000);
    }

    private void foundDevice(){
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(400);
        mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mScanningText, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mScanningText, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        mAnimatorSet.playTogether(animatorList);
        mAnimatorSet.start();
    }


    private void handleProgress(){

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    float initialValue = 1;
                    int progrssIteration = getProgrssIteration();
                    float delta = getDelta(progrssIteration);
                    int i = 0;

                    while ( i <= progrssIteration+1) {

                        initialValue = initialValue+ delta;

                        if (i == progrssIteration){
                            initialValue = 100;
                        }

                        Thread.sleep(getSleepDuration());
                        float finalInitialValue = initialValue;
                        i++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(finalInitialValue > 100){
                                    scanningComplete();
                                }else {
                                    mProgressText.setText(""+(int) finalInitialValue+"%");
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

    }


    private int getProgrssIteration(){
        int randomNum = mRand.nextInt((100 - 50) + 1) + 50;
        return randomNum;
    }

    private float getDelta(int progrssIteration){
           return  100/progrssIteration;
    }

    private long getSleepDuration(){
        int randomNum = mRand.nextInt((1000 - 100) + 1) + 100;
        return randomNum;
    }





    public void toggleAnimation() {
        if (mShimmer != null && mShimmer.isAnimating()) {
            mShimmer.cancel();
        } else {
            mShimmer = new Shimmer();
            mShimmer.start(mScanningText);
        }
    }

    private void scanningComplete(){
        if (mShimmer != null && mShimmer.isAnimating()) {
            mShimmer.cancel();
            mScanningText.setVisibility(View.GONE);
        }
        mRippleBackground.stopRippleAnimation();
        mProgressText.setVisibility(View.GONE);
        mScanningCompleteText.setVisibility(View.VISIBLE);
        mTextDeviceSequre.setVisibility(View.VISIBLE);
        mThreadDetactParent.setVisibility(View.GONE);
        mNoThreadParent.setVisibility(View.VISIBLE);
    }
}
