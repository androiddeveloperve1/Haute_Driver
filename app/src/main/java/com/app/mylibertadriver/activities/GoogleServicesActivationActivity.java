package com.app.mylibertadriver.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.constants.PermissionConstants;
import com.app.mylibertadriver.geofencing.GeofenceBroadcastReceiver;
import com.app.mylibertadriver.interfaces.CommanTaskListener;
import com.app.mylibertadriver.permission.PermissionHandlerListener;
import com.app.mylibertadriver.permission.PermissionUtils;
import com.app.mylibertadriver.prefes.MySharedPreference;
import com.app.mylibertadriver.utils.AppUtils;
import com.app.mylibertadriver.interfaces.GoogleConnectionCallBackAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public abstract class GoogleServicesActivationActivity extends FragmentActivity implements CommanTaskListener, GoogleConnectionCallBackAdapter {
    public static final int LocationTag = 13001;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ArrayList<Geofence> mGeofenceList;
    protected static boolean isRationalPermissionDEtect = false;
    protected boolean isSelectNoThanks = false;


    @Override
    protected void onResume() {
        super.onResume();
        if (!isRationalPermissionDEtect && !isSelectNoThanks) {
            if (AppUtils.isNetworkConnected(GoogleServicesActivationActivity.this)) {
                PermissionUtils.getInstance().checkAllPermission(GoogleServicesActivationActivity.this, PermissionConstants.permissionArrayForLocation, new PermissionHandlerListener() {
                    @Override
                    public void onGrant() {
                        isRationalPermissionDEtect = false;
                        if (checkGooglePlayServiceAvailability(GoogleServicesActivationActivity.this)) {
                            buildGoogleApiClient();
                        }
                    }

                    @Override
                    public void onReject(ArrayList<String> remainsPermissonList) {
                        isRationalPermissionDEtect = false;
                    }

                    @Override
                    public void onRationalPermission(ArrayList<String> rationalPermissonList) {
                        isRationalPermissionDEtect = true;
                        firePerimisionActivity(GoogleServicesActivationActivity.this);
                    }
                });
            } else {
                onNoInternetFound();
            }
        }


    }


    public boolean checkGooglePlayServiceAvailability(Context context) {
        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if ((statusCode == ConnectionResult.SUCCESS)) {
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, this, 10, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return false;
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(GoogleServicesActivationActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }


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

    private void checkResolutionAndProceed() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setSmallestDisplacement(Constants.LOCATION_DISTANCE_IN_METER);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
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
                            status.startResolutionForResult(GoogleServicesActivationActivity.this, LocationTag);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationTag) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    isSelectNoThanks = false;
                    break;
                case Activity.RESULT_CANCELED:
                    isSelectNoThanks = true;
                    gpsDisableAlert();
                    break;
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestLocatipnUpdate() {
        onServicesReady();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(GoogleServicesActivationActivity.this).removeLocationUpdates(mLocationCallback);
        LocationServices.getFusedLocationProviderClient(GoogleServicesActivationActivity.this).requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());


    }

    private void gpsDisableAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS Disable!");
        builder.setMessage("Please turn your device's GPS functionality, otherwise application functionality will not work.");
        builder.setPositiveButton("Turn GPS On", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isSelectNoThanks = false;
                dialogInterface.dismiss();
                checkResolutionAndProceed();
            }
        });
        builder.setNegativeButton("EXIT FROM APP.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isSelectNoThanks = false;
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void stopLocationUpdate() {
        LocationServices.getFusedLocationProviderClient(GoogleServicesActivationActivity.this).removeLocationUpdates(mLocationCallback);

    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            onUpdatedLocation(locationResult);
        }
    };



    private void firePerimisionActivity(final Activity mActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Permission Denied");
        builder.setMessage(getResources().getString(R.string.permission_require));
        builder.setPositiveButton("SETTING", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                isRationalPermissionDEtect = false;
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                mActivity.startActivity(intent);

            }
        });
        builder.setNegativeButton("EXIT FROM APP.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isRationalPermissionDEtect = false;
                dialogInterface.dismiss();
                mActivity.finishAffinity();
            }
        });

        builder.show();


    }
}
