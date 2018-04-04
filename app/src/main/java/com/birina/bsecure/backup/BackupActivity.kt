package com.example.bsecure.backup

import android.Manifest
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.bsecure.R
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.bsecure.antivirus.shimmer.Shimmer
import com.example.bsecure.backup.model.Request
import com.example.bsecure.util.BirinaPrefrence
import com.example.bsecure.util.ContactUtility
import kotlinx.android.synthetic.main.activity_backup.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import android.view.ViewTreeObserver
import com.example.bsecure.util.Constant


class BackupActivity : AppCompatActivity(), BackupContract.View {


    val REQUEST_CODE_ASK_PERMISSIONS = 123
    var mPresenter: BackupPresenter ? = null
    var mProgressBar: ProgressBar? = null

    internal var mShimmer: Shimmer? = null
    internal var mBackupCompleteTxt: TextView? = null
    private var mBackupMsgText: com.example.bsecure.antivirus.shimmer.ShimmerTextView? = null

      var  mContext : Context? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)
        val result = BackupPresenter(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(R.string.backup)
        mContext = this
        mBackupMsgText = backupMsg
        mBackupCompleteTxt = textBackupComplete

        mProgressBar =backupProgress

        mBackupCompleteTxt?.getViewTreeObserver()?.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {

                    override fun onGlobalLayout() {

                        Log.d(Constant.TAG_RESTORE, "Enter in onGlobalLayout of " )

                        // only want to do this once
                        mBackupCompleteTxt?.getViewTreeObserver()?.removeGlobalOnLayoutListener(this)

                        // set the menu title, the empty string check prevents sub-classes
                        // from blanking out the title - which they shouldn't but belt and braces!
                            if (ActivityCompat.checkSelfPermission(mContext as BackupActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(mContext as BackupActivity, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(mContext as BackupActivity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(mContext as BackupActivity, arrayOf(Manifest.permission.READ_CONTACTS,
                                        Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS), REQUEST_CODE_ASK_PERMISSIONS)
                            } else {
                                showDialog();
                                startRestore()
                                backupContactAndSms()

                            }


                    }
                })

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
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
        return ContactUtility(mContext).fetchAll()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults.size > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                showDialog();
                startRestore()
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
        Toast.makeText(this, getString(R.string.backup_complete), Toast.LENGTH_LONG).show()

         completeRestore()
    }

    override fun onFailure() {
        Toast.makeText(this, getString(R.string.backup_failed), Toast.LENGTH_LONG).show()
        finish()
    }



    fun startRestore() {
        if (mShimmer != null && mShimmer?.isAnimating()!!) {
            mShimmer?.cancel()
        } else {
            mShimmer = Shimmer()
            mShimmer?.start(mBackupMsgText)
        }
    }

    fun completeRestore() {
        if (mShimmer != null && mShimmer?.isAnimating()!!) {
            mShimmer?.cancel()
            mBackupMsgText?.setVisibility(View.GONE)
        }
            mBackupCompleteTxt?.setVisibility(View.VISIBLE)
    }


    override fun onDestroy() {
        super.onDestroy()
        completeRestore();
    }
}
