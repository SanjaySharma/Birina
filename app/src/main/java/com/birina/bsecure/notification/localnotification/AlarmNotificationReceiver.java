package com.birina.bsecure.notification.localnotification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;


import com.birina.bsecure.R;
import com.birina.bsecure.dashboard.WebActivity;
import com.birina.bsecure.junkcleaner.activity.PhoneBoosterActivity;
import com.birina.bsecure.util.Constant;

import static android.app.PendingIntent.FLAG_ONE_SHOT;
import static com.birina.bsecure.util.Constant.WEB_INTENT_KEY;

public class AlarmNotificationReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           NotificationGenerator.generateNotificationO(context);
        }else {
            NotificationGenerator.generateNotification(context);
        }
    }

}