package com.example.birina.backup;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.birina.backup.model.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by narendra on 27/02/18.
 */

public class SMSFetcher {

    public List<Request.SmsBean> getAllSms(Activity activity) {
        List<Request.SmsBean> lstSms = new ArrayList<Request.SmsBean>();
        Request.SmsBean objSms = new Request.SmsBean();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = activity.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        activity.startManagingCursor(c);
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
    }

    public void inserSMS(Activity activity){
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = activity.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("address", "9599385903");
        values.put("body", "test message");
        cr.insert(message,values);

    }
}
