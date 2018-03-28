package com.example.birina.restore

import android.Manifest
import android.app.Activity
import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.birina.R
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.birina.backup.model.Request
import com.example.birina.restore.model.RestoreRequest
import com.example.birina.util.BirinaPrefrence
import com.example.birina.util.ContactUtility
import kotlinx.android.synthetic.main.activity_backup.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import android.net.Uri
import android.os.Build
import android.os.RemoteException
import android.provider.Telephony
import android.util.Log
import com.example.birina.util.Constant
import java.util.ArrayList


class RestoreActivity : AppCompatActivity(), RestoreContract.View {

    val TAG = "RestoreActivity"

    val REQUEST_CODE_ASK_PERMISSIONS = 123
    var mPresenter: RestorePresenter ? = null
    var mProgressBar: ProgressBar? = null

     var defaultSmsApp: String ?= null
     val SMS_REQUEST_CODE = 1
     val SMS_COMPLETE_REQUEST_CODE = 2

    var smsBeanList:List<Request.SmsBean> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)
        val result = RestorePresenter(this)
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
            restoreConotactAndSms()

        }
    }

    private fun restoreConotactAndSms() {

        //"8109243585"

        var request = RestoreRequest()

        request.mobile=BirinaPrefrence.getRegisteredNumber(this)

       // request.mobile = "9599385905";

        mPresenter?.restoreBackup(request);
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults.size > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                restoreConotactAndSms()
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
            Observable.fromCallable { restoreContact(response) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { obj ->
                                if(response.sms != null && response.sms.size > 0) {

                                    Log.d(Constant.TAG_RESTORE, "Enter in subscribe of onSuccess " + TAG)

                                    restoreSms(response.sms)
                                }else{
                                    Log.d(Constant.TAG_RESTORE, "Enter in else of subscribe onSuccess, " +
                                            "sms not available " + TAG)

                                    hideDialog();
                                    Toast.makeText(this, getString(R.string.restore_success), Toast.LENGTH_LONG).show()

                                    finish()
                                }
                            })
        }else{

            Log.d(Constant.TAG_RESTORE, "Enter in else of onSuccess contact not available " + TAG)

            hideDialog();

            Toast.makeText(this, getString(R.string.restore_success), Toast.LENGTH_LONG).show()

            finish()
        }

        Log.d(Constant.TAG, "Exit from onSuccess of " + TAG)

    }

    override fun onFailure() {
        Log.d(Constant.TAG_RESTORE, "Enter in onFailure of " + TAG)


        Toast.makeText(this, getString(R.string.restore_failed), Toast.LENGTH_LONG).show()
        finish()

        Log.d(Constant.TAG_RESTORE, "Exit from onFailure of " + TAG)

    }



    private fun restoreContact(request : Request) {

        Log.d(Constant.TAG_RESTORE, "Enter in restoreContact of " + TAG)

        var contactList =  request.contact
      var contactUtility = ContactUtility(this)

            contactUtility.insertContacts(contactList)


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

            hideDialog();
            finish()

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


    fun saveSms(smsBeanList: List<Request.SmsBean>?){
        try {

            Log.d(Constant.TAG_RESTORE, "Enter in saveSms of " + TAG)

            smsBeanList?.forEach {

                Log.d(Constant.TAG_RESTORE, "address: " + it.address + " body: " + it.msg + "\n read: " + it.readState
                        + "time: " + it.time)

                if( ( null != it.address && it.address.isNotEmpty()) &&
                        ( null != it.msg && it.msg.isNotEmpty()) &&
                        ( null != it.time && it.time.isNotEmpty() && Integer.parseInt(it.time) > 0) ){

                val values = ContentValues()
                values.put("address", it.address)
                values.put("body", it.msg)
                values.put("read", it.readState) //"0" for have not read sms and "1" for have read sms
                values.put("date", it.time)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    var uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Telephony.Sms.Sent.CONTENT_URI
                    } else {
                        applicationContext.contentResolver.insert(Uri.parse("content://sms/" + it.folderName), values)
                    }
                    if (it.folderName == "inbox") {
                        uri = Telephony.Sms.Inbox.CONTENT_URI
                    }
                    applicationContext.contentResolver.insert(uri, values)
                } else {
                    applicationContext.contentResolver.insert(Uri.parse("content://sms/" + it.folderName), values)
                }
            }
            }

            Log.d(Constant.TAG_RESTORE, "Exit from saveSms of " + TAG)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }


   /* fun  saveSms(smsBeans: List<Request.SmsBean>?):Boolean {
        try {
            Log.d(Constant.TAG_RESTORE, "Enter in saveSms of " + TAG)

            val contentValues = arrayOfNulls<ContentValues>(smsBeans?.size!!)
            var uri: Uri? = null

            for (i in smsBeans?.indices!!) {


                Log.d(Constant.TAG_RESTORE,"SMS  address: "+smsBeans[i].address + " body: "+smsBeans[i].msg + "\n read: "+smsBeans[i].readState
                        +"time: "+smsBeans[i].time)

                val values = ContentValues()
                values.put("address", smsBeans[i].address)
                values.put("body", smsBeans[i].msg)
                values.put("read", smsBeans[i].readState) //"0" for have not read sms and "1" for have read sms
                values.put("date", smsBeans[i].time)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    uri = Telephony.Sms.Sent.CONTENT_URI
                    if (smsBeans[i].folderName == "inbox") {
                        uri = Telephony.Sms.Inbox.CONTENT_URI
                    }
                } else {
                    uri = Uri.parse("content://sms/" + smsBeans[i].folderName)
                }

                contentValues[i] = values
            }


            applicationContext.contentResolver.bulkInsert(uri!!, contentValues)

            Log.d(Constant.TAG_RESTORE, "Exit from saveSms of " + TAG)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
         return true;
    }*/


    private fun setDefaultSmsApp() {
        Log.d(Constant.TAG_RESTORE, "Enter in setDefaultSmsApp of " + TAG)

        hideDialog()
        val intent1 = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        intent1.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp)
        startActivity(intent1)

        Log.d(Constant.TAG_RESTORE, "Exit from setDefaultSmsApp of " + TAG)

    }


}
