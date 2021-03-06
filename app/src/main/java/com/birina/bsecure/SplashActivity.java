package com.birina.bsecure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.login.LoginActivity;
import com.birina.bsecure.registration.RegistrationActivity;


public class SplashActivity extends BaseActivity {

	private final int SPLASH_DISPLAY_LENGTH = 1500;


	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();


		if (supportActionBar != null)
			supportActionBar.hide();

		// New handler to start the splash activity
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

					startActivity(new Intent(SplashActivity.this,
							RegistrationActivity.class));
					finish();
			}

		}, SPLASH_DISPLAY_LENGTH);
	}



}
