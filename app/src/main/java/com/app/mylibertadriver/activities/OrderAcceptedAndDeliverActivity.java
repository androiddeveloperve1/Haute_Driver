package com.app.mylibertadriver.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.ActivityOrderAcceptedAndDeliverBinding;
import com.app.mylibertadriver.dialogs.SwipeViewDialog;
import com.app.mylibertadriver.interfaces.SwipeListener;
import com.app.mylibertadriver.interfaces.TaskLoadedCallback;
import com.app.mylibertadriver.model.orders.OrderDetailsModel;
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

public class OrderAcceptedAndDeliverActivity extends GoogleServicesActivationActivity implements OnMapReadyCallback, TaskLoadedCallback {

    ActivityOrderAcceptedAndDeliverBinding binder;
    private GoogleMap mMap;
    private LatLng delivarableLatLongUser;
    private LatLng myCurrentLatLong = null;
    private Polyline currentPolyline;
    private OrderDetailsModel orderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_order_accepted_and_deliver);
        binder.setClick(new MyClick());

        orderDetails = new Gson().fromJson(getIntent().getStringExtra("data"), OrderDetailsModel.class);
        delivarableLatLongUser = new LatLng(orderDetails.getUser_id().getDelivery_address().getLoc().getCoordinates().get(0), orderDetails.getUser_id().getDelivery_address().getLoc().getCoordinates().get(1
        ));

        binder.setData(orderDetails);
        binder.swipeView.setEventListener(new SwipeListener() {
            @Override
            public void swipeStarted() {
            }

            @Override
            public void Swiped(int flag) {
                if (flag == SwipeView.SWIPED_RIGHT) {
                    startSwipeDialog();
                }
            }
        });
        binder.swipeView.setText("REACHED DELIVERY POINT");


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onServicesReady() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(OrderAcceptedAndDeliverActivity.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                myCurrentLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_track);
                mapFragment.getMapAsync(OrderAcceptedAndDeliverActivity.this);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            orderDetails.setDistance(GoogleApiUtils.getDistanceBitweenLatlongInKM(
                    new LatLng(myCurrentLatLong.latitude, myCurrentLatLong.longitude),
                    new LatLng(orderDetails.getRestaurant_id().getLocation().getCoordinates().get(0), orderDetails.getRestaurant_id().getLocation().getCoordinates().get(1))
            ) + " Km.");
        } catch (Exception e) {

        }
        MarkerOptions myCurrentLatLongMarker = new MarkerOptions().position(myCurrentLatLong).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(GoogleApiUtils.getLocatinIcon(OrderAcceptedAndDeliverActivity.this)));
        MarkerOptions delivarableLatLongMarker = new MarkerOptions().position(delivarableLatLongUser);
        mMap = googleMap;
        mMap.clear();
        mMap.addMarker(myCurrentLatLongMarker);
        mMap.addMarker(delivarableLatLongMarker);

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(delivarableLatLongUser).include(myCurrentLatLong);
        LatLngBounds bounds = boundsBuilder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.animateCamera(cameraUpdate);


        new FetchURL(OrderAcceptedAndDeliverActivity.this).execute(GoogleApiUtils.getUrlForDrawRoute(myCurrentLatLongMarker.getPosition(), delivarableLatLongMarker.getPosition(), "driving"));
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    void startSwipeDialog() {
        SwipeViewDialog d = new SwipeViewDialog(OrderAcceptedAndDeliverActivity.this, new SwipeListener() {
            @Override
            public void swipeStarted() {

            }

            @Override
            public void Swiped(int flag) {
                finish();
            }
        });
        d.show();

    }

    public class MyClick {
        public void onBack(View v) {
            finish();
        }

        public void onCall(View v) {
            GoogleApiUtils.requestCall(OrderAcceptedAndDeliverActivity.this, orderDetails.getRestaurant_id().getContact_no());
        }

        public void onNavifationStart(View v) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + myCurrentLatLong.latitude + "," + myCurrentLatLong.longitude + "+&daddr=" + delivarableLatLongUser.latitude + "," + delivarableLatLongUser.longitude));
            startActivity(intent);
        }

    }
}
