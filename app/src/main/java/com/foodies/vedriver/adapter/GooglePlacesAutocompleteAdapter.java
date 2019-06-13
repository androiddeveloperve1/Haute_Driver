package com.foodies.vedriver.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.foodies.vedriver.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {


    private ArrayList resultList;
    ArrayList placeIdList = null;

    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index).toString();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();

                if (constraint != null) {

                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());


                    // Assign the data to the FilterResults
                    filterResults.values = resultList;

                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private ArrayList autocomplete(String input) {

        ArrayList resultList = null;


        HttpURLConnection conn = null;

        StringBuilder jsonResults = new StringBuilder();

        try {

            StringBuilder sb = new StringBuilder(Constants.PLACES_API_BASE + Constants.TYPE_AUTOCOMPLETE + Constants.OUT_JSON);

            sb.append("?key=" + Constants.API_KEY);

//            sb.append("&components=country:in");

            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            conn = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder

            int read;

            char[] buff = new char[1024];

            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

        } catch (Exception e) {
            Log.e("@@@@@@@@", "" + e.getMessage());
            return resultList;

        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results

            JSONObject jsonObj = new JSONObject(jsonResults.toString());

            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results

            resultList = new ArrayList(predsJsonArray.length());
            placeIdList = new ArrayList(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                Log.e("@@@@@@@@", "Results" + predsJsonArray.getJSONObject(i).getString("description"));
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                placeIdList.add(predsJsonArray.getJSONObject(i).getString("place_id"));
            }
        } catch (JSONException e) {
            Log.e("@@@@@@", "Cannot process JSON results", e);
        }
        return resultList;
    }

    public ArrayList<String> getPlacesList() {

        return placeIdList;
    }
}
