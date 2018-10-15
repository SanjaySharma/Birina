package com.birina.bsecure.realtimeprotection;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
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

        simulateProgress();
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
        animator.setDuration(70000);
        animator.start();
    }



    private void startSystemAdvisor() {
        startActivity(new Intent(RealTimeProtectionActivity.this, AdvisorActivity.class));
        finish();
    }


}
