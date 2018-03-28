package com.example.birina;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.example.birina.login.LoginActivity;


public class SplashActivity extends AppCompatActivity {

	private final int SPLASH_DISPLAY_LENGTH = 1000;


	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// New handler to start the splash activity
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
					finish();
			}

		}, SPLASH_DISPLAY_LENGTH);
	}



}
