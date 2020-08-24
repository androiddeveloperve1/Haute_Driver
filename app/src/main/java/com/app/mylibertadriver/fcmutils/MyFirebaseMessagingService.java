package com.app.mylibertadriver.fcmutils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.activities.MainActivity;
import com.app.mylibertadriver.model.NotificationDataModel;
import com.app.mylibertadriver.prefes.MySharedPreference;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

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
            sendNotification(new Gson().toJson(remoteMessage.getData()));
        }

    }

    @Override
    public void onNewToken(String token) {
        Log.e("##########", "Refreshed token: " + token);
        MySharedPreference.getInstance(this).setFCM(token);

    }

    private void sendNotification(String notificationDetails) {
        // Get an instance of the Notification manager
        NotificationDataModel data = new Gson().fromJson(notificationDetails, NotificationDataModel.class);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel = new NotificationChannel("liberta_driver", name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }

        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)

                .setContentTitle(data.title)
                .setContentText(data.body)
                .setContentIntent(notificationPendingIntent);

        if (data.getType().equals("neworder")) {
            builder.setSound(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.new_order));
        }else{
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(uri);
        }
        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("liberta"); // Channel ID
        }
        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        mNotificationManager.notify(0, builder.build());
    }
}
