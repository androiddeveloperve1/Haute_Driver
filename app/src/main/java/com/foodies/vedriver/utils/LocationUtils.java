package com.foodies.vedriver.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.foodies.vedriver.activities.LocationListenerActivity;
import com.foodies.vedriver.activities.MapsActivity;
import com.foodies.vedriver.activities.SignupActivity;
import com.foodies.vedriver.constants.PermissionConstants;
import com.foodies.vedriver.interfaces.LocationDetector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by Rahul on 13/12/17.
 */

public class LocationUtils implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final int LocationTag = 10001;
    private final String Wifi_Action = "android.net.conn.CONNECTIVITY_CHANGE";
    private MapsActivity activity;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationDetector detector;
    /*
     * Location will update here
     * */
    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            activity.stopProgress();
            detector.OnLocationChange(location);
        }
    };
    /*Network enable disable event tracker*/
    private BroadcastReceiver recieverWifi = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Wifi_Action)) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    activity.listenLocation(detector);
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, listener);
                } else {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, listener);
                    detector.onErrors(PermissionConstants.NO_NETWORK);
                }
            }
        }
    };

    public LocationUtils(MapsActivity activity, LocationDetector detector) {
        this.activity = activity;
        this.detector = detector;
        buildGoogleApiClient();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest().create();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, listener);
        checkResolutionAndProceed();
    }

    @Override
    public void onConnectionSuspended(int i) {
        detector.onErrors(PermissionConstants.GOOGLE_PLAY_CONNECTION_SUSPEND);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        detector.onErrors(PermissionConstants.GOOGLE_PLAY_CONNECTION_ERROR);
    }

    /*
     * Request for Connecting Google Client
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    /*
     * To check gps is Enable or not
     * */
    private void checkResolutionAndProceed() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startProgressNow();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(activity, LocationTag);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }

    /*
     * start location listener
     * */
    public void startProgressNow() {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        activity.stopProgress();
        if (mLastLocation != null) {
            detector.OnLastLocationFound(mLastLocation);
        } else {
            detector.onErrors(PermissionConstants.NO_LAST_LOCATION_FOUND);
        }
        registerWifiListener();
    }

    /*
     * Stop all Service as location and networks
     *
     * */
    public void onStop() {
        unRigisterWifiListener();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, listener);
            mGoogleApiClient.disconnect();
        }
    }

    /*
     * Rigister Network listener
     * */
    private void registerWifiListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Wifi_Action);
        activity.registerReceiver(recieverWifi, filter);
    }

    /*
     *
     * Unregister Network Listener
     *
     * */

    private void unRigisterWifiListener() {
        try {
            activity.unregisterReceiver(recieverWifi);
        } catch (Exception e) {
        }
    }


    boolean chekInternetAndPlayservice() {
        if (checkGooglePlayServiceAvailability(activity)) {
            return true;
        } else {
            activity.stopProgress();
            detector.onErrors(PermissionConstants.PLAYSERICE_ERROR);
        }
        return false;
    }


    public boolean checkGooglePlayServiceAvailability(Context context) {
        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if ((statusCode == ConnectionResult.SUCCESS)) {
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, activity, 10, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    detector.onErrors(PermissionConstants.PLAYSERICE_ERROR);
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return false;
        }
    }

}