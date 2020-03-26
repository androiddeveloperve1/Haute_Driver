package com.app.mylibertadriver.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.app.mylibertadriver.interfaces.TimerListener;
import com.app.mylibertadriver.prefes.MySharedPreference;

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
            int maxDeliveryTimeInSecond = 10000 * 60;
            //int maxDeliveryTime = Integer.parseInt(pref.getRestroDeliveryTime()) *60;
            long acceptTimeMilliSecond = AppUtils.getMilliFromDate(pref.getAcceptTime());
            long myCurrentMillisecond = new Date(System.currentTimeMillis() - Calendar.getInstance().get(Calendar.ZONE_OFFSET) + Calendar.getInstance().get(Calendar.DST_OFFSET)).getTime();
            long differentSecond = (myCurrentMillisecond - acceptTimeMilliSecond) / 1000;
            long timeelapsed = maxDeliveryTimeInSecond - differentSecond;
            if (timeelapsed < 0) {
                listener.onExpire();
                sendPush(mContext);
                return null;
            } else {
                return new MyCountDownTimer(listener, timeelapsed * 1000, TIMER_DIFFERENT).start();
            }
        }
    }

    static void sendPush(Context mContext) {
        if (!MySharedPreference.getInstance(mContext).getLatePush()) {
            MySharedPreference.getInstance(mContext).setLatePush(true);
            Log.e("@@@@@@@@", "Push");
        }
    }
}