package com.app.mylibertadriver.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.databinding.ActivityAcceptRestaurantBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.interfaces.OnAddressListener;
import com.app.mylibertadriver.interfaces.SwipeListener;
import com.app.mylibertadriver.interfaces.TaskLoadedCallback;
import com.app.mylibertadriver.interfaces.TimerListener;
import com.app.mylibertadriver.model.orders.TaskModel;
import com.app.mylibertadriver.utils.AppUtils;
import com.app.mylibertadriver.utils.FetchURL;
import com.app.mylibertadriver.utils.GoogleApiUtils;
import com.app.mylibertadriver.utils.MyCountDownTimer;
import com.app.mylibertadriver.utils.SwipeView;
import com.google.android.gms.location.LocationResult;
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
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class AcceptRestaurantActivity extends GoogleServicesActivationActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private LatLng myCurrentLatLong = null;
    private GoogleMap mMap;
    private LatLng restaurantLatlong;
    private Polyline currentPolyline;
    private ActivityAcceptRestaurantBinding binder;
    private TaskModel restaurantDetails;
    private MarkerOptions delivarableLatLongMarker;
    private CountDownTimer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_accept_restaurant);
        disableButton();
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

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onServicesReady() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_track);
        mapFragment.getMapAsync(AcceptRestaurantActivity.this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onUpdatedLocation(LocationResult locationResult) {
        myCurrentLatLong = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
        stopLocationUpdate();
        MarkerOptions myCurrentLatLongMarker = new MarkerOptions().position(myCurrentLatLong).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(AppUtils.getLocatinIcon(AcceptRestaurantActivity.this)));
        delivarableLatLongMarker = new MarkerOptions().position(restaurantLatlong);
        mMap.clear();
        mMap.addMarker(myCurrentLatLongMarker);
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(myCurrentLatLong).include(restaurantLatlong);
        LatLngBounds bounds = boundsBuilder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.animateCamera(cameraUpdate);
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(restaurantLatlong, 8));
        new FetchURL(AcceptRestaurantActivity.this).execute(AppUtils.getUrlForDrawRoute(myCurrentLatLongMarker.getPosition(), delivarableLatLongMarker.getPosition(), "driving"));
        //WorkUtils.startBackgroundService();
        GoogleApiUtils.getAddressFromLatLong(restaurantLatlong, new OnAddressListener() {
            @Override
            public void onAddressFound(Object address) {
                delivarableLatLongMarker.title("Restaurant: " + restaurantDetails.getOrderInfo().getRestaurantInfo().getName()).snippet("Address: " + (String) address);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.addMarker(delivarableLatLongMarker).showInfoWindow();
                    }
                });
            }

            @Override
            public void onAddressError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.addMarker(delivarableLatLongMarker);
                    }
                });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

    }

    @Override
    public void onTaskDone(Object... values) {
        if (values[0] != null) {
            Log.e("@@@@@@@@@@", "" + values[3].toString());
            if (currentPolyline != null)
                currentPolyline.remove();
            currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
            restaurantDetails.getOrderInfo().setDistance(values[1].toString());
            restaurantDetails.getOrderInfo().setTravelTime(AppUtils.getDrivingTimeFromValue(values[2].toString()));
            if (Integer.parseInt(values[3].toString()) <= Constants.ENABLE_DISTANCE) {
                enableButton();
            } else {
                disableButton();
            }
        } else {
            disableButton();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    void enableButton() {
        binder.swipeView.enableSwipe();
        binder.disableView.setVisibility(View.GONE);
        binder.ivNavigation.setVisibility(View.VISIBLE);

    }


    void disableButton() {
        binder.swipeView.disableSwipe();
        binder.disableView.setVisibility(View.VISIBLE);
        binder.ivNavigation.setVisibility(View.VISIBLE);
    }

    void onRestaurantSelected() {
        //WorkUtils.stopBackgroundService();
        Intent intent = new Intent(AcceptRestaurantActivity.this, ReachedRestaurantActivty.class);
        intent.putExtra("order_id", restaurantDetails.getOrder_id());
        startActivity(intent);
        finish();
    }

    @Override
    public void onNoInternetFound() {
        ResponseDialog.showErrorDialog(this, Constants.NO_INTERNET_CONNECTION_FOUND_TAG);
    }

    public class MyClick {
        public void onNavifationStart(View v) {
            if (myCurrentLatLong != null) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + myCurrentLatLong.latitude + "," + myCurrentLatLong.longitude + "+&daddr=" + restaurantLatlong.latitude + "," + restaurantLatlong.longitude));
                startActivity(intent);
            } else {

                Toast.makeText(AcceptRestaurantActivity.this, "Searching please wait", Toast.LENGTH_SHORT).show();
            }

        }

        public void onBack(View v) {
            finish();
        }

        public void onCall(View v) {
            AppUtils.requestCall(AcceptRestaurantActivity.this, "" + restaurantDetails.getOrderInfo().getRestaurantInfo().getContact_no());
        }

        public void onHelp(View v) {
            startActivity(new Intent(AcceptRestaurantActivity.this, HelpActivity.class));

        }
    }


    void showTiming() {
        myTimer = new MyCountDownTimer.MyTimer().startNow(this, new TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                String s = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                binder.tvTime.setText(s);
            }

            @Override
            public void onFinish() {
                binder.tvTime.setText("Time Expired!");
            }

            @Override
            public void onExpire() {
                binder.tvTime.setText("Time Expired!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showTiming();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            myTimer.cancel();
        } catch (Exception e) {
        }
    }
}
