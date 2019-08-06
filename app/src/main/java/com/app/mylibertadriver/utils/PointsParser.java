package com.app.mylibertadriver.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.app.mylibertadriver.interfaces.TaskLoadedCallback;
import com.app.mylibertadriver.model.GooglePathDisatnceTimeModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointsParser extends AsyncTask<String, Integer, GooglePathDisatnceTimeModel> {

    TaskLoadedCallback taskCallback;

    public PointsParser(Context mContext) {
        this.taskCallback = (TaskLoadedCallback) mContext;
    }

    // Parsing the data in non-ui thread
    @Override
    protected GooglePathDisatnceTimeModel doInBackground(String... jsonData) {

        JSONObject jObject;
        GooglePathDisatnceTimeModel routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("mylog", jsonData[0].toString());
            DataParser parser = new DataParser();
            Log.d("mylog", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("mylog", "Executing routes");
            Log.d("mylog", routes.toString());

        } catch (Exception e) {
            Log.d("mylog", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(GooglePathDisatnceTimeModel result) {
        try {

            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            // Traversing through all the routes
            for (int i = 0; i < result.getRoutes().size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.getRoutes().get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLACK);
                Log.d("mylog", "onPostExecute lineoptions decoded");
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                //mMap.addPolyline(lineOptions);
                taskCallback.onTaskDone(lineOptions, result.getDistance(), result.getTime(), result.getDitanceLat());
            } else {
                Log.d("mylog", "without Polylines drawn");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}