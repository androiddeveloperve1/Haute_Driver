package com.app.mylibertadriver.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.databinding.ActivityAcceptRestaurantBinding;
import com.app.mylibertadriver.interfaces.SwipeListener;
import com.app.mylibertadriver.interfaces.TaskLoadedCallback;
import com.app.mylibertadriver.model.orders.TaskModel;
import com.app.mylibertadriver.utils.FetchURL;
import com.app.mylibertadriver.utils.GoogleApiUtils;
import com.app.mylibertadriver.utils.SwipeView;
import com.app.mylibertadriver.worker.DriverLocationUpdateService;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AcceptRestaurantActivity extends GoogleServicesActivationActivity implements OnMapReadyCallback, TaskLoadedCallback {
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.e("@@@@@@", "Map updating");
        }
    };
    private LatLng myCurrentLatLong = null;
    private GoogleMap mMap;
    private LatLng restaurantLatlong;
    private Polyline currentPolyline;
    private ActivityAcceptRestaurantBinding binder;
    private TaskModel restaurantDetails;
    private OneTimeWorkRequest.Builder driverLocationRequest;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_accept_restaurant);
        restaurantDetails = new Gson().fromJson(getIntent().getStringExtra("data"), TaskModel.class);
        restaurantLatlong = new LatLng(restaurantDetails.getOrderInfo().getRestaurantInfo().getLocation().getCoordinates().get(0), restaurantDetails.getOrderInfo().getRestaurantInfo().getLocation().getCoordinates().get(1));
        binder.setData(restaurantDetails);
        binder.setClick(new MyClick());
        binder.swipeView.setEventListener(new SwipeListener() {
            @Override
            public void swipeStarted() {
            }

            @Override
            public void Swiped(int flag) {
                if (flag == SwipeView.SWIPED_RIGHT) {
                    onRestaurantSelected();
                }
            }
        });
        binder.swipeView.setText("REACHED RESTAURANT");
        enableButton();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onServicesReady() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(100);

        LocationServices.getFusedLocationProviderClient(AcceptRestaurantActivity.this).removeLocationUpdates(mLocationCallback);
        LocationServices.getFusedLocationProviderClient(AcceptRestaurantActivity.this).requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        LocationServices.getFusedLocationProviderClient(AcceptRestaurantActivity.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                myCurrentLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_track);
                mapFragment.getMapAsync(AcceptRestaurantActivity.this);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions myCurrentLatLongMarker = new MarkerOptions().position(myCurrentLatLong).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(GoogleApiUtils.getLocatinIcon(AcceptRestaurantActivity.this)));
        MarkerOptions delivarableLatLongMarker = new MarkerOptions().position(restaurantLatlong);
        mMap = googleMap;
        mMap.clear();
        mMap.addMarker(myCurrentLatLongMarker);
        mMap.addMarker(delivarableLatLongMarker);

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(myCurrentLatLong).include(restaurantLatlong);
        LatLngBounds bounds = boundsBuilder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.animateCamera(cameraUpdate);

        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(restaurantLatlong, 8));
        new FetchURL(AcceptRestaurantActivity.this).execute(GoogleApiUtils.getUrlForDrawRoute(myCurrentLatLongMarker.getPosition(), delivarableLatLongMarker.getPosition(), "driving"));
        listentoBackground();

    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    void enableButton() {
        binder.swipeView.enableSwipe();
        binder.disableView.setVisibility(View.GONE);
        binder.ivNavigation.setVisibility(View.GONE);
    }


    void disableButton() {
        binder.swipeView.disableSwipe();
        binder.disableView.setVisibility(View.VISIBLE);
        binder.ivNavigation.setVisibility(View.VISIBLE);
    }

    void onRestaurantSelected() {
        Intent intent = new Intent(AcceptRestaurantActivity.this, ReachedRestaurantActivty.class);
        intent.putExtra("order_id", restaurantDetails.getOrder_id());
        startActivity(intent);
        finish();
    }

    void listentoBackground() {
        try {
            ListenableFuture<List<WorkInfo>> work = WorkManager.getInstance().getWorkInfosByTag(Constants.BACKGROUND_WORKER_REQUEST);
            List<WorkInfo> work2 = work.get();
            if (work2.size() > 0) {
                Log.e("@@@@@@@", "Already Request");
                if (work2.get(0).getState().isFinished()) {
                    Log.e("@@@@@@@", "Again Back Request");
                    OneTimeWorkRequest req = driverLocationRequest.build();
                    WorkManager.getInstance().enqueue(req);
                }
            } else {
                buildWorkManager();
                OneTimeWorkRequest req = driverLocationRequest.build();
                WorkManager.getInstance().enqueue(req);
                WorkManager.getInstance().getWorkInfoByIdLiveData(req.getId()).observe(AcceptRestaurantActivity.this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Log.e("@@@@@@@", "Work status" + workInfo.getState().name());
                    }
                });
            }
        } catch (Exception e) {

        }

    }

    void buildWorkManager() {
        Log.e("@@@@@@@", "New Back Request");
        driverLocationRequest = new OneTimeWorkRequest.Builder(DriverLocationUpdateService.class);
        driverLocationRequest.addTag(Constants.BACKGROUND_WORKER_REQUEST);
        driverLocationRequest.setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.SECONDS);
        driverLocationRequest.setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build());
    }

    public class MyClick {
        public void onNavifationStart(View v) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + myCurrentLatLong.latitude + "," + myCurrentLatLong.longitude + "+&daddr=" + restaurantLatlong.latitude + "," + restaurantLatlong.longitude));
            startActivity(intent);
        }

        public void onBack(View v) {
            finish();
        }

        public void onCall(View v) {
            GoogleApiUtils.requestCall(AcceptRestaurantActivity.this, "" + restaurantDetails.getOrderInfo().getRestaurantInfo().getContact_no());
        }
    }
}
