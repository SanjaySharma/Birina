package com.example.birina.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by 1001400 on 6/7/2016.
 */

public class ConnectionManager {
    // Checks the network connection and return a boolean variables accordingly
    public static  boolean isInternetAvailable(Context mCtx) {
        ConnectivityManager connMgr = (ConnectivityManager)
                mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnected();
    }
}
