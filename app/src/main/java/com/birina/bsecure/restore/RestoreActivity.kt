package com.birina.bsecure.restore

import android.Manifest
import android.app.Activity
import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.birina.bsecure.R
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.birina.bsecure.backup.model.Request
import com.birina.bsecure.restore.model.RestoreRequest
import com.birina.bsecure.util.BirinaPrefrence
import com.birina.bsecure.util.ContactUtility
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import com.birina.bsecure.antivirus.shimmer.Shimmer
import com.birina.bsecure.util.BirinaUtility
import com.birina.bsecure.util.Constant
import kotlinx.android.synthetic.main.activity_restore.*
import java.util.ArrayList


class RestoreActivity : AppCompatActivity(), RestoreContract.View {

    val TAG = "RestoreActivity"

    val AUTHORITY = "sms"

    val REQUEST_CODE_ASK_PERMISSIONS = 123
    var mPresenter: RestorePresenter ? = null
    var mProgressBar: ProgressBar? = null

     var defaultSmsApp: String ?= null
     val SMS_REQUEST_CODE = 1
     val SMS_COMPLETE_REQUEST_CODE = 2

     var  totalCompleteOperation = 0;
     var totalQueue = 0;
     var smsQueue = 0;

    var smsBeanList:List<Request.SmsBean> ?= null

    internal var mShimmer: Shimmer? = null
    internal var mRestoreCompleteTxt: TextView? = null
    private var mRestoreMsgText: com.birina.bsecure.antivirus.shimmer.ShimmerTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restore)
        val result = RestorePresenter(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(R.string.restore)
        mRestoreMsgText = restore_msg
        mRestoreCompleteTxt = textRestoreComplete

        mProgressBar =progressBar1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this)
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_SMS), REQUEST_CODE_ASK_PERMISSIONS)
        } else {
            restoreContactAndSms()
        }
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


    private fun restoreContactAndSms() {

        //"8109243585"

        var request = RestoreRequest()

        request.mobile=BirinaPrefrence.getRegisteredNumber(this)

       // request.mobile = "9599385905";

        startRestore()
        mPresenter?.restoreBackup(request);
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults.size > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                restoreContactAndSms()
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

    override fun setPresenter(presenter: RestoreContract.Presenter?) {
        mPresenter= presenter as RestorePresenter?
    }

    override fun onSuccess( response : Request) {


        Log.d(Constant.TAG_RESTORE, "Enter in onSuccess of " + TAG)
        if(response.contact != null && response.contact.size > 0) {

            Log.d(Constant.TAG_RESTORE, "Enter in onSuccess of " + TAG +"Total Contact: "
                    +response.contact.size)

            smsBeanList = response.sms

            if(response.sms != null && response.sms.size > 0) {

                Log.d(Constant.TAG_RESTORE, "Enter in subscribe of onSuccess " + TAG)
                smsQueue = 1;
                restoreSms(response.sms)
            }

           var contactList =  BirinaUtility.getBackUpData(applicationContext).contact


           // var contactList =  response.contact

            val queueSize = 300 //400
            val contactQueue = contactList.size/queueSize

            if(contactQueue > 0) {

                var startIndex = 0
                var endIndex = 0

                var tempList: List<Request.ContactBean>? = null

                totalQueue = contactQueue + 1+smsQueue

                for (i in 0..contactQueue) {

                    startIndex = i * queueSize
                    endIndex = startIndex + queueSize

                    endIndex = if (endIndex < contactList.size) endIndex else contactList.size

                    tempList = contactList.subList(startIndex, endIndex);

                    Log.d(Constant.TAG_RESTORE, "In loop totalQueue: " + contactQueue + " i: " + i
                            + " startIndex: " + startIndex + "endIndex: " + endIndex + " Queuesize: " + tempList.size)

                    restoreContact(tempList);
                }
            }else{
                totalQueue = 1+smsQueue;
                restoreContact(contactList);

            }
        }else{

            Log.d(Constant.TAG_RESTORE, "Enter in else of onSuccess contact not available " + TAG)

            hideDialog();
            completeRestore(false)

            Toast.makeText(this, getString(R.string.contact_not_available), Toast.LENGTH_LONG).show()

            finish()
        }

        Log.d(Constant.TAG, "Exit from onSuccess of " + TAG)

    }

    override fun onFailure() {
        Log.d(Constant.TAG_RESTORE, "Enter in onFailure of " + TAG)


        Toast.makeText(this, getString(R.string.contact_not_available), Toast.LENGTH_LONG).show()
        completeRestore(false)
        finish()

        Log.d(Constant.TAG_RESTORE, "Exit from onFailure of " + TAG)

    }



    private fun restoreContact( contactList: List<Request.ContactBean>) {

        Log.d(Constant.TAG_RESTORE, "Enter in restoreContact of " + TAG)

      var contactUtility = ContactUtility(this)

        Observable.fromCallable { contactUtility.testBatchInsertion1(contactList); }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    totalCompleteOperation++

                    if(totalCompleteOperation === totalQueue){

                        Log.d(Constant.TAG_RESTORE, " in subscribe restoreContact " +
                                "totalCompleteOperation: "+ totalCompleteOperation +" totalQueue "+totalQueue)

                        hideDialog();
                        completeRestore(true)
                    }
                }

                )

        Log.d(Constant.TAG_RESTORE, "Exit from restoreContact of " + TAG)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.d(Constant.TAG_RESTORE, "Enter in onActivityResult  " + TAG)

        if (requestCode == SMS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    val myPackageName = packageName
                    if (Telephony.Sms.getDefaultSmsPackage(this) == myPackageName) {

                        saveSmsData(smsBeanList)
                    }
                }
            }
        }else if(requestCode == SMS_COMPLETE_REQUEST_CODE){
            Log.d(Constant.TAG_RESTORE, "Enter in SMS_COMPLETE_REQUEST_CODE " + TAG)

            totalCompleteOperation++

            if(totalCompleteOperation === totalQueue){

                Log.d(Constant.TAG_RESTORE, " in onActivityResult SMS_COMPLETE_REQUEST_CODE " +
                        "totalCompleteOperation: "+ totalCompleteOperation +" totalQueue "+totalQueue)

                hideDialog();
                completeRestore(true)
            }

        }

        Log.d(Constant.TAG_RESTORE, "Exit from onActivityResult of " + TAG)

    }


    private fun restoreSms(smsBeanList:List<Request.SmsBean> ) {

        Log.d(Constant.TAG_RESTORE, "Enter in restoreSms of " + TAG)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val myPackageName = packageName
            if (Telephony.Sms.getDefaultSmsPackage(this) != myPackageName) {

                val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName)
                startActivityForResult(intent, SMS_REQUEST_CODE)
            } else {
                saveSmsData(smsBeanList)
            }
        } else {
            saveSmsData(smsBeanList)
        }

        Log.d(Constant.TAG_RESTORE, "Exit from restoreSms of " + TAG)

    }




    fun saveSmsData(smsBeanList: List<Request.SmsBean>?){
        Log.d(Constant.TAG_RESTORE, "Enter in saveSmsData of " + TAG)

        Observable.fromCallable {  saveSms(smsBeanList)}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           Log.d(Constant.TAG_RESTORE, "Enter in subscribe saveSmsData of " + TAG)

                          setDefaultSmsApp()
                        }
                )
        Log.d(Constant.TAG, "Exit from saveSmsData of " + TAG)

    }



    private fun setDefaultSmsApp() {
        Log.d(Constant.TAG_RESTORE, "Enter in setDefaultSmsApp of " + TAG)

        val intent1 = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        intent1.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp)
        startActivityForResult(intent1, SMS_COMPLETE_REQUEST_CODE)

        Log.d(Constant.TAG_RESTORE, "Exit from setDefaultSmsApp of " + TAG)

    }



    fun saveSms(smsBeans: List<Request.SmsBean>?):Boolean {

        Log.d(Constant.TAG_RESTORE, "Enter in saveSms smsBeans Size " + smsBeans?.size)


        val MAX_OPERATIONS_FOR_INSERTION = 100 //100
        var ops = ArrayList<ContentProviderOperation>()

        smsBeans?.forEach {

            generateBatchOperation(ops, it)

            if (ops.size >= MAX_OPERATIONS_FOR_INSERTION) {
                applicationContext.getContentResolver().applyBatch(AUTHORITY, ops)
                ops.clear()
            }
        }

        if (ops.size > 0)
            applicationContext.getContentResolver().applyBatch(AUTHORITY, ops)

        Log.d(Constant.TAG_RESTORE, "Exit from saveSms of " + TAG)
          return true
    }


    fun generateBatchOperation( ops: ArrayList<ContentProviderOperation>,
                                smsBean : Request.SmsBean ){

        var uri: Uri? = null

        if (null != smsBean.address && !smsBean.address.isEmpty() &&
                null != smsBean.msg && !smsBean.msg.isEmpty() &&
                null != smsBean.time && !smsBean.time.isEmpty()) {

            Log.d(Constant.TAG_RESTORE, "SMS  address: " + smsBean.address
                    + " body: " + smsBean.msg + "\n read: " + smsBean.readState
                    + "time: " + smsBean.time +" FolderName "+smsBean.folderName)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                uri = Telephony.Sms.Sent.CONTENT_URI
                if (smsBean.folderName.equals("inbox")) {
                    uri = Telephony.Sms.Inbox.CONTENT_URI
                }
            } else {
                uri = Uri.parse("content://sms/" + smsBean.folderName)
            }

            Log.d(Constant.TAG_RESTORE," uri "+uri);

            ops.add(ContentProviderOperation.newInsert(uri)
                    .withValue("address", smsBean.address)
                    .withValue("body", smsBean.msg)
                    .withValue("read", smsBean.readState)
                    .withValue("date", smsBean.time)
                    .build())
        }
    }

    fun startRestore() {
        if (mShimmer != null && mShimmer?.isAnimating()!!) {
            mShimmer?.cancel()
        } else {
            mShimmer = Shimmer()
            mShimmer?.start(mRestoreMsgText)
        }
    }

     fun completeRestore(isSuccessful: Boolean) {
        if (mShimmer != null && mShimmer?.isAnimating()!!) {
            mShimmer?.cancel()
            mRestoreMsgText?.setVisibility(View.GONE)
        }
        if(isSuccessful)
        mRestoreCompleteTxt?.setVisibility(View.VISIBLE)
    }
}
