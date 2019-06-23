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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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


    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
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

    public static String getDistanceBitweenLatlongInKM(LatLng from, LatLng to) {

        Log.e("@@@@@", "FROM" + from.latitude + ":" + from.longitude + "--TO" + to.latitude + ":" + to.longitude);
        double lat1 = from.latitude;
        double lon1 = from.longitude;
        double lat2 = to.latitude;
        double lon2 = to.longitude;
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return String.format("%.2f", dist);  // in km*/


       /* Location fromLocation = new Location("From");
        fromLocation.setLatitude(from.latitude);
        fromLocation.setLongitude(from.longitude);


        Location toLocation = new Location("To");
        toLocation.setLatitude(to.latitude);
        toLocation.setLongitude(to.longitude);


        return String.format("%.2f", (fromLocation.distanceTo(toLocation)) / 1000);  // in km*/

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



}
