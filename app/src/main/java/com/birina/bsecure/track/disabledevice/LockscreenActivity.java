package com.birina.bsecure.track.disabledevice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.track.disabledevice.service.LockscreenViewService;
import com.birina.bsecure.track.disabledevice.service.MyAccessibilityService;
import com.birina.bsecure.util.Constant;


public class LockscreenActivity extends BaseActivity {



    private final String TAG = "LockscreenActivity";
    private static Context sLockscreenActivityContext = null;

    private RelativeLayout mLockscreenMainLayout = null;

    public static SendMassgeHandler mMainHandler = null;

    private static final int ACCESSIBILITY_ENABLED = 1;


    public PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                default:
                    break;
            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        sLockscreenActivityContext = this;
        mMainHandler = new SendMassgeHandler();
//        getWindow().setType(2004);
//        getWindow().addFlags(524288);
//        getWindow().addFlags(4194304);
        ///

        Log.d(Constant.TAG, "Enter in onCreate of "+TAG);

        getWindow().setType(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        initLockScreenUi();

        setLockGuard();

        isAccessibilitySettingsOn(this);

        Log.d(Constant.TAG, "Exit from onCreate of "+TAG);
    }

    private class SendMassgeHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(Constant.TAG, "Enter in SendMassgeHandler of "+TAG);

            finishLockscreenAct();
        }
    }

    private void finishLockscreenAct() {
        finish();
    }


    private void initLockScreenUi() {
        setContentView(R.layout.activity_lockscreen);
        mLockscreenMainLayout = (RelativeLayout) findViewById(R.id.lockscreen_main_layout);
        mLockscreenMainLayout.getBackground().setAlpha(15);

    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void setLockGuard() {

        Log.d(Constant.TAG, "Enter in setLockGuard of "+TAG);

        boolean isLockEnable = false;
        if (!LockscreenUtil.getInstance(sLockscreenActivityContext).isStandardKeyguardState()) {
            isLockEnable = false;
        } else {
            isLockEnable = true;
        }

        Intent startLockscreenIntent = new Intent(this, LockscreenViewService.class);
        startService(startLockscreenIntent);

        boolean isSoftkeyEnable = LockscreenUtil.getInstance(sLockscreenActivityContext).isSoftKeyAvail(this);
        SharedPreferencesUtil.setBoolean(Lockscreen.ISSOFTKEY, isSoftkeyEnable);
        if (!isSoftkeyEnable) {
            mMainHandler.sendEmptyMessage(0);
        } else if (isSoftkeyEnable) {
            if (isLockEnable) {
                mMainHandler.sendEmptyMessage(0);
            }
        }

        finish();
        Log.d(Constant.TAG, "Exit from setLockGuard of "+TAG);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }


    public static boolean isAccessibilitySettingsOn(Context context) {

        Log.d(Constant.TAG, "Enter in isAccessibilitySettingsOn of LockscreenActivity");

        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + MyAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("AU", "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == ACCESSIBILITY_ENABLED) {
            String settingValue = Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
