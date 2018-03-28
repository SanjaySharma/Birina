package com.example.birina.track.simalert;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.birina.R;

import java.io.FileOutputStream;

public class LockScreenAppActivity extends Activity {
	TextView mTextView;
	ToggleButton mToggleButton;
	boolean isLock = false;

	String FILENAME = "old_file.txt";
	int simstatus;
	String msisdn;
	final int REQUEST_CODE_ASK_PERMISSIONS = 123;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mTextView = (TextView) findViewById(R.id.tvInfo);
		mToggleButton = (ToggleButton) findViewById(R.id.btnLock);

		mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isLock = isChecked;

				startService(new Intent(LockScreenAppActivity.this, SaveMyAppsService.class));
			}
		});




		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		simstatus = tManager.getSimState();
		if (simstatus != TelephonyManager.SIM_STATE_ABSENT) {
			System.out.println("--------SIM Present:" + simstatus);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
			}else{
				msisdn = tManager.getSimSerialNumber();
				FileOutputStream fos;
				try {
					fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
					fos.write(msisdn.getBytes());
					System.out.println("---------Data written to files is:"
							+ msisdn);
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();

				}
			}

		}

	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_ASK_PERMISSIONS:
				if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
					//TODO
					getSimNumer();
				}
				break;


			default:
				break;
		}
	}



	private void getSimNumer(){



		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		simstatus = tManager.getSimState();
		if (simstatus != TelephonyManager.SIM_STATE_ABSENT) {
			System.out.println("--------SIM Present:" + simstatus);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_ASK_PERMISSIONS);
			}else{
				msisdn = tManager.getLine1Number();
				FileOutputStream fos;
				try {
					fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
					fos.write(msisdn.getBytes());
					System.out.println("---------Data written to files is:"
							+ msisdn);
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();

				}
			}

		}
	}
}