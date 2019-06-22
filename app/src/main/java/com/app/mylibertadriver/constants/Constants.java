package com.app.mylibertadriver.constants;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class Constants {

    public static final String BACKGROUND_WORKER_REQUEST = "REQUEST_DRIVER_LOCATION_UPDATE";
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String TYPE_DETAILS = "/details";
    public static final String OUT_JSON = "/json";
    public static final String ANDROID_KEY = "android";
    public static final String API_KEY = "AIzaSyAq8L4mpVsRU0XYfdm4njQJ_Ntpi-KOvyc";
    public static int FROM_FORGOT_PASS = 1;
    public static int FROM_SPLASH = 2;
    public static int FROM_SIGNUP = 0;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public static final long LOCATION_DISTANCE_IN_METER = 20;

    public static final String NO_INTERNET_CONNECTION_FOUND_TAG="No address associated with hostname";
    public static final String NO_INTERNET_CONNECTION_FOUND_MESSAGE="Please make sure, that you are connected to internet.";

}
