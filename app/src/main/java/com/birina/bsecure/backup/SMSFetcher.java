package com.example.bsecure.backup;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.bsecure.backup.model.Request;
import com.example.bsecure.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by narendra on 27/02/18.
 */

public class SMSFetcher {

    public List<Request.SmsBean> getAllSms(Activity activity) {
        List<Request.SmsBean> lstSms = new ArrayList<Request.SmsBean>();

        try{
        Request.SmsBean objSms = new Request.SmsBean();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = activity.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);

        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Request.SmsBean();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }catch (Exception e){
            Log.d(Constant.TAG_RESTORE, "Exception in getAllSms "+e );
        }

        return lstSms;

    }


}
