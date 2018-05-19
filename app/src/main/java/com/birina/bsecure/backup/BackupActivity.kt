package com.birina.bsecure.backup

import android.Manifest
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.birina.bsecure.R
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.birina.bsecure.antivirus.shimmer.Shimmer
import com.birina.bsecure.backup.model.Request
import com.birina.bsecure.util.BirinaPrefrence
import com.birina.bsecure.util.ContactUtility
import kotlinx.android.synthetic.main.activity_backup.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import android.view.ViewTreeObserver
import android.widget.Button
import com.birina.bsecure.Base.BaseActivity
import com.birina.bsecure.Base.BirinaActivity
import com.birina.bsecure.util.Constant
import kotlinx.android.synthetic.main.content_backup.*
import java.text.SimpleDateFormat
import java.util.*


class BackupActivity : BirinaActivity(), BackupContract.View {


    val REQUEST_CODE_ASK_PERMISSIONS = 123
    var mPresenter: BackupPresenter ? = null
    var mProgressBar: ProgressBar? = null

    internal var mShimmer: Shimmer? = null
    internal var mBackupCompleteTxt: TextView? = null
    internal var mBackUpCompleteOkBtn: Button? = null
    internal var mTxtBackUpDate: TextView? = null
    internal var mTxtSms: TextView? = null
    internal var mTxtContact: TextView? = null

    private var mBackupMsgText: com.birina.bsecure.antivirus.shimmer.ShimmerTextView? = null

      var  mContext : Context? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkExpireStatus()
        setContentView(R.layout.activity_backup)
         BackupPresenter(this)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(R.string.backup)

        mContext = this
        mBackupMsgText = backupMsg
        mBackupCompleteTxt = textBackupComplete
        mBackUpCompleteOkBtn = btnBackUpCompleteOk
        mTxtBackUpDate = txtBackUpDate
        mTxtSms = txtSms
        mTxtContact = txtContact

        mProgressBar =backupProgress

        if(null != BirinaPrefrence.getBackUpDate(this))
        mTxtBackUpDate?.setText(BirinaPrefrence.getBackUpDate(this))

        mBackUpCompleteOkBtn?.setOnClickListener( View.OnClickListener { view -> finish() })

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
                                    ActivityCompat.checkSelfPermission(mContext as BackupActivity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(mContext as BackupActivity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(mContext as BackupActivity, arrayOf(Manifest.permission.READ_CONTACTS,
                                        Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE), REQUEST_CODE_ASK_PERMISSIONS)
                            } else {
                                showDialog();
                                startRestore()
                                backupContactAndSms()

                            }


                    }
                })

    }


    override fun onResume() {
        super.onResume()
        checkExpireStatus()
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

                    mTxtContact?.setText(""+obj.contact.size)
                    mTxtSms?.setText(""+obj.sms.size)

                    mPresenter?.createBackup(obj);

                })

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
        saveBackUpDate()
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
            mBackUpCompleteOkBtn?.setVisibility(View.VISIBLE)
    }


    override fun onDestroy() {
        super.onDestroy()
        completeRestore();
    }

    fun saveBackUpDate(){

        val sdf = SimpleDateFormat("dd/MM/yyyy \nhh:mm a")
        val format = sdf.format(Calendar.getInstance().getTime())
        BirinaPrefrence.saveBackUpDate(this, format)
    }
}
