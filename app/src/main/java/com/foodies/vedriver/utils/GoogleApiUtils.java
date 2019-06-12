package com.foodies.vedriver.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.foodies.vedriver.adapter.GooglePlacesAutocompleteAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class GoogleApiUtils {


    public static String getAddressFromLatLong(LatLng latLng) {
        String urlAPI = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.longitude + "," + latLng.longitude + "&key=" + GooglePlacesAutocompleteAdapter.API_KEY;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url = new URL(urlAPI);
            Log.e("@@@@@@@@@@", urlAPI);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.e("@@@@@@@@@", "" + jsonObj);


        } catch (Exception e) {

            return null;

        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }


        return null;
    }


    public static LatLng getLatLongByPlace(final String placeID) {
        final LatLng[] lat = {null};

        new AsyncTask<Void, String, LatLng>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected LatLng doInBackground(Void... params) {
                return performPlaceAPIHit(placeID);
            }

            @Override
            protected void onPostExecute(LatLng dummy) {
                lat[0] = dummy;
                Log.e("@@@@@@@", "" + new Gson().toJson(dummy));


            }
        }.execute();

        return lat[0];
    }

    private static LatLng performPlaceAPIHit(String placeID) {
        LatLng l = null;
        HttpURLConnection conn = null;

        StringBuilder jsonResults = new StringBuilder();

        try {

            StringBuilder sb = new StringBuilder(GooglePlacesAutocompleteAdapter.PLACES_API_BASE + GooglePlacesAutocompleteAdapter.TYPE_DETAILS + GooglePlacesAutocompleteAdapter.OUT_JSON);

            sb.append("?placeid=" + URLEncoder.encode(placeID, "utf8"));

            sb.append("&key=" + GooglePlacesAutocompleteAdapter.API_KEY);

            URL url = new URL(sb.toString());

            conn = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder

            int read;

            char[] buff = new char[1024];

            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("@@@@@@@@@", "Error processing Places API URL", e);
        } catch (IOException e) {
            Log.e("@@@@@@@@@@", "Error connecting to Places API", e);
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            l = parse(jsonObj);

        } catch (JSONException e) {
            Log.e("@@@@@@@@", "Cannot process JSON results", e);
        }
        return l;

    }

    public static LatLng parse(JSONObject jObject) {

        Double lat = Double.valueOf(0);
        Double lng = Double.valueOf(0);

        try {
            lat = (Double) jObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat");
            lng = (Double) jObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("@@@@@@", "" + lat + "-" + lng);

        return new LatLng(lat, lng);


    }
}
