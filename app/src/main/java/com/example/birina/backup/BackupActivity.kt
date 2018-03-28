package com.example.birina.backup

import android.Manifest
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.birina.R
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.birina.backup.model.Request
import com.example.birina.util.BirinaPrefrence
import com.example.birina.util.ContactUtility
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_backup.*
import rx.Observable
import rx.Observer
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class BackupActivity : AppCompatActivity(), BackupContract.View {


    val REQUEST_CODE_ASK_PERMISSIONS = 123
    var mPresenter: BackupPresenter ? = null
    var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)
        val result = BackupPresenter(this)
        mProgressBar =progressBar1



    }


    override fun onStart() {
        super.onStart()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_SMS), REQUEST_CODE_ASK_PERMISSIONS)
        } else {
            showDialog();

            backupContactAndSms()

        }
    }



    private fun backupContactAndSms() {


        Observable.fromCallable { getContacts()}
                .subscribeOn(AndroidSchedulers.mainThread())
                .map({ obj ->

                    var request = Request()

                    request.mobile = BirinaPrefrence.getRegisteredNumber(this)
                    request.contact = obj
                    request.sms = getSmsList()

                   return@map request
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                { obj ->
                    mPresenter?.createBackup(obj);

                })

    /*    var contactList = getContacts();
        var smsList =getSmsList();

        var request = Request()

        request.mobile="9599385901"
        request.contact=contactList
        request.sms=smsList


        mPresenter?.createBackup(request);*/
    }

    private fun getSmsList(): List<Request.SmsBean> {
        return SMSFetcher().getAllSms(this)
    }


    private fun getContacts(): List<Request.ContactBean>? {
        return ContactUtility(this).fetchAll()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults.size > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                showDialog();
                backupContactAndSms()
            }

            else -> {
            }
        }
    }

    override fun showDialog() {
        mProgressBar!!.visibility= View.VISIBLE
    }

    override fun hideDialog() {
        mProgressBar!!.visibility= View.GONE
    }

    override fun setPresenter(presenter: BackupContract.Presenter?) {
        mPresenter= presenter as BackupPresenter?
    }

    override fun onSuccess() {
        Toast.makeText(this, getString(R.string.backup_created), Toast.LENGTH_LONG).show()

        finish()
    }

    override fun onFailure() {
        Toast.makeText(this, getString(R.string.backup_failed), Toast.LENGTH_LONG).show()
        finish()
    }



}
