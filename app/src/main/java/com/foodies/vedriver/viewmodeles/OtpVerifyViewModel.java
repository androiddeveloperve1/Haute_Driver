package com.foodies.vedriver.viewmodeles;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.foodies.vedriver.activities.MainActivity;
import com.foodies.vedriver.activities.MyApplication;
import com.foodies.vedriver.activities.UploadDocumentActivity;
import com.foodies.vedriver.dialogs.ResponseDialog;
import com.foodies.vedriver.model.ApiResponseModel;
import com.foodies.vedriver.model.UserModel;
import com.foodies.vedriver.network.APIInterface;
import com.foodies.vedriver.prefes.MySharedPreference;
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
    MutableLiveData<UserModel> modelData;


    public OtpVerifyViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<UserModel> getData(Context mContext, final HashMap param) {
        if (modelData == null) {
            modelData = new MutableLiveData<UserModel>();
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
                .subscribe(new Subscriber<ApiResponseModel<UserModel>>() {
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
                    public void onNext(ApiResponseModel<UserModel> response) {
                        progressDialog.dismiss();
                        Log.e("@@@@@@@@@@@Success", new Gson().toJson(response));
                        if (response.getStatus().equals("200")) {
                            modelData.postValue(response.getData());

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
                .subscribe(new Subscriber<ApiResponseModel<UserModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(mContext, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<UserModel> response) {
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