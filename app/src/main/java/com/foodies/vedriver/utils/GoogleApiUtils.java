package com.foodies.vedriver.utils;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NavUtils;

import com.foodies.vedriver.adapter.GooglePlacesAutocompleteAdapter;
import com.foodies.vedriver.constants.Constants;
import com.foodies.vedriver.interfaces.OnAddressListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
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
