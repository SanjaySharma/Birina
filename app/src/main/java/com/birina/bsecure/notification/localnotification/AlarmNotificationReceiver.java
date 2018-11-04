package com.birina.bsecure.notification.localnotification;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;


import com.birina.bsecure.R;
import com.birina.bsecure.junkcleaner.activity.PhoneBoosterActivity;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class AlarmNotificationReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent myIntent = new Intent(context, PhoneBoosterActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                FLAG_ONE_SHOT );


        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_app_logo)
                .setContentTitle(context.getResources().getString(R.string.cache_clean))
                .setStyle(new NotificationCompat .BigTextStyle().bigText(context.getResources()
                        .getString(R.string.cache_clean)))
                .setStyle(new NotificationCompat .BigTextStyle().bigText(context.getResources()
                .getString(R.string.notification_desc)).setSummaryText(""))
                .setContentIntent(pendingIntent)
                .setContentText(context.getResources().getString(R.string.notification_desc))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

    }
}