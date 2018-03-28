package com.example.birina.login;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Admin on 3/24/2018.
 */

public class HeadlessSmsSendService extends IntentService {
    public HeadlessSmsSendService() {
        super(HeadlessSmsSendService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}