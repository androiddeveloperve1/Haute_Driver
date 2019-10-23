package com.app.mylibertadriver.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.databinding.ActivityAcceptOrderBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.interfaces.OnAddressListener;
import com.app.mylibertadriver.interfaces.SwipeListener;
import com.app.mylibertadriver.interfaces.TaskLoadedCallback;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.orders.RestaurantInfoModel;
import com.app.mylibertadriver.model.orders.TaskModel;
import com.app.mylibertadriver.model.orders.TaskResponse;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.utils.AppUtils;
import com.app.mylibertadriver.utils.FetchURL;
import com.app.mylibertadriver.utils.GoogleApiUtils;
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

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class AcceptOrderActivity extends GoogleServicesActivationActivity implements OnMapReadyCallback, TaskLoadedCallback {
    @Inject
    APIInterface apiInterface;
    private GoogleMap mMap;
    private Polyline currentPolyline;
    private LatLng myCurrentLatLong = null;
    private LatLng delivarableLatLong = null;
    private TaskResponse orderData;
    private ActivityAcceptOrderBinding binder;
    private RestaurantInfoModel restaurantInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_accept_order);
        orderData = new Gson().fromJson(getIntent().getStringExtra("data"), TaskResponse.class);

        restaurantInfoModel = orderData.getOrderInfo().getRestaurantInfo();




        delivarableLatLong = new LatLng(orderData.getOrderInfo().getRestaurantInfo().getLocation().getCoordinates().get(0), restaurantInfoModel.getLocation().getCoordinates().get(1));
        Log.e("@@@@", new Gson().toJson(delivarableLatLong));
        binder.setData(orderData);
        binder.setClick(new MyClick());
        binder.swipeView.setEventListener(new SwipeListener() {
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
        binder.swipeView.setText("ACCEPT");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
    }


    @Override
    public void onTaskDone(Object... values) {

        if (values[0] != null) {
            if (currentPolyline != null)
                currentPolyline.remove();
            currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
            orderData.getOrderInfo().setDistance(values[1].toString());
            Log.e("@@@@@@@@", "Task Done" + values[2].toString());
            orderData.getOrderInfo().setTravelTime(AppUtils.getDrivingTimeFromValue(values[2].toString()));

        }else {
            Toast.makeText(this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void hitApiToAcceptTask() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(AcceptOrderActivity.this);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.acceptOrder(orderData.getOrder_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        binder.swipeView.swipeLeft();
                        ResponseDialog.showErrorDialog(AcceptOrderActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel response) {
                        progressDialog.dismiss();
                        Toast.makeText(AcceptOrderActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.getStatus().equals("200")) {
                            finish();
                        } else {
                            binder.swipeView.swipeLeft();
                            Toast.makeText(AcceptOrderActivity.this, "" + response.getMessage(), Toast.LENGTH_LONG).show();
                            //ResponseDialog.showErrorDialog(AcceptOrderActivity.this, response.getMessage());
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onServicesReady() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_track);
        mapFragment.getMapAsync(AcceptOrderActivity.this);


    }

    @Override
    public void onUpdatedLocation(LocationResult locationResult) {
        myCurrentLatLong = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
        mMap.clear();
        stopLocationUpdate();
        MarkerOptions myCurrentLatLongMarker = new MarkerOptions().position(myCurrentLatLong).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(AppUtils.getLocatinIcon(AcceptOrderActivity.this)));
        final MarkerOptions delivarableLatLongMarker = new MarkerOptions().position(delivarableLatLong);

        mMap.clear();
        mMap.addMarker(myCurrentLatLongMarker);

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(myCurrentLatLong).include(delivarableLatLong);
        LatLngBounds bounds = boundsBuilder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 60);
        mMap.animateCamera(cameraUpdate);
        new FetchURL(AcceptOrderActivity.this).execute(AppUtils.getUrlForDrawRoute(myCurrentLatLongMarker.getPosition(), delivarableLatLongMarker.getPosition(), "driving"));

        GoogleApiUtils.getAddressFromLatLong(new LatLng(restaurantInfoModel.getLocation().getCoordinates().get(0), restaurantInfoModel.getLocation().getCoordinates().get(1)), new OnAddressListener() {
            @Override
            public void onAddressFound(final Object address) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        delivarableLatLongMarker.title("Restaurant: " + restaurantInfoModel.getName()).snippet("Address: " + (String) address);
                        mMap.addMarker(delivarableLatLongMarker);
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
    public void onNoInternetFound() {
        ResponseDialog.showErrorDialog(this, Constants.NO_INTERNET_CONNECTION_FOUND_TAG);
    }

    public class MyClick {

        public void onBack(View v) {
            finish();
        }

        public void onCall(View v) {
            AppUtils.requestCall(AcceptOrderActivity.this, orderData.getOrderInfo().getRestaurantInfo().getContact_no());
        }


        public void onHelp(View v) {
            startActivity(new Intent(AcceptOrderActivity.this, HelpActivity.class));

        }
    }
}

