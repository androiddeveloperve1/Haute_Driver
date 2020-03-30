package com.app.mylibertadriver.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.activities.MainActivity;
import com.app.mylibertadriver.interfaces.TimerListener;
import com.app.mylibertadriver.model.NotificationDataModel;
import com.app.mylibertadriver.prefes.MySharedPreference;
import com.app.mylibertadriver.services.TimerService;
import com.google.gson.Gson;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyCountDownTimer extends CountDownTimer {
    private TimerListener listener;
    private static final long TIMER_DIFFERENT = 1000;


    public MyCountDownTimer(TimerListener listener, long inFuture, long interval) {
        super(inFuture, interval);
        this.listener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        listener.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        listener.onFinish();

    }

    public static class MyTimer {
        public CountDownTimer startNow(Context mContext, TimerListener listener) {
            MySharedPreference pref = MySharedPreference.getInstance(mContext);
            int maxDeliveryTimeInSecond = Integer.parseInt(pref.getRestroDeliveryTime()) * 60;
            long acceptTimeMilliSecond = AppUtils.getMilliFromDate(pref.getAcceptTime())+19800000;
            long myCurrentMillisecond = System.currentTimeMillis();
            long differentSecond = (myCurrentMillisecond - acceptTimeMilliSecond) / 1000;
            long timeelapsed = maxDeliveryTimeInSecond - differentSecond;
            if (timeelapsed < 0) {
                listener.onExpire();
                sendPush(mContext);
                return null;
            } else {
                startServiceNow(mContext);
                return new MyCountDownTimer(listener, timeelapsed * 1000, TIMER_DIFFERENT).start();
            }
        }
    }
    static void sendPush(Context mContext) {
        if (!MySharedPreference.getInstance(mContext).getLatePush()) {
            MySharedPreference.getInstance(mContext).setLatePush(true);
            Log.e("@@@@@@@@", "Push");
        }
        //sendNotification(mContext);
    }

    static void startServiceNow(Context mContext) {
        Intent serviceIntent = new Intent(mContext, TimerService.class);
        ContextCompat.startForegroundService(mContext, serviceIntent);
    }

    private static void sendNotification(Context mContext) {
        // Get an instance of the Notification manager
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel = new NotificationChannel("liberta_driver", name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }

        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(mContext, MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle("Delivery")
                .setContentText("Your time has been Expired ")
                .setContentIntent(notificationPendingIntent);

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("liberta"); // Channel ID
        }

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

}