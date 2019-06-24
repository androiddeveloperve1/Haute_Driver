package com.app.mylibertadriver.fcmutils;

import android.util.Log;

import com.app.mylibertadriver.prefes.MySharedPreference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("##########", "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.e("##########", "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("##########", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.e("##########", "Refreshed token: " + token);
        MySharedPreference.getInstance(this).setFCM(token);

    }
}
