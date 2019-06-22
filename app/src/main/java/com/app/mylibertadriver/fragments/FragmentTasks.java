package com.app.mylibertadriver.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.activities.AcceptOrderActivity;
import com.app.mylibertadriver.activities.AcceptRestaurantActivity;
import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.databinding.FragmentTasksBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.orders.TaskModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.utils.AppUtils;
import com.app.mylibertadriver.utils.GoogleApiUtils;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Date;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class FragmentTasks extends Fragment {
    Presenter p = new Presenter();
    @Inject
    APIInterface apiInterface;
    private FragmentTasksBinding binder;
    private TaskModel bindableModel;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false);
        binder.llCurrentTask.setClick(p);
        binder.llNewTask.setClick(p);
        View view = binder.getRoot();

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
                        } else {
                            ResponseDialog.showErrorDialog(getActivity(), response.getMessage());
                        }
                    }
                });
    }




    public void onUpdatedLocation(LocationResult locationResult) {
        bindableModel.getOrderInfo().
                setDistance(
                        AppUtils.getDistanceBitweenLatlongInKM(
                                new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()),
                                new LatLng(bindableModel.getOrderInfo().getRestaurantInfo().getLocation().getCoordinates().get(0), bindableModel.getOrderInfo().getRestaurantInfo().getLocation().getCoordinates().get(1))
                        ) + " Km."

                );
    }


    void updateTimeToExpire() {
        if (bindableModel != null) {
            if (bindableModel.getStatus().equals("0"))  // mean it new task
            {
                Date date = AppUtils.getUTCDateObjectFromUTCTime(bindableModel.getCreatedAt());
                Date myTime = AppUtils.getCurrentDateINUTC();
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
