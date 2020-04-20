package com.app.mylibertadriver.worker;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.mylibertadriver.activities.GoogleServicesActivationActivity;
import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.utils.AppUtils;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Create By Rahul Mangal
 * Project Haute Delivery
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
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        LocationServices.getFusedLocationProviderClient(mContext).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                try {
                    sendDriverCurrentLocationToServer(new LatLng(location.getLatitude(), location.getLongitude()));
                } catch (Exception e) {
                }
            }
        });
        return Result.retry();
    }


    void sendDriverCurrentLocationToServer(final LatLng latlng) {
        HashMap param = new HashMap();
        param.put("latitude", "" + latlng.latitude);
        param.put("longitude", "" + latlng.longitude);
        Log.e("@@@@@@@@@", "" + new Gson().toJson(param));
        //writeToSDFile(AppUtils.getCurrentDateINUTC() + "----->" + latlng.latitude + "," + latlng.longitude + "\n");
        ((MyApplication) mContext).getConfiguration().inject(this);
        apiInterface.updateDriverLocation(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel>() {
                    @Override
                    public void onCompleted() {
                        Log.e("@@@@", "Location updated to server2");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("@@@@Error", "Location updated to server2");
                    }

                    @Override
                    public void onNext(ApiResponseModel response) {
                        Log.e("@@@@", "Location updated to server");
                        //writeToSDFile(latlng.latitude+"<--->"+latlng.longitude);
                    }
                });
    }


    private void writeToSDFile(String latng) {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/LibertaLatlng");
        dir.mkdirs();
        File file = new File(dir, "loc.txt");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            out.write(latng);
            out.close();
        } catch (IOException e) {
            e.toString();
            Toast.makeText(mContext,"Enable Write permission",Toast.LENGTH_SHORT).show();
        }

    }


}
