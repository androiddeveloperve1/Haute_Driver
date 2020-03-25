package com.app.mylibertadriver.prefes;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.mylibertadriver.model.DriverModel;
import com.google.gson.Gson;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class MySharedPreference {

    public static final String APP_PREFERENCE = "haute_driver_ve";
    private static final String User_Detail = "user_data";
    private static final String USER_SESSION_TOKEN = "user_session_token";
    private static final String FCM_TOKEN = "fcm_token";
    static final String RESTAURANT_DELIVERY_TIME = "time";
    static final String ACCEPT_TIME = "accept_time";


    private static MySharedPreference instence;
    private static Context mContext;
    private static SharedPreferences sharedpreferences;


    public MySharedPreference(Context mContext) {
        this.mContext = mContext;
        sharedpreferences = mContext.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE);
    }


    public static MySharedPreference getInstance(Context mContext) {
        if (instence == null) {
            instence = new MySharedPreference(mContext);
        }
        return instence;
    }



    public void clearMyPreference() {
        sharedpreferences.edit().clear().commit();
    }

    public DriverModel getUser() {
        return new Gson().fromJson(sharedpreferences.getString(User_Detail, null), DriverModel.class);
    }

    public void setUser(DriverModel data) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(User_Detail, new Gson().toJson(data));
        editor.commit();
    }

    public String getSessionToken() {
        return sharedpreferences.getString(USER_SESSION_TOKEN, null);
    }

    public void setSessionToken(String data) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USER_SESSION_TOKEN, data);
        editor.commit();
    }


    public String getFCM() {
        return sharedpreferences.getString(FCM_TOKEN, null);
    }

    public void setFCM(String data) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(FCM_TOKEN, data);
        editor.commit();
    }


    public String getAcceptTime() {
        return sharedpreferences.getString(ACCEPT_TIME, null);
    }

    public void setAcceptTime(String time) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(ACCEPT_TIME, time);
        editor.commit();
    }

    public String getRestroDeliveryTime() {
        return sharedpreferences.getString(RESTAURANT_DELIVERY_TIME, null);
    }

    public void setRestroDeliveryTime(String time) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(RESTAURANT_DELIVERY_TIME, time);
        editor.commit();
    }

}
