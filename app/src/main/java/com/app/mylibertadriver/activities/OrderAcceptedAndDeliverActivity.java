package com.app.mylibertadriver.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.databinding.ActivityOrderAcceptedAndDeliverBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.dialogs.SwipeViewDialog;
import com.app.mylibertadriver.interfaces.OnAddressListener;
import com.app.mylibertadriver.interfaces.SwipeListener;
import com.app.mylibertadriver.interfaces.TaskLoadedCallback;
import com.app.mylibertadriver.interfaces.TimerListener;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.orders.OrderDetailsModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.services.TimerService;
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

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class OrderAcceptedAndDeliverActivity extends GoogleServicesActivationActivity implements OnMapReadyCallback, TaskLoadedCallback {
    @Inject
    APIInterface apiInterface;
    private CountDownTimer myTimer;
    ActivityOrderAcceptedAndDeliverBinding binder;
    private GoogleMap mMap;
    private LatLng delivarableLatLongUser;
    private LatLng myCurrentLatLong = null;
    private Polyline currentPolyline;
    private OrderDetailsModel orderDetails;
    private SwipeViewDialog orderDeliveredDialog;

    SwipeListener orderDeliver = new SwipeListener() {
        @Override
        public void swipeStarted() {

        }


        @Override
        public void onCrossButton() {
            binder.swipeView.swipeLeft();

        }

        @Override
        public void Swiped(int flag) {
            if (flag == SwipeView.SWIPED_RIGHT) {
                orderDeliveredDialog.dismiss();
                orderDelivered();

                onResume();
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_order_accepted_and_deliver);
        binder.setClick(new MyClick());
        disableButton();
        orderDetails = new Gson().fromJson(getIntent().getStringExtra("data"), OrderDetailsModel.class);
        delivarableLatLongUser = new LatLng(orderDetails.getUser_id().getDelivery_address().get(0).getLoc().getCoordinates().get(0), orderDetails.getUser_id().getDelivery_address().get(0).getLoc().getCoordinates().get(1
        ));


        binder.setData(orderDetails);
        binder.swipeView.setEventListener(new SwipeListener() {
            @Override
            public void swipeStarted() {
            }

            @Override
            public void Swiped(int flag) {
                if (flag == SwipeView.SWIPED_RIGHT) {
                    //WorkUtils.stopBackgroundService();
                    startSwipeDialog();
                }
            }
        });
        binder.swipeView.setText("REACHED DELIVERY POINT");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onServicesReady() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_track);
        mapFragment.getMapAsync(OrderAcceptedAndDeliverActivity.this);
    }

    @Override
    public void onUpdatedLocation(LocationResult locationResult) {
        myCurrentLatLong = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
        mMap.clear();
        stopLocationUpdate();

        MarkerOptions myCurrentLatLongMarker = new MarkerOptions().position(myCurrentLatLong).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(AppUtils.getLocatinIcon(OrderAcceptedAndDeliverActivity.this)));
        final MarkerOptions delivarableLatLongMarker = new MarkerOptions().position(delivarableLatLongUser);
        mMap.addMarker(myCurrentLatLongMarker);
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(delivarableLatLongUser).include(myCurrentLatLong);
        LatLngBounds bounds = boundsBuilder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.animateCamera(cameraUpdate);
        new FetchURL(OrderAcceptedAndDeliverActivity.this).execute(AppUtils.getUrlForDrawRoute(myCurrentLatLongMarker.getPosition(), delivarableLatLongMarker.getPosition(), "driving"));
        //WorkUtils.startBackgroundService();


        GoogleApiUtils.getAddressFromLatLong(delivarableLatLongUser, new OnAddressListener() {
            @Override
            public void onAddressFound(final Object address) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        delivarableLatLongMarker.title("User: " + orderDetails.getUser_id().getName()).snippet("Address: " + (String) address);
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
            orderDetails.setDistance(values[1].toString());
            orderDetails.setTravelTime(AppUtils.getDrivingTimeFromValue(values[2].toString()));

            if (Integer.parseInt(values[3].toString()) <= Constants.ENABLE_DISTANCE) {
                enableButton();
            } else {
                disableButton();

            }
        } else {
            disableButton();
        }

    }

    void startSwipeDialog() {
        orderDeliveredDialog = new SwipeViewDialog(this, orderDetails, orderDeliver);
        orderDeliveredDialog.show();

    }


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

    @Override
    public void onNoInternetFound() {
        ResponseDialog.showErrorDialog(this, Constants.NO_INTERNET_CONNECTION_FOUND_TAG);
    }

    private void orderDelivered() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(OrderAcceptedAndDeliverActivity.this);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.deliverOrderNow(orderDetails.get_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(OrderAcceptedAndDeliverActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel response) {
                        progressDialog.dismiss();
                        Toast.makeText(OrderAcceptedAndDeliverActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.getStatus().equals("200")) {
                            try {
                                Intent stop = new Intent(OrderAcceptedAndDeliverActivity.this, TimerService.class);
                                stop.putExtra("shouldStop", true);
                                ContextCompat.startForegroundService(OrderAcceptedAndDeliverActivity.this, stop);
                            } catch (Exception e) {
                            }
                            finish();
                        } else {
                            ResponseDialog.showErrorDialog(OrderAcceptedAndDeliverActivity.this, response.getMessage());
                        }
                    }
                });
    }

    public class MyClick {
        public void onBack(View v) {
            finish();
        }


        public void refresh(View v) {
            OrderAcceptedAndDeliverActivity.this.onResume();
        }

        public void onCall(View v) {
            AppUtils.requestCall(OrderAcceptedAndDeliverActivity.this, orderDetails.getUser_id().getMobile_no());
        }

        public void onNavifationStart(View v) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + myCurrentLatLong.latitude + "," + myCurrentLatLong.longitude + "+&daddr=" + delivarableLatLongUser.latitude + "," + delivarableLatLongUser.longitude));
            startActivity(intent);
        }

        public void onHelp(View v) {
            startActivity(new Intent(OrderAcceptedAndDeliverActivity.this, HelpActivity.class));

        }

    }

    void showTiming() {
        try {
            myTimer.cancel();
            Log.e("@@@@@@@@@@@@", "Timer canceled");
        } catch (Exception e) {
        }


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
                binder.secText.setVisibility(View.GONE);
            }

            @Override
            public void onExpire() {
                binder.tvTime.setText("Time Expired!");
                binder.secText.setVisibility(View.GONE);
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
