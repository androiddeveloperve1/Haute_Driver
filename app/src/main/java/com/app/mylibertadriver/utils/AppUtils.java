package com.app.mylibertadriver.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


//code improvement, handle permission gps, geofencing , distance and time ,

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class AppUtils {
    static DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static boolean eMailValidation(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static Date getUTCDateObjectFromUTCTime(String data) {
        try {
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return utcFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Date getCurrentDateINUTC() {
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return getUTCDateObjectFromUTCTime(utcFormat.format(new Date()));


    }


    public static String getUrlForDrawRoute(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + Constants.API_KEY;
        return url;
    }


    public static void requestCall(Context mContext, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        mContext.startActivity(intent);
    }

    public static Bitmap getLocatinIcon(Context mContext) {
        BitmapDrawable b = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.ic_current_location);
        return Bitmap.createScaledBitmap(b.getBitmap(), (int) mContext.getResources().getDimension(R.dimen._20_px), (int) mContext.getResources().getDimension(R.dimen._20_px), false);
    }


    public static String getDrivingTimeFromValue(String value) {
        Log.e("@@@@@@@@",""+value);
        DecimalFormat f = new DecimalFormat("##.00");
        String travelTime = "0 Mins";
        try{
            float valueFloat = Float.parseFloat(value) / 3600;
            String valueString = String.valueOf(f.format(valueFloat));
            String hours = valueString.split("\\.")[0];
            String mins = valueString.split("\\.")[1];
            if (hours.equals("0")) {
                travelTime = mins + "Mins.";
            } else if (mins.equals("0")) {
                travelTime = hours + "Hours";
            }else {
                travelTime = hours + " Hours\n"+mins+" Min.(s)";
            }

        }catch (Exception e){}


        return travelTime;
    }
}
