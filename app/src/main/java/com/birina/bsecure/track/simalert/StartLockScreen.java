package com.birina.bsecure.track.simalert;

import android.app.Activity;
import android.os.Bundle;

public class StartLockScreen extends Activity {
/*	 KeyguardManager.KeyguardLock k1;

	 public void onAttachedToWindow() {
			// TODO Auto-generated method stub
			 this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
			super.onAttachedToWindow();
		}*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		finish();


		/*getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	        KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
	        k1 = km.newKeyguardLock("IN");
	        k1.disableKeyguard();*/


	}

}
