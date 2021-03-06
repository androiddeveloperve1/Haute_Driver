package com.app.mylibertadriver.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.activities.MainActivity;
import com.app.mylibertadriver.model.NotificationDataModel;
import com.app.mylibertadriver.prefes.MySharedPreference;
import com.app.mylibertadriver.utils.AppUtils;
import com.app.mylibertadriver.utils.MyCountDownTimer;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

public class TimerService extends Service {
    int noti_id = 1;
    private String CHANNEl_ID="driver";
    int interval = 1000; //1 sec
    int elapsedTime = 0;
    MyCountDownTimer timer;
    long totalTime;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getBooleanExtra("shouldStop", false)) {
            Log.e("@@@@@@@@@", "stop");
            deliveryDone();

        } else {
            MySharedPreference pref = MySharedPreference.getInstance(TimerService.this);
            int maxDeliveryTimeInSecond = Integer.parseInt(pref.getRestroDeliveryTime()) * 60;
            long acceptTimeMilliSecond = AppUtils.getMilliFromDate(pref.getAcceptTime()) + 19800000;
            long myCurrentMillisecond = System.currentTimeMillis();
            long differentSecond = (myCurrentMillisecond - acceptTimeMilliSecond) / 1000;
            //totalTime = 20 * 1000;
            totalTime = (maxDeliveryTimeInSecond - differentSecond) * 1000;
            if (elapsedTime <= totalTime) {
                timer = new MyCountDownTimer(totalTime, 1000);
                timer.start();
            } else {
                stopTimerExpiredNow();
            }
            Log.e("@@@@@@@@@", "startd");
        }


        return START_NOT_STICKY;
    }


    void stopTimerExpiredNow() {
        try{
            timer.cancel();
        }catch (Exception e){}

        stopSelf();
        stopForeground(false);
        Notification notification = getMyActivityNotification("Your time has been Expired ", "", true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEl_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setSound(null, null);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        mNotificationManager.notify(1, notification);

    }

    void deliveryDone() {
        try{
            timer.cancel();
        }catch (Exception e){}

        stopSelf();
        stopForeground(false);
      /*  Notification notification = getMyActivityNotification("Delivered", "", true);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEl_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        mNotificationManager.notify(1, notification);*/

    }

    private Notification getMyActivityNotification(String Title, String body, boolean isFinished) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle(Title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEl_ID); // Channel ID
        }
        if (isFinished) {
            builder.setProgress((int) totalTime, 0, false);
        } else {
            builder.setProgress((int) totalTime, (int) (totalTime - elapsedTime), false);
        }

        builder.setAutoCancel(true);
        return builder.getNotification();
    }


    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long inFuture, long interval) {
            super(inFuture, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Notification notification = getMyActivityNotification("Your time expires within", Msg(), false);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.app_name);
                NotificationChannel mChannel = new NotificationChannel(CHANNEl_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
                mChannel.setSound(null, null);
                mNotificationManager.createNotificationChannel(mChannel);
            }
            startForeground(noti_id, notification);
            mNotificationManager.notify(1, notification);
            elapsedTime = elapsedTime + interval;
        }

        @Override
        public void onFinish() {
            stopTimerExpiredNow();

        }
    }

    String Msg() {
        long millisUntilFinished = totalTime - elapsedTime;
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + " Second(s)";
    }
}
