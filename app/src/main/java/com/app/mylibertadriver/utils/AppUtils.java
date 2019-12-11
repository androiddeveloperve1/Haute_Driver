package com.app.mylibertadriver.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.app.mylibertadriver.BuildConfig;
import com.app.mylibertadriver.R;
import com.app.mylibertadriver.activities.MainActivity;
import com.app.mylibertadriver.constants.Constants;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


//code improvement, handle permission gps, geofencing , distance and time ,

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class AppUtils {
    public static DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    static DateFormat humanFormat = new SimpleDateFormat("dd MMM, yyyy'.' HH:mm");
    static DateFormat humanFormat2 = new SimpleDateFormat("yyyy-MM-dd");

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
        if (value.contains("hour")) {
            String arr[] = value.split(" ");
            return arr[0] + arr[1] + "\n" + arr[2] + arr[3];
        } else {
            String arr[] = value.split(" ");
            return arr[0] + arr[1];
        }
    }


    public static String getTextFromStatus(String status) {
        String flag = null;
        if (status.equals("0")) {
            flag = Constants.DELIVERY_STATUS_0;
        } else if (status.equals("1")) {
            flag = Constants.DELIVERY_STATUS_1;
        } else if (status.equals("2")) {
            flag = Constants.DELIVERY_STATUS_2;
        } else if (status.equals("3")) {
            flag = Constants.DELIVERY_STATUS_3;
        } else if (status.equals("4")) {
            flag = Constants.DELIVERY_STATUS_4;
        } else if (status.equals("5")) {
            flag = Constants.DELIVERY_STATUS_5;
        } else if (status.equals("6")) {
            flag = Constants.DELIVERY_STATUS_6;
        }
        return flag;
    }


    public static String getHumanReadableTimeFromUTCString(String status) {
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return humanFormat.format(utcFormat.parse(status));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static Uri saveImage(Context context, Bitmap mBitmap) {
        File file = null;
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            file = new File(path, "demo.png");
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
            Log.e("@@@@@@@@", "Image saved" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null) {
            Uri u = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            Log.e("@@@@@@@@", "URi"+u.getPath());
            return u;
        } else {
            return
                    null;
        }
    }

    public static String getDecimalFormat(String value)
    {
        return String.format("%.02f", Float.parseFloat(value));
    }

    public static String getPaidUnpaidStatus(String value)
    {
        return Integer.parseInt(value)==0?"UnPaid":"Paid";
    }


    public static String showDateSlot(String from,String to)

    {

        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return getHumanReadableTimeFromUTCString2(from)+" To "+getHumanReadableTimeFromUTCString2(to);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getHumanReadableTimeFromUTCString2(String status) {
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return humanFormat2.format(utcFormat.parse(status));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }


}
