package com.birina.bsecure.realtimeprotection;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.antivirus.AntivirusActivity;
import com.birina.bsecure.antivirus.RippleBackground;
import com.birina.bsecure.antivirus.shimmer.Shimmer;
import com.birina.bsecure.dashboard.DeshBoardActivity;
import com.birina.bsecure.realtimeprotection.progressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.Random;


public class RealTimeProtectionActivity extends BirinaActivity {
    private CircleProgressBar mLineProgressBar;
    AnimatorSet mAnimatorSet;
    Handler mHandler;
    Random mRand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkExpireStatus();
        setContentView(R.layout.activity_realtimeprotection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.real_time_protection);


        mLineProgressBar = (CircleProgressBar) findViewById(R.id.line_progress);
       /* initializeView();
        startScanning();
        handleProgress();*/
        simulateProgress();
    }


    private void initializeView(){

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
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mLineProgressBar, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mLineProgressBar, "ScaleY", 0f, 1.2f, 1f);
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
                                    //scanningComplete();
                                }else {
                                    mLineProgressBar.setProgress((int) finalInitialValue);
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




    @Override
    protected void onResume() {
        super.onResume();
        checkExpireStatus();

    }

    private void simulateProgress() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 101);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                mLineProgressBar.setProgress(progress);

                if(progress >= 101){
                    startSystemAdvisor();
                    animator.cancel();
                }

            }
        });
        animator.setRepeatCount(0);
        animator.setDuration(15000);
        animator.start();
    }



    private void startSystemAdvisor() {
        startActivity(new Intent(RealTimeProtectionActivity.this, AdvisorActivity.class));
        finish();
    }


}
