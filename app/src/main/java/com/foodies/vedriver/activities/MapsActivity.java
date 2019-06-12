package com.foodies.vedriver.activities;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.foodies.vedriver.R;
import com.foodies.vedriver.adapter.GooglePlacesAutocompleteAdapter;
import com.foodies.vedriver.constants.PermissionConstants;
import com.foodies.vedriver.interfaces.LocationDetector;
import com.foodies.vedriver.model.ApiResponseModel;
import com.foodies.vedriver.network.APIInterface;
import com.foodies.vedriver.utils.GoogleApiUtils;
import com.foodies.vedriver.utils.LocationUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    @Inject
    APIInterface apiInterface;
    private GoogleMap mMap;
    private TextView toolbar_address;
    private AutoCompleteTextView enter_new_address;
    private ProgressDialog progressDoalog;
    private LocationUtils mLocationUtils;
    private LatLng selectedLocation;
    private String address = "";
    private MarkerOptions mMarkerOptions;
    LocationDetector locationDetector = new LocationDetector() {
        @Override
        public void OnLocationChange(Location location) {
            selectedLocation = new LatLng(location.getLatitude(), location.getLongitude());
            stopProgress();
            mLocationUtils.onStop();
            showLocationOnMap(new LatLng(location.getLatitude(), location.getLongitude()));

        }

        @Override
        public void OnLastLocationFound(Location location) {
            selectedLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onErrors(int errorCode) {
            Log.e("@@@", "" + errorCode);
        }
    };
    private GooglePlacesAutocompleteAdapter googlePlacesAutocompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);
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
        enter_new_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LatLng selectedAddressLatlng = GoogleApiUtils.getLatLongByPlace(googlePlacesAutocompleteAdapter.getPlacesList().get(i));
                showLocationOnMap(selectedAddressLatlng);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
            @Override
            public void onClick(View view) {
                enter_new_address.setText("");
                mLocationUtils = new LocationUtils(MapsActivity.this, locationDetector);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMarkerOptions = new MarkerOptions();
        mMap = googleMap;
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
        mLocationUtils = new LocationUtils(MapsActivity.this, locationDetector);

    }

    public void listenLocation(LocationDetector dector) {
        this.locationDetector = dector;
        if (this.progressDoalog != null && this.progressDoalog.isShowing()) {
            return;
        }
        this.progressDoalog = new ProgressDialog(this);
        this.progressDoalog.setMessage("Please wait....");
        this.progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDoalog.setCancelable(false);
        this.progressDoalog.show();
    }

    /*
     * Progress dialog will cancel here
     * */
    public void stopProgress() {
        if (this.progressDoalog != null && progressDoalog.isShowing()) {
            this.progressDoalog.dismiss();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationUtils.LocationTag) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    mLocationUtils.startProgressNow();
                    break;
                case Activity.RESULT_CANCELED:
                    stopProgress();
                    locationDetector.onErrors(PermissionConstants.GPS_DISABLED);
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationUtils != null)
            mLocationUtils.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mLocationUtils != null)
            mLocationUtils.onStop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mLocationUtils != null)
            mLocationUtils.onStop();
        doFinish();
    }

    void showLocationOnMap(final LatLng loc) {
        mMap.clear();
        mMap.addMarker(mMarkerOptions.position(loc).draggable(true).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        hitApiToGetAddress();

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
        address = "Address from Api";
        GoogleApiUtils.getAddressFromLatLong(selectedLocation);
        //https://maps.googleapis.com/maps/api/geocode/json?latlng=44.4647452,7.3553838&key=AIzaSyBbNBMYzMBd9A_YuakL89TuZR8buL7kSiU

       /* ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.userRegster(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("@@@@@@@@@@@", throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel response) {
                        Log.e("@@@@@@@@@@@", new Gson().toJson(response));
                        startActivity(new Intent(SignupActivity.this, EnterOTPActivity.class));
                        finishAffinity();
                    }
                });*/
    }


}
