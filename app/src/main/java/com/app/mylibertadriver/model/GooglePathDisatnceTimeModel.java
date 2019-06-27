package com.app.mylibertadriver.model;

import java.util.HashMap;
import java.util.List;

public class GooglePathDisatnceTimeModel {

    private List<List<HashMap<String, String>>> routes;
    private String distance;
    private String time;
    private int ditanceLat;

    public int getDitanceLat() {
        return ditanceLat;
    }

    public void setDitanceLat(int ditanceLat) {
        this.ditanceLat = ditanceLat;
    }

    public List<List<HashMap<String, String>>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<HashMap<String, String>>> routes) {
        this.routes = routes;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
