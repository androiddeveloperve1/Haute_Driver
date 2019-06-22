package com.app.mylibertadriver.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.constants.PermissionConstants;
import com.app.mylibertadriver.permission.PermissionHandlerListener;
import com.app.mylibertadriver.permission.PermissionUtils;
import com.app.mylibertadriver.utils.AppUtils;
import com.app.mylibertadriver.interfaces.CommanTaskListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

import java.util.ArrayList;

public abstract class GoogleServiceActivationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, CommanTaskListener, GoogleApiClient.OnConnectionFailedListener {

    public static final int LocationTag = 14001;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkResolutionAndProceed();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppUtils.isNetworkConnected(getActivity())) {
            PermissionUtils.getInstance().checkAllPermission(getActivity(), PermissionConstants.permissionArrayForLocation, new PermissionHandlerListener() {
                @Override
                public void onGrant() {
                    if (checkGooglePlayServiceAvailability(getActivity())) {
                        buildGoogleApiClient();
                    }
                }

                @Override
                public void onReject(ArrayList<String> remainsPermissonList) {

                }

                @Override
                public void onRationalPermission(ArrayList<String> rationalPermissonList) {
                    super.onRationalPermission(rationalPermissonList);
                }
            });
        } else {
            onNoInternetFound();
        }

    }

    public boolean checkGooglePlayServiceAvailability(Context context) {
        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if ((statusCode == ConnectionResult.SUCCESS)) {
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, getActivity(), 10, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    getActivity().finish();
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return false;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    private void checkResolutionAndProceed() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setSmallestDisplacement(Constants.LOCATION_DISTANCE_IN_METER);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResult(LocationSettingsResult result) {
                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        requestLocatipnUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), LocationTag);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestLocatipnUpdate() {
        onServicesReady();
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(mLocationCallback);
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());


    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            onUpdatedLocation(locationResult);
        }
    };

    public void stopLocationUpdate() {
        LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(mLocationCallback);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.getInstance().hendlePermissionForFragment(getActivity(), requestCode, permissions, grantResults);
    }
}
