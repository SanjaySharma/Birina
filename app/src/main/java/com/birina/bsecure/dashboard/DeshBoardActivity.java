package com.birina.bsecure.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.birina.bsecure.Base.BirinaActivity;
import com.birina.bsecure.Base.LocationActivity;
import com.birina.bsecure.R;
import com.birina.bsecure.antivirus.AntivirusActivity;
import com.birina.bsecure.backup.BackupActivity;
import com.birina.bsecure.junkcleaner.activity.PhoneBoosterActivity;
import com.birina.bsecure.login.LoginPresenterImp;
import com.birina.bsecure.login.LoginView;
import com.birina.bsecure.pockettheft.PocketTheftActivity;
import com.birina.bsecure.realtimeprotection.RealTimeProtectionActivity;
import com.birina.bsecure.remotescreaming.RemoteScreamingActivity;
import com.birina.bsecure.restore.RestoreActivity;
import com.birina.bsecure.simAlert.SimAlertActivity;
import com.birina.bsecure.track.TrackActivity;
import com.birina.bsecure.trackingrecovery.TrackingRecoveryActivity;
import com.birina.bsecure.unplugcharger.UnPlugChargerActivity;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.BirinaUtility;
import com.birina.bsecure.util.Constant;


public class DeshBoardActivity extends BirinaActivity implements LoginView, View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        checkExpireStatus();

        new LoginPresenterImp(this);

        //   mLoginPresenter.getData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.navUserName)).setText(BirinaPrefrence.getUserName(this));
        ((TextView) header.findViewById(R.id.navSerial)).setText(BirinaPrefrence.getSiNo(this));

        if(null != BirinaPrefrence.getExpireDate(this)
                && !BirinaPrefrence.getExpireDate(this).isEmpty() ) {

            String remainingTime = BirinaUtility.getDateDifference(BirinaPrefrence.getExpireDate(this) );
            ((TextView) header.findViewById(R.id.subcriptionValue)).setText(remainingTime);
        }
        setUpClickEvents();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkExpireStatus();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            case R.id.action_contact:
                return true;
            case R.id.action_sms:
                return true;
            case R.id.action_other:
                BirinaUtility.getBackUpData(this);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String error) {

    }

    @Override
    public void setData(String t) {

    }


    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void setPresenter(Object presenter) {

    }


    private void setUpClickEvents() {

        findViewById(R.id.textPhoneBoost).setOnClickListener(this);
        findViewById(R.id.textAntivirus).setOnClickListener(this);
        findViewById(R.id.textPocketTheft).setOnClickListener(this);

        findViewById(R.id.textUnPlugCharger).setOnClickListener(this);
        findViewById(R.id.textSimAlert).setOnClickListener(this);
        findViewById(R.id.textTrackingRecovery).setOnClickListener(this);

        findViewById(R.id.textRemoteScreaming).setOnClickListener(this);
        findViewById(R.id.textBackup).setOnClickListener(this);
        findViewById(R.id.textRestoreData).setOnClickListener(this);

        findViewById(R.id.textDataWipe).setOnClickListener(this);
        findViewById(R.id.textAntitheft).setOnClickListener(this);
        findViewById(R.id.textRealtimeProtection).setOnClickListener(this);



    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.textPhoneBoost:
                startCleanerActivity();
                break;
            case R.id.textTrackingRecovery:
                startTrackingRecoveryActivity();
                break;

            case R.id.textAntivirus:
                folderSelectionDialog();
                break;

            case R.id.textDataWipe:
                startTrackActivity(getResources().getString(R.string.data_wipe),getResources().getString(R.string.remote_wipe_description));
                break;

            case R.id.textAntitheft:
                startTrackActivity(getResources().getString(R.string.antitheft),getResources().getString(R.string.remote_lock_description));
                break;

            case R.id.textBackup:
                backupDialog();
                break;

            case R.id.textRestoreData:
                restoreDialog();
                break;
            case R.id.textRealtimeProtection:
                startRealTimeProtectionActivity();
                break;

             case R.id.textPocketTheft:
                startPocketTheftActivity();
                break;

            case R.id.textUnPlugCharger:
                startUnPlugChargerActivity();
                break;
            case R.id.textRemoteScreaming:
                startRemoteScreamingActivity();
                break;
            case R.id.textSimAlert:
              startSimAlertActivity();
                break;



        }
    }


    private void startCleanerActivity() {
        startActivity(new Intent(DeshBoardActivity.this, PhoneBoosterActivity.class));
    }

    private void startSimAlertActivity() {
        startActivity(new Intent(DeshBoardActivity.this, SimAlertActivity.class));
    }


    private void startAntivirusActivity() {
        startActivity(new Intent(DeshBoardActivity.this, AntivirusActivity.class));
    }

    private void startTrackActivity(String title, String desc) {


        //ContactUtility contactUtility = new ContactUtility(this);
        //contactUtility.deleteContact();
        // contactUtility.deleteSms();

        Intent intent = new Intent(DeshBoardActivity.this, TrackActivity.class);
        intent.putExtra(Constant.TRACK_INTENT_KEY, title);
        intent.putExtra(Constant.TRACK_INTENT_DEC_KEY, desc);
        startActivity(intent);
    }

    private void startBackUpActivity() {
        startActivity(new Intent(DeshBoardActivity.this, BackupActivity.class));
    }

    private void startRestoreActivity() {
        startActivity(new Intent(DeshBoardActivity.this, RestoreActivity.class));
    }

    private void startRealTimeProtectionActivity() {
        startActivity(new Intent(DeshBoardActivity.this, RealTimeProtectionActivity.class));
    }

    private void startPocketTheftActivity() {
        startActivity(new Intent(DeshBoardActivity.this, PocketTheftActivity.class));
    }

    private void startUnPlugChargerActivity() {
        startActivity(new Intent(DeshBoardActivity.this, UnPlugChargerActivity.class));
    }

    private void startRemoteScreamingActivity() {
        startActivity(new Intent(DeshBoardActivity.this, RemoteScreamingActivity.class));
    }


    private void startTrackingRecoveryActivity() {
        startActivity(new Intent(DeshBoardActivity.this, TrackingRecoveryActivity.class));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void displaySelectedScreen(int itemId) {

        Intent webIntent = new Intent(DeshBoardActivity.this, WebActivity.class);

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu_help:
                webIntent.putExtra(Constant.WEB_INTENT_KEY, Constant.HELP);
                break;
            case R.id.nav_menu_feedback:
                webIntent.putExtra(Constant.WEB_INTENT_KEY, Constant.FEEDBACK);
                break;
            case R.id.nav_menu_about:
                webIntent.putExtra(Constant.WEB_INTENT_KEY, Constant.ABOUT);
                break;
        }

        startActivity(webIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }



    private void folderSelectionDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.antivirus_alert_dialog, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        TextView okBtn;
        AlertDialog dialog = alert.create();

        okBtn = (TextView) alertLayout.findViewById(R.id.btnOk);

        ((CheckBox)alertLayout.findViewById(R.id.checkAgree))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if(isChecked){
                                                        okBtn.setEnabled(true);
                                                        okBtn.setTextColor(getResources().getColor(R.color.apps_list_cache_memory));
                                                        //okBtn.setBackgroundColor();
                                                    }else{
                                                        okBtn.setEnabled(false);
                                                        okBtn.setTextColor(getResources().getColor(R.color.black_sub_header));
                                                    }
                                                }
                                            }
                );

        alertLayout.findViewById(R.id.btnOk).setOnClickListener((v) -> {
            startAntivirusActivity();
            dialog.cancel();
        });

        alertLayout.findViewById(R.id.btnCancel).setOnClickListener((v) -> {
            dialog.cancel();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen._210sdp));
    }




    private void backupDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.backup_alert_dialog, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        TextView okBtn;
        AlertDialog dialog = alert.create();

        okBtn = (TextView) alertLayout.findViewById(R.id.btnOk);

        ((CheckBox)alertLayout.findViewById(R.id.checkAgree))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     if(isChecked){
                         okBtn.setEnabled(true);
                         okBtn.setTextColor(getResources().getColor(R.color.apps_list_cache_memory));
                         //okBtn.setBackgroundColor();
                     }else{
                         okBtn.setEnabled(false);
                         okBtn.setTextColor(getResources().getColor(R.color.black_sub_header));
                     }
               }
             }
        );

        alertLayout.findViewById(R.id.btnOk).setOnClickListener((v) -> {
            startBackUpActivity();
            dialog.cancel();
        });

        alertLayout.findViewById(R.id.btnCancel).setOnClickListener((v) -> {
            dialog.cancel();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen._210sdp));
    }



    private void restoreDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.restore_alert_dialog, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        TextView okBtn;
        AlertDialog dialog = alert.create();

        okBtn = (TextView) alertLayout.findViewById(R.id.btnOk);

        ((CheckBox)alertLayout.findViewById(R.id.checkAgree))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     if(isChecked){
                         okBtn.setEnabled(true);
                         okBtn.setTextColor(getResources().getColor(R.color.apps_list_cache_memory));
                         //okBtn.setBackgroundColor();
                     }else{
                         okBtn.setEnabled(false);
                         okBtn.setTextColor(getResources().getColor(R.color.black_sub_header));
                     }
               }
             }
        );

        alertLayout.findViewById(R.id.btnOk).setOnClickListener((v) -> {
            startRestoreActivity();
            dialog.cancel();
        });

        alertLayout.findViewById(R.id.btnCancel).setOnClickListener((v) -> {
            dialog.cancel();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen._210sdp));
    }


}
