package com.birina.bsecure.realtimeprotection;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.login.LoginActivity;


public class AdvisorActivity extends BirinaActivity {

	private final int SPLASH_DISPLAY_LENGTH = 1500;


	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkExpireStatus();
		setContentView(R.layout.activity_advisor);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.system_advisor);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkExpireStatus();
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



}
