/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.birina.bsecure.notification.remotenotification;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.birina.bsecure.R;
import com.birina.bsecure.dashboard.WebActivity;
import com.birina.bsecure.junkcleaner.activity.PhoneBoosterActivity;
import com.birina.bsecure.login.LoginActivity;
import com.birina.bsecure.login.model.LogInRequestModel;
import com.birina.bsecure.login.model.LogInResponseModel;
import com.birina.bsecure.network.RestClient;
import com.birina.bsecure.notification.TokenRegistrationRequest;
import com.birina.bsecure.notification.TokenRegistrationResponse;
import com.birina.bsecure.remotescreaming.RemoteScreamingService;
import com.birina.bsecure.util.BirinaPrefrence;
import com.birina.bsecure.util.Constant;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.PendingIntent.FLAG_ONE_SHOT;
import static com.birina.bsecure.util.Constant.WEB_INTENT_KEY;


public class BiSecureFirebaseMessagingService  extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";



    Map<String, String> notificationMap = null;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "hij: " + remoteMessage.getFrom());



        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            notificationMap = remoteMessage.getData();
            if(null != notificationMap){
              //  startRemoteNotificationService();

                rx.Observable.just(getRemoteImage())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(obj -> {
                            if (null != obj) {
                                sendNotification(obj);
                            }
                        });
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private Bitmap getRemoteImage(){
        Bitmap myBitmap= null;
        try {

            URL url = new URL(notificationMap.get(Constant.IMAGE_URL_KEY));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream in = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myBitmap;
    }

    private void sendNotification( Bitmap bitmap) {



        Notification.Builder builder = new Notification .Builder(getApplicationContext());

        Intent myIntent = new Intent(getApplicationContext(), WebActivity.class);
        myIntent.putExtra(Constant.TARGET_LINK_KEY, notificationMap.get(Constant.TARGET_LINK_KEY));
        myIntent.putExtra(WEB_INTENT_KEY, Constant.URL_NOTIFICATION);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                myIntent,
                FLAG_ONE_SHOT );

        String channelId = getString(R.string.notification_chhenal);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(notificationMap.get(Constant.TITLE_KEY))
                //.setStyle(new Notification .BigTextStyle().bigText(notificationMap.get(Constant.TITLE_KEY)))
                //.setStyle(new Notification .BigTextStyle().bigText(notificationMap.get(Constant.MESSAGE_KEY)).setSummaryText(""))
                .setContentIntent(pendingIntent)
                .setContentText(notificationMap.get(Constant.MESSAGE_KEY))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info")
                //.setColor(Notification .getColor(context, R.color.colorPrimary))
                .setLargeIcon(bitmap)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(bitmap))
                .setSmallIcon(R.drawable.ic_app_logo);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

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
                startForeground(17, builder.build());
        }else{
            notificationManager.notify(1,builder.build());
        }



    }




    private void sendRegistrationToServer(String token) {

        RestClient restClient = new RestClient();

        Observable<Response<TokenRegistrationResponse>> tokenResponsePayload
                = restClient.getApiService().registrationToken(new TokenRegistrationRequest(token));

        tokenResponsePayload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(elabelResponse ->
                        {
                            if (null != elabelResponse && elabelResponse.isSuccessful() &&
                                    null != elabelResponse.body() && null != elabelResponse.body().getResponse()) {

                                Log.e(TAG, "sendRegistrationToServer success call: " );

                            } else {

                                throw new RuntimeException("Login Fail");
                            }
                        },
                        t -> {
                           Log.e(TAG, "Error in sendRegistrationToServer(): "+t);
                        });
    }


}