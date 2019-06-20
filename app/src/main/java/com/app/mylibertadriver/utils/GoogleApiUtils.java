package com.app.mylibertadriver.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.interfaces.OnAddressListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class GoogleApiUtils {

    static DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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


    public static double getDistanceBitweenLatlongInKM(LatLng from, LatLng to) {

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
        return (dist);
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
}
