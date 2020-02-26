package com.app.mylibertadriver.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.app.mylibertadriver.activities.MainActivity;
import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.activities.OrderAcceptedAndDeliverActivity;
import com.app.mylibertadriver.databinding.FragmentTasksBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.orders.OrderDetailsModel;
import com.app.mylibertadriver.model.orders.TaskResponse;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.utils.AppUtils;
import com.app.mylibertadriver.utils.FetchURL;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class FragmentTasks extends Fragment {
    private final int EXPIRE_TIME = 60 * 2; //seconds
    Presenter p = new Presenter();
    @Inject
    APIInterface apiInterface;

    private FragmentTasksBinding binder;
    private TaskResponse bindableModel;
    private boolean isTaskAvailable = false;
    private MainActivity mainActivity;
    private CountDownTimer expiryTimer;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false);
        binder.llCurrentTask.setClick(p);
        binder.llNewTask.setClick(p);
        mainActivity = (MainActivity) getActivity();
        binder.llNewTask.setClick(p);
        binder.llCurrentTask.setIsVisible(View.GONE);
        binder.llNewTask.setIsVisible(View.GONE);


        View view = binder.getRoot();

        return view;
    }

    public void getTask() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(getActivity());
        ((MyApplication) getActivity().getApplication()).getConfiguration().inject(this);
        apiInterface.getTaskDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<TaskResponse>>() {
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
                    public void onNext(ApiResponseModel<TaskResponse> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            if (response.getData() != null && response.getData().get_id() != null) {
                                bindableModel = response.getData();
                                isTaskAvailable = true;
                                binder.rlTask.setVisibility(View.VISIBLE);
                                binder.tvNoTask.setVisibility(View.GONE);

                                if (bindableModel.getStatus().equals("0")) {
                                    //0 (new task)
                                    binder.llCurrentTask.setIsVisible(View.GONE);
                                    binder.llNewTask.setData(bindableModel.getOrderInfo());
                                    binder.llNewTask.setIsVisible(View.VISIBLE);
                                    updateTimeToExpire();
                                } else {
                                    // current task is running
                                    binder.llCurrentTask.setData(bindableModel.getOrderInfo());
                                    binder.llCurrentTask.setIsVisible(View.VISIBLE);
                                    binder.llNewTask.setIsVisible(View.GONE);
                                    binder.llCurrentTask.otp.setText("OTP : " + response.getData().getOtp());
                                }
                                if (mainActivity.mLocationResult != null) {
                                    onUpdatedLocation(mainActivity.mLocationResult);
                                }

                            } else {
                                isTaskAvailable = false;
                                binder.rlTask.setVisibility(View.GONE);
                                binder.tvNoTask.setVisibility(View.VISIBLE);
                            }

                        } else {
                            isTaskAvailable = false;
                            binder.rlTask.setVisibility(View.GONE);
                            binder.tvNoTask.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public void onUpdatedLocation(LocationResult locationResult) {
        if (isTaskAvailable) { new FetchURL(getActivity()).execute(AppUtils.getUrlForDrawRoute(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude())
                    , new LatLng(bindableModel.getOrderInfo().getRestaurantInfo().getLocation().getCoordinates().get(0), bindableModel.getOrderInfo().getRestaurantInfo().getLocation().getCoordinates().get(1)), "driving"));
        }
    }

    void updateTimeToExpire() {
        Date date = getIncrementedTime(AppUtils.getUTCDateObjectFromUTCTime(bindableModel.getCreatedAt()));
        Date myTime = AppUtils.getCurrentDateINUTC();
        long mills = myTime.getTime() - date.getTime();
        Log.e("@@@@@", "Different in sec :" + mills / (1000));
        if (mills <= 0) {
            startTimer(mills / (1000));// sec
        } else {

            binder.rlTask.setVisibility(View.GONE);
            binder.tvNoTask.setVisibility(View.VISIBLE);
        }

    }

    private void startTimer(long i) {
        if (expiryTimer != null) {
            expiryTimer.cancel();
        }
        expiryTimer = new CountDownTimer(Math.abs(i) * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                showSecondInView(millisUntilFinished);
            }

            public void onFinish() {
                Log.e("@@@@@", "done");
                isTaskAvailable = false;
                binder.rlTask.setVisibility(View.GONE);
                binder.tvNoTask.setVisibility(View.VISIBLE);


            }

        }.start();
    }


    void showSecondInView(long millisUntilFinished) {
        int totalSecond = (int) (millisUntilFinished / 1000);
        if (totalSecond <= 60) {
            bindableModel.getOrderInfo().setExpiryTime("00:" + totalSecond);

        } else {
            String mins = "" + (totalSecond / 60);
            String sec = "" + (totalSecond % 60);

            if (sec.length() == 2) {
                bindableModel.getOrderInfo().setExpiryTime(mins + ":" + sec);
            } else {
                bindableModel.getOrderInfo().setExpiryTime(mins + ":0" + sec);
            }
        }
    }

    public void onTaskDone(String distance) {
        bindableModel.getOrderInfo().setDistance(distance);
    }

    private void getOrderDetails(String orderId) {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(getActivity());
        ((MyApplication) getActivity().getApplication()).getConfiguration().inject(this);
        apiInterface.getOrderDetails(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<OrderDetailsModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(getActivity(), throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<OrderDetailsModel> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            Log.e("@@@@@@@@", "Delivery info" + new Gson().toJson(response.getData()));
                            Intent mIntent = new Intent(getActivity(), OrderAcceptedAndDeliverActivity.class);
                            mIntent.putExtra("data", new Gson().toJson(response.getData()));
                            startActivity(mIntent);

                        } else {
                            ResponseDialog.showErrorDialog(getActivity(), response.getMessage());
                        }
                    }
                });
    }

    Date getIncrementedTime(Date myTime) {
        Log.e("Current Time", myTime.toString());
        Calendar incrementTime = Calendar.getInstance();
        incrementTime.setTime(myTime);
        incrementTime.add(incrementTime.SECOND, EXPIRE_TIME);
        Log.e("InCrement Time", incrementTime.getTime().toString());
        return incrementTime.getTime();
    }

    public class Presenter {
        public void onCurrentTaskClicked(View view) {
            if (bindableModel.getOrderInfo().getDelivery_status().equals("4")) {
                getOrderDetails(bindableModel.getOrder_id());
            } else {
                Intent intent = new Intent(getActivity(), AcceptRestaurantActivity.class);
                intent.putExtra("data", new Gson().toJson(bindableModel));
                startActivity(intent);
            }
        }

        public void onNewTaskClicked(View view) {
            Intent mIntent = new Intent(getActivity(), AcceptOrderActivity.class);
            mIntent.putExtra("data", new Gson().toJson(bindableModel));
            startActivity(mIntent);
        }
    }

}
