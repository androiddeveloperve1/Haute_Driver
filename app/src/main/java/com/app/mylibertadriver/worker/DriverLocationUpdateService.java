package com.app.mylibertadriver.worker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.network.APIInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class DriverLocationUpdateService extends Worker {
    @Inject
    APIInterface apiInterface;
    private Context mContext;


    public DriverLocationUpdateService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Result doWork() {
        return startProgressNow();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Result startProgressNow() {
        if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        LocationServices.getFusedLocationProviderClient(mContext).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                sendDriverCurrentLocationToServer(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        });
        return Result.retry();
    }

    void sendDriverCurrentLocationToServer(LatLng latlng) {
        Log.e("@@@@@@@@", "location updated in background");
        HashMap param = new HashMap();
        param.put("latitude", "" + latlng.latitude);
        param.put("longitude", "" + latlng.longitude);
        ((MyApplication) mContext).getConfiguration().inject(this);
        apiInterface.updateDriverLocation(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onNext(ApiResponseModel response) {
                        Log.e("@@@@Location To Server", new Gson().toJson(response));
                    }
                });
    }

}
