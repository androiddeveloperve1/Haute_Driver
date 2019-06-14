package com.foodies.vedriver.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.foodies.vedriver.R;
import com.foodies.vedriver.adapter.GooglePlacesAutocompleteAdapter;
import com.foodies.vedriver.constants.PermissionConstants;
import com.foodies.vedriver.dialogs.ResponseDialog;
import com.foodies.vedriver.interfaces.OnAddressListener;
import com.foodies.vedriver.permission.PermissionHandlerListener;
import com.foodies.vedriver.permission.PermissionUtils;
import com.foodies.vedriver.utils.GoogleApiUtils;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class Maps2Activity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final int LocationTag = 10001;
    private GooglePlacesAutocompleteAdapter googlePlacesAutocompleteAdapter;
    private MarkerOptions mMarkerOptions;
    private GoogleMap mMap;
    private TextView toolbar_address;
    private String address = "";
    private AutoCompleteTextView enter_new_address;
    private LatLng selectedLocation;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            showLocationOnMap(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()));
            getFusedLocationProviderClient(Maps2Activity.this).removeLocationUpdates(locationCallback);
        }
    };
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationTag) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    startProgressNow();
                    break;
                case Activity.RESULT_CANCELED:
                    gpsDisableAlert();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionUtils.getInstance().checkAllPermission(Maps2Activity.this, PermissionConstants.permissionArrayForLocation, new PermissionHandlerListener() {
            @Override
            public void onGrant() {
                if (checkGooglePlayServiceAvailability(Maps2Activity.this)) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(Maps2Activity.this);
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps2);
        toolbar_address = findViewById(R.id.toolbar_address);
        enter_new_address = findViewById(R.id.enter_new_address);
        googlePlacesAutocompleteAdapter = new GooglePlacesAutocompleteAdapter(this, R.layout.item_places);
        enter_new_address.setAdapter(googlePlacesAutocompleteAdapter);

        enter_new_address.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                googlePlacesAutocompleteAdapter.getFilter().filter(s.toString());
            }
        });

        enter_new_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoogleApiUtils.getLatLongByPlace(googlePlacesAutocompleteAdapter.getPlacesList().get(position), new OnAddressListener() {
                    @Override
                    public void onAddressFound(final Object address) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showLocationOnMap((LatLng) address);
                            }
                        });

                    }

                    @Override
                    public void onAddressError(String error) {

                    }
                });

            }
        });


        findViewById(R.id.save_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinish();
            }
        });
        findViewById(R.id.iv_current_location).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                enter_new_address.setText("");
                checkResolutionAndProceed();
            }
        });
    }

    void showLocationOnMap(final LatLng loc) {
        selectedLocation = loc;
        Log.e("@@@@@@", "location updated" + loc.latitude + ":" + loc.longitude);
        mMap.clear();
        mMap.addMarker(mMarkerOptions.position(loc).draggable(true).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        hitApiToGetAddress();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        checkResolutionAndProceed();

    }

    @Override
    public void onConnectionSuspended(int i) {
        ResponseDialog.showErrorDialog(this, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ResponseDialog.showErrorDialog(this, connectionResult.getErrorMessage());
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMarkerOptions = new MarkerOptions();
        mMap = googleMap;
        buildGoogleApiClient();
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.e("@@@@@Dragged end", "" + marker.getPosition().latitude + "-" + marker.getPosition().longitude);
                selectedLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                hitApiToGetAddress();
            }
        });


    }

    void doFinish() {
        if (selectedLocation != null) {
            Intent mIntent = new Intent();
            mIntent.putExtra("lat", selectedLocation.latitude);
            mIntent.putExtra("long", selectedLocation.longitude);
            mIntent.putExtra("address", address);
            setResult(Activity.RESULT_OK, mIntent);
        }
        finish();
    }

    void hitApiToGetAddress() {
        Log.e("@@@@@@@", "api" + new Gson().toJson(selectedLocation));
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                GoogleApiUtils.getAddressFromLatLong(selectedLocation, new OnAddressListener() {
                    @Override
                    public void onAddressFound(Object addressFound) {
                        address = (String) addressFound;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                enter_new_address.setText(address);
                            }
                        });

                    }

                    @Override
                    public void onAddressError(String error) {

                    }
                });
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopUpdateLocation();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        stopUpdateLocation();
        doFinish();
    }

    void stopUpdateLocation() {
        getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
    }


    void gpsDisableAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("You have to turn on locatin service to track order");
        builder.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                checkResolutionAndProceed();
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.show();
    }

    private void checkResolutionAndProceed() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResult(LocationSettingsResult result) {
                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startProgressNow();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(Maps2Activity.this, LocationTag);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startProgressNow() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
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
}
