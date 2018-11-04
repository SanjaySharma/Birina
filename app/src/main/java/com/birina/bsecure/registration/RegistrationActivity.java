package com.birina.bsecure.registration;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.birina.bsecure.Base.BaseActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.dashboard.DeshBoardActivity;
import com.birina.bsecure.login.LoginActivity;
import com.birina.bsecure.login.TempLoginActivity;
import com.birina.bsecure.network.RestClient;
import com.birina.bsecure.registration.model.RegistrationRequestModel;
import com.birina.bsecure.registration.model.RegistrationResponseModel;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.ConnectionManager;
import com.birina.bsecure.util.Constant;
import com.birina.bsecure.util.Validation;

import java.util.Calendar;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RegistrationActivity extends BaseActivity {


    private EditText mEdtName,mEdtSiNo, mEdtPhone, mEdtEmail,mEdtCreatePwd,mEdtConfPwd, mEdtDob;

     private android.widget.Spinner mYearSpinner;

     private String mSelectedGender;

    final int REQUEST_CODE_ASK_PERMISSIONS = 10009;

    private String[] genderList = {"Select Gender","Male","Female"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        navigateFlow();

        ActionBar supportActionBar = getSupportActionBar();


        if (supportActionBar != null)
            supportActionBar.hide();

        //  AddUser();
        initializeClick();

    }

    // Method to initialize id & ClickListener
    private void initializeClick() {


        mEdtName = (EditText) findViewById(R.id.userName);
        mEdtSiNo = (EditText) findViewById(R.id.siNo);
        mEdtPhone = (EditText) findViewById(R.id.textPhone);
        mEdtEmail = (EditText) findViewById(R.id.textEmail);
        mEdtCreatePwd = (EditText) findViewById(R.id.password);
        mEdtConfPwd = (EditText) findViewById(R.id.confPassword);
        mEdtDob = (EditText) findViewById(R.id.textdob);
        mYearSpinner = (android.widget.Spinner) findViewById(R.id.select_gender);

        setDataOnYearSpinner();


        findViewById(R.id.btn_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();

                if(ConnectionManager.isInternetAvailable(RegistrationActivity.this)){
                    validateNo();

                }else {
                    dismissProgressDialog();
                    Snackbar.make(findViewById(R.id.loginperant), getResources().getString(R.string.connection_manager_fail),
                            Snackbar.LENGTH_LONG).show();

                }
            }
        });

        findViewById(R.id.login).setOnClickListener((View v) ->{

            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                StringBuilder date = new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear+1)
                        .append("/").append(year);

                if(null != date){
                    mEdtDob.setText(date.toString());
                }
            }

        };

        findViewById(R.id.btndob).setOnClickListener(view->{

            new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }


    private void validateNo() {

        if (!Validation.isFieldEmpty(mEdtPhone)) {

            if (Validation.isMobileValid(mEdtPhone.getText().toString())) {
                if (Validation.isFieldEmpty(mEdtSiNo)) {

                    dismissProgressDialog();

                    Snackbar.make(findViewById(R.id.registrationParent), getResources().getString(R.string.fill_si_number),
                            Snackbar.LENGTH_LONG).show();

                } else {

                    if (Validation.isFieldEmpty(mEdtName)) {

                        dismissProgressDialog();

                        Snackbar.make(findViewById(R.id.registrationParent), getResources().getString(R.string.fill_user_name),
                                Snackbar.LENGTH_LONG).show();

                    } else {

                        if (null == mSelectedGender || mSelectedGender.isEmpty()) {

                            dismissProgressDialog();

                            Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                            .getString(R.string.fill_gender),
                                    Snackbar.LENGTH_LONG).show();

                        } else {

                            if (Validation.isFieldEmpty(mEdtDob)) {

                                dismissProgressDialog();

                                Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                                .getString(R.string.fill_dob),
                                        Snackbar.LENGTH_LONG).show();

                            } else {
                                if (Validation.isFieldEmpty(mEdtCreatePwd)) {

                                    dismissProgressDialog();

                                    Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                                    .getString(R.string.fill_pwd),
                                            Snackbar.LENGTH_LONG).show();

                                } else {

                                    if (Validation.isFieldEmpty(mEdtConfPwd)) {

                                        dismissProgressDialog();

                                        Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                                        .getString(R.string.fill_conf_pwd),
                                                Snackbar.LENGTH_LONG).show();

                                    } else {
                                        if (!mEdtConfPwd.getText().toString().equals(mEdtCreatePwd.getText().toString())) {

                                            dismissProgressDialog();

                                            Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                                            .getString(R.string.password_not_match),
                                                    Snackbar.LENGTH_LONG).show();

                                        } else{

                                            if(mEdtConfPwd.getText().toString().length()<6){
                                                dismissProgressDialog();

                                                Snackbar.make(findViewById(R.id.registrationParent), getResources()
                                                                .getString(R.string.password_lenght_match),
                                                        Snackbar.LENGTH_LONG).show();
                                            }else {
                                                callApi();
                                            }
                                        }

                                    }

                                }

                            }

                        }
                    }
                }

            } else {

                dismissProgressDialog();

                Snackbar.make(findViewById(R.id.registrationParent), getResources().getString(R.string.reg_invalid_mobile),
                        Snackbar.LENGTH_LONG).show();
            }


        } else {

            dismissProgressDialog();

            Snackbar.make(findViewById(R.id.registrationParent), getResources().getString(R.string.fill_mobile_number),
                    Snackbar.LENGTH_LONG).show();


        }

    }

/*
* This method check is user loggedIn or not.
* Already loggedIn navigate to Dashboard.
* Not logged in show login UI.
* */

    public void navigateFlow() {

        if (BirinaPrefrence.isLoggedIn(RegistrationActivity.this)
                && BirinaPrefrence.isTempLoggedIn(RegistrationActivity.this) ) {
            startDashBoardActivity();
        }if (BirinaPrefrence.isLoggedIn(RegistrationActivity.this)
                && !BirinaPrefrence.isTempLoggedIn(RegistrationActivity.this) ) {
            startTempLoginActivity();
        }
    }


    private void startLoginActivity() {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        finish();
    }

    private void startTempLoginActivity() {
        startActivity(new Intent(RegistrationActivity.this, TempLoginActivity.class));
        finish();
    }

    private void startDashBoardActivity() {
        Intent myIntent = new Intent(RegistrationActivity.this, DeshBoardActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(myIntent);

        finish();
    }



    private void callApi(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS
                    ,Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS,Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
        } else {

            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String imei =  telephonyManager.getDeviceId();
            RegistrationApi(mEdtSiNo.getText().toString(), mEdtPhone.getText().toString(),
                    (mEdtEmail.getText() != null ? mEdtEmail.getText().toString() : " "), imei
                    , mEdtName.getText().toString(), mEdtCreatePwd.getText().toString(),
                    mEdtDob.getText().toString(), mSelectedGender);

        }

    }

    private void RegistrationApi(String siNo, String phone, String email,  String imei,  String name
            , String password, String dob, String gender) {

        RestClient restClient = new RestClient();

        Observable<Response<RegistrationResponseModel>> registrationResponsePayload
                = restClient.getApiService().performRegistration(
                        new RegistrationRequestModel( email,siNo, phone, name, imei, password, dob, gender));


        registrationResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(elabelResponse ->
                        {
                            dismissProgressDialog();
                            if (null != elabelResponse && elabelResponse.isSuccessful() &&
                                    null != elabelResponse.body() && null != elabelResponse.body().getResponse()
                                    && elabelResponse.body().getResponse() == Constant.LOGGED_SUCCESS) {

                                BirinaPrefrence.saveUserName(RegistrationActivity.this, mEdtName.getText().toString());
                                BirinaPrefrence.saveRegisteredNumber(RegistrationActivity.this, mEdtPhone.getText().toString());
                                BirinaPrefrence.saveExpireDate(RegistrationActivity.this, elabelResponse.body().getEnddate());

                                startLoginActivity();

                            } else {

                                // startDashBoardActivity();

                                throw new RuntimeException("Registration Fail");
                            }
                        },
                        t -> {
                            dismissProgressDialog();
                            // startDashBoardActivity();

                            Toast.makeText(RegistrationActivity.this, "Registration Fail", Toast.LENGTH_LONG).show();
                        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    callApi();
                }
                break;

            default:
                break;
        }
    }



     private void setDataOnYearSpinner() {
        mYearSpinner.setAdapter(new android.widget.BaseAdapter() {
            @Override
            public int getCount() {
                return genderList == null ? 0 : genderList.length;
            }

            @Override
            public Object getItem(int position) {
                return genderList[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                            .item_spinner, parent, false);
                }
                TextView selectView = (TextView) convertView;
                selectView.setText(genderList[position]);
                return selectView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                            .dropdown_item_spinner, parent, false);
                }
                TextView dropDownView = (TextView) convertView;
                if (position == 0) {
                    dropDownView.setHeight(0);
                } else {
                    dropDownView.setHeight((int)getResources().getDimension(R.dimen._30sdp));
                }
                dropDownView.setText(genderList[position]);
                return dropDownView;
            }
        });

        //set the itemselected listener on medium adapter
        mYearSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                mSelectedGender = genderList[position];
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });
    }

}
