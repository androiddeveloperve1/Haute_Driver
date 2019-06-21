package com.app.mylibertadriver.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.activities.AcceptOrderActivity;
import com.app.mylibertadriver.activities.AcceptRestaurantActivity;
import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.constants.PermissionConstants;
import com.app.mylibertadriver.databinding.FragmentTasksBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.orders.TaskModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.permission.PermissionHandlerListener;
import com.app.mylibertadriver.permission.PermissionUtils;
import com.app.mylibertadriver.utils.GoogleApiUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class FragmentTasks extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final int LocationTag = 10001;
    Presenter p = new Presenter();
    @Inject
    APIInterface apiInterface;
    private FragmentTasksBinding binder;
    private GoogleApiClient mGoogleApiClient;
    private TaskModel bindableModel;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false);
        binder.llCurrentTask.setClick(p);
        binder.llNewTask.setClick(p);
        View view = binder.getRoot();
        getTask();
        return view;
    }

    public void getTask() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(getActivity());
        ((MyApplication) getActivity().getApplication()).getConfiguration().inject(this);
        apiInterface.getTaskDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<TaskModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(getActivity(), throwable.getLocalizedMessage());
                    }

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onNext(ApiResponseModel<TaskModel> response) {
                        progressDialog.dismiss();
                        Log.e("@@@@@@@@@@@Success", new Gson().toJson(response.getData()));
                        if (response.getStatus().equals("200")) {
                            bindableModel = response.getData();
                            if (bindableModel.getStatus().equals("1")) {
                                //  1 (current task )
                                binder.llCurrentTask.setIsVisible(View.VISIBLE);
                                binder.llCurrentTask.setData(bindableModel.getOrderInfo());
                                binder.llNewTask.setIsVisible(View.GONE);
                            } else if (bindableModel.getStatus().equals("0")) {
                                //0 (new task)
                                binder.llCurrentTask.setIsVisible(View.GONE);
                                binder.llNewTask.setIsVisible(View.VISIBLE);
                                binder.llNewTask.setData(bindableModel.getOrderInfo());
                            }
                            updateTimeToExpire();
                            startProgressNow();
                        } else {
                            ResponseDialog.showErrorDialog(getActivity(), response.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkResolutionAndProceed();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        updateTimeToExpire();
        PermissionUtils.getInstance().checkAllPermissionFragment(getActivity(), PermissionConstants.permissionArrayForLocation, new PermissionHandlerListener() {
            @Override
            public void onGrant() {
                if (checkGooglePlayServiceAvailability(getActivity())) {
                    buildGoogleApiClient();
                }
            }

            @Override
            public void onReject(ArrayList<String> remainsPermissonList) {
                Log.e("@@@@@@@@@@@", "Permission Rejected");
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onRequestPermissionNow(String[] arr, int req) {
                requestPermissions(PermissionConstants.permissionArrayForLocation, PermissionUtils.RequestCode);
            }

            @Override
            public void onRationalPermission(ArrayList<String> rationalPermissonList) {
                super.onRationalPermission(rationalPermissonList);
            }
        });


    }

    public boolean checkGooglePlayServiceAvailability(Context context) {
        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if ((statusCode == ConnectionResult.SUCCESS)) {
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, getActivity(), 10, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    getActivity().finish();
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return false;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    private void checkResolutionAndProceed() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResult(LocationSettingsResult result) {
                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startProgressNow();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), LocationTag);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.getInstance().hendlePermissionForFragment(getActivity(), requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startProgressNow() {
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (bindableModel != null)
                    bindableModel.getOrderInfo().
                            setDistance(
                                    GoogleApiUtils.getDistanceBitweenLatlongInKM(
                                            new LatLng(location.getLatitude(), location.getLongitude()),
                                            new LatLng(bindableModel.getOrderInfo().getRestaurantInfo().getLocation().getCoordinates().get(0), bindableModel.getOrderInfo().getRestaurantInfo().getLocation().getCoordinates().get(1))
                                    ) + " Km."

                            );

            }
        });
    }

    void updateTimeToExpire() {
        if (bindableModel != null) {
            if (bindableModel.getStatus().equals("0"))  // mean it new task
            {
                Date date = GoogleApiUtils.getUTCDateObjectFromUTCTime(bindableModel.getCreatedAt());
                Date myTime = GoogleApiUtils.getCurrentDateINUTC();
                long mills = myTime.getTime() - date.getTime();
                long hours = mills / (1000 * 60 * 60);
                long mins = (mills / (1000 * 60)) % 60;
                if (hours > 0) {
                    binder.llNewTask.setIsVisible(View.GONE);
                    return;
                } else if (mins > 2) {
                    binder.llNewTask.setIsVisible(View.GONE);
                    return;
                }
            }
        }
    }

    public class Presenter {
        public void onCurrentTaskClicked(View view) {
            Intent mIntent = new Intent(getActivity(), AcceptRestaurantActivity.class);
            mIntent.putExtra("data", new Gson().toJson(bindableModel));
            startActivity(mIntent);
        }

        public void onNewTaskClicked(View view) {
            Intent mIntent = new Intent(getActivity(), AcceptOrderActivity.class);
            mIntent.putExtra("data", new Gson().toJson(bindableModel));
            startActivity(mIntent);
        }
    }


}
