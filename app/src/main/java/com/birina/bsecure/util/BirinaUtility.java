package com.birina.bsecure.util;

import android.content.Context;

import com.birina.bsecure.backup.model.Request;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Admin on 3/29/2018.
 */

public class BirinaUtility {


    public static Request getBackUpData(Context mContext) {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("compliance_score1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Request request = null;

        Gson gson = new Gson();

        try {
            request = gson.fromJson(json, Request.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
