package com.app.mylibertadriver.worker;

import android.util.Log;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.app.mylibertadriver.constants.Constants;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class WorkUtils {
    private static OneTimeWorkRequest.Builder driverLocationRequest;


    public static void startBackgroundService() {
        try {
            ListenableFuture<List<WorkInfo>> work = WorkManager.getInstance().getWorkInfosByTag(Constants.BACKGROUND_WORKER_REQUEST);
            List<WorkInfo> work2 = work.get();
            if (work2.size() > 0) {
                if (work2.get(0).getState().isFinished()) {
                    buildWorkManager();
                    OneTimeWorkRequest req = driverLocationRequest.build();
                    WorkManager.getInstance().enqueue(req);
                    Log.e("--------------------", "Already Service but restarted");
                } else {
                    Log.e("--------------------", "Already Service running");
                }
            } else {
                Log.e("-------------------", "new Service");
                buildWorkManager();
                OneTimeWorkRequest req = driverLocationRequest.build();
                WorkManager.getInstance().enqueue(req);
            }
        } catch (Exception e) {

        }

    }

    private static void buildWorkManager() {
        driverLocationRequest = new OneTimeWorkRequest.Builder(DriverLocationUpdateService.class);
        driverLocationRequest.addTag(Constants.BACKGROUND_WORKER_REQUEST);
        driverLocationRequest.setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.SECONDS);
        driverLocationRequest.setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build());
    }

    public static void stopBackgroundService() {
        Log.e("@@@@@@", "Stoped");
        try{
            WorkManager.getInstance().cancelAllWorkByTag(Constants.BACKGROUND_WORKER_REQUEST);
        }catch (Exception e){}

    }
}
