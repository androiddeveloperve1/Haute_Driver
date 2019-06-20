package com.app.mylibertadriver.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.work.WorkManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.constants.PermissionConstants;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.interfaces.SwipeListener;
import com.app.mylibertadriver.interfaces.TaskLoadedCallback;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.permission.PermissionHandlerListener;
import com.app.mylibertadriver.permission.PermissionUtils;
import com.app.mylibertadriver.utils.FetchURL;
import com.app.mylibertadriver.utils.SwipeView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class AcceptOrderActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, TaskLoadedCallback {
    public static final int LocationTag = 10001;
    @Inject
    APIInterface apiInterface;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Polyline currentPolyline;

    private LatLng myCurrentLatLong = null;
    private Bitmap myCurrentLatLongIcon;
    private LatLng delivarableLatLong = new LatLng(27.176670, 78.008072);

    private SwipeView swipe_view;
    private ImageView iv_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_order);
        swipe_view = findViewById(R.id.swipe_view);
        iv_navigation = findViewById(R.id.iv_navigation);
        swipe_view.setEventListener(new SwipeListener() {
            @Override
            public void swipeStarted() {
            }

            @Override
            public void Swiped(int flag) {
                if (flag == SwipeView.SWIPED_RIGHT) {
                    hitApiToAcceptTask();


                }
            }
        });
        swipe_view.setText("ACCEPT");
        iv_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + myCurrentLatLong.latitude + "," + myCurrentLatLong.longitude + "+&daddr=" + delivarableLatLong.latitude + "," + delivarableLatLong.longitude));
                startActivity(intent);
            }
        });
        BitmapDrawable b = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_current_location);
        myCurrentLatLongIcon = Bitmap.createScaledBitmap(b.getBitmap(), (int) getResources().getDimension(R.dimen._20_px), (int) getResources().getDimension(R.dimen._20_px), false);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions myCurrentLatLongMarker = new MarkerOptions().position(myCurrentLatLong).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(myCurrentLatLongIcon));
        MarkerOptions delivarableLatLongMarker = new MarkerOptions().position(delivarableLatLong);
        mMap = googleMap;
        mMap.clear();
        mMap.addMarker(myCurrentLatLongMarker);
        mMap.addMarker(delivarableLatLongMarker);
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(myCurrentLatLong).include(delivarableLatLong);
        LatLngBounds bounds = boundsBuilder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.animateCamera(cameraUpdate);
        new FetchURL(AcceptOrderActivity.this).execute(getUrl(myCurrentLatLongMarker.getPosition(), delivarableLatLongMarker.getPosition(), "driving"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionUtils.getInstance().checkAllPermission(AcceptOrderActivity.this, PermissionConstants.permissionArrayForLocation, new PermissionHandlerListener() {
            @Override
            public void onGrant() {
                if (checkGooglePlayServiceAvailability(AcceptOrderActivity.this)) {
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
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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
        ResponseDialog.showErrorDialog(this, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ResponseDialog.showErrorDialog(this, connectionResult.getErrorMessage());
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

    private void checkResolutionAndProceed() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
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
                            status.startResolutionForResult(AcceptOrderActivity.this, LocationTag);
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
                    startProgressNow();
                    break;
                case Activity.RESULT_CANCELED:
                    gpsDisableAlert();
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startProgressNow() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(AcceptOrderActivity.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                myCurrentLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_track);
                mapFragment.getMapAsync(AcceptOrderActivity.this);
            }
        });
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

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdate();
    }


    void stopLocationUpdate() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + Constants.API_KEY;
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    private void hitApiToAcceptTask() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(AcceptOrderActivity.this);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.acceptOrder("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(AcceptOrderActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel response) {
                        progressDialog.dismiss();
                        Log.e("@@@@@@@@@@@Success", new Gson().toJson(response));
                        if (response.getStatus().equals("200")) {
                            swipe_view.setText("ACCEPTED");
                            swipe_view.disableSwipe();
                        } else {
                            ResponseDialog.showErrorDialog(AcceptOrderActivity.this, response.getMessage());
                        }
                    }
                });
    }

}
