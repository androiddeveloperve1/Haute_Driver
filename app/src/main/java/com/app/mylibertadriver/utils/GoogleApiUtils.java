package com.app.mylibertadriver.utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.interfaces.OnAddressListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class GoogleApiUtils {



    public static void getLatLongByPlace(final String placeID, final OnAddressListener listener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                    StringBuilder sb = new StringBuilder(Constants.PLACES_API_BASE + Constants.TYPE_DETAILS + Constants.OUT_JSON);
                    sb.append("?placeid=" + URLEncoder.encode(placeID, "utf8"));
                    sb.append("&key=" + Constants.API_KEY);
                    URL url = new URL(sb.toString());
                    Log.e("@@@@@@@@@", "" + url);
                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        jsonResults.append(buff, 0, read);
                    }
                    JSONObject jsonObj = new JSONObject(jsonResults.toString());
                    Double lat = Double.valueOf(0);
                    Double lng = Double.valueOf(0);
                    lat = (Double) jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat");
                    lng = (Double) jsonObj.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng");
                    Log.e("@@@@@@", "" + lat + "-" + lng);
                    listener.onAddressFound(new LatLng(lat, lng));
                } catch (Exception e) {
                    listener.onAddressError(e.getLocalizedMessage());
                    Log.e("@@@@@@@@@@", "Error connecting to Places API", e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                return null;
            }
        }.execute();
    }


    public static void getAddressFromLatLong(LatLng latLng, final OnAddressListener listener) {

        new AsyncTask<LatLng, Void, String>() {
            @Override
            protected String doInBackground(LatLng... strings) {
                Log.e("@@@@@@@@", "" + strings[0]);
                String urlAPI = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + strings[0].latitude + "," + strings[0].longitude + "&key=" + Constants.API_KEY;
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                    URL url = new URL(urlAPI);
                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        jsonResults.append(buff, 0, read);
                    }

                    JSONArray Results = new JSONObject(jsonResults.toString()).getJSONArray("results");
                    JSONObject zero = Results.getJSONObject(0);
                    String Address = zero.getString("formatted_address");
                    if (Address != null) {
                        listener.onAddressFound(Address);
                    } else {
                        listener.onAddressError("No Address found");
                    }
                    Log.e("@@@@@@@@", jsonResults.toString());
                } catch (Exception e) {
                    listener.onAddressError("No Address found");
                    Log.e("@@@@@@@@", "" + e.getMessage());
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute(latLng);
    }




}
