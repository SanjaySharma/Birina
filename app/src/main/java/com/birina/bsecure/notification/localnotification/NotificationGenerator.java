package com.birina.bsecure.notification.localnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.birina.bsecure.R;
import com.birina.bsecure.dashboard.WebActivity;
import com.birina.bsecure.junkcleaner.activity.PhoneBoosterActivity;
import com.birina.bsecure.util.Constant;

import static android.app.PendingIntent.FLAG_ONE_SHOT;
import static com.birina.bsecure.util.Constant.WEB_INTENT_KEY;

public class NotificationGenerator {

    public static void generateNotification(Context context){

        Log.d("BirinaActivity "," generateNotification ");

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

    public static void generateNotificationO(Context context){

        Log.d("BirinaActivity "," generateNotificationO ");

        Notification.Builder builder = new Notification .Builder(context.getApplicationContext());

        Intent myIntent = new Intent(context, PhoneBoosterActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                FLAG_ONE_SHOT );

        String channelId = context.getString(R.string.notification_chhenal);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(context.getResources().getString(R.string.cache_clean))
                //.setStyle(new Notification .BigTextStyle().bigText(notificationMap.get(Constant.TITLE_KEY)))
                //.setStyle(new Notification .BigTextStyle().bigText(notificationMap.get(Constant.MESSAGE_KEY)).setSummaryText(""))
                .setContentIntent(pendingIntent)
                .setContentText(context.getResources().getString(R.string.notification_desc))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info")
                .setSmallIcon(R.drawable.ic_app_logo);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);

            builder.setChannelId(channelId);
            notificationManager.notify(1,builder.build());
        }else{
            notificationManager.notify(1,builder.build());
        }

    }
}
