package com.app.mylibertadriver.viewmodeles;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.mylibertadriver.activities.EnterOTPActivity;
import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.network.APIInterface;
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

public class OtpVerifyViewModel extends AndroidViewModel {
    @Inject
    APIInterface apiInterface;
    MutableLiveData<ApiResponseModel<DriverModel>> modelData;


    public OtpVerifyViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ApiResponseModel<DriverModel>> getData(Context mContext, final HashMap param) {
        if (modelData == null) {
            modelData = new MutableLiveData<ApiResponseModel<DriverModel>>();
            getDataFromServer(mContext, param);
        }
        return modelData;
    }


    public void getDataResendOtp(Context mContext, final HashMap param) {
        getDataFromServerResentOTP(mContext, param);
    }


    private void getDataFromServer(final Context mContext, final HashMap param) {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(mContext);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.verifyOTP(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<DriverModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(mContext, throwable.getLocalizedMessage());
                        modelData = null;
                    }

                    @Override
                    public void onNext(ApiResponseModel<DriverModel> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            modelData.postValue(response);
                        } else {
                            modelData = null;
                            ResponseDialog.showErrorDialog(mContext, response.getMessage());
                        }


                    }
                });
    }


    private void getDataFromServerResentOTP(final Context mContext, final HashMap param) {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(mContext);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.resendOTP(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<DriverModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(mContext, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<DriverModel> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            Toast.makeText(mContext, "Otp resend successfully ", Toast.LENGTH_SHORT).show();
                        } else {
                            ResponseDialog.showErrorDialog(mContext, response.getMessage());
                        }

                    }
                });
    }


}