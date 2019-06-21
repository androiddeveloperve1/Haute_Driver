package com.app.mylibertadriver.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.ActivityAcceptRestaurantBinding;
import com.app.mylibertadriver.interfaces.SwipeListener;
import com.app.mylibertadriver.interfaces.TaskLoadedCallback;
import com.app.mylibertadriver.model.orders.TaskModel;
import com.app.mylibertadriver.utils.FetchURL;
import com.app.mylibertadriver.utils.GoogleApiUtils;
import com.app.mylibertadriver.utils.SwipeView;
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
import com.google.gson.Gson;

public class AcceptRestaurantActivity extends GoogleServicesActivationActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private LatLng myCurrentLatLong = null;
    private GoogleMap mMap;
    private LatLng restaurantLatlong;
    private Polyline currentPolyline;
    private ActivityAcceptRestaurantBinding binder;
    private TaskModel restaurantDetails;

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

    public class MyClick {
        public void onNavifationStart(View v) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + myCurrentLatLong.latitude + "," + myCurrentLatLong.longitude + "+&daddr=" + restaurantLatlong.latitude + "," + restaurantLatlong.longitude));
            startActivity(intent);
        }

        public void onBack(View v) {
            finish();
        }

        public void onCall(View v) {
            GoogleApiUtils.requestCall(AcceptRestaurantActivity.this, ""+restaurantDetails.getOrderInfo().getRestaurantInfo().getContact_no());
        }
    }
}
