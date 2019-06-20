package com.app.mylibertadriver.viewmodeles;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.mylibertadriver.activities.EnterOTPActivity;
import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.prefes.MySharedPreference;
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

public class SignupViewModel extends AndroidViewModel {
    @Inject
    APIInterface apiInterface;
    MutableLiveData<DriverModel> modelData;


    public SignupViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<DriverModel> getData(Context mContext, final HashMap param) {
        if (modelData == null) {
            modelData = new MutableLiveData<DriverModel>();
            getDataFromServer(mContext, param);
        }
        return modelData;
    }


    private void getDataFromServer(final Context mContext, final HashMap param) {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(mContext);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.userRegster(param)
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
                        Log.e("@@@@@@@@@@@suCCESS", new Gson().toJson(response));
                        if (response.getStatus().equals("200")) {
                            modelData.postValue(response.getData());
                            MySharedPreference.getInstance(mContext).setUser(response.getData());
                            mContext.startActivity(new Intent(mContext, EnterOTPActivity.class));
                        } else {
                            modelData = null;
                            ResponseDialog.showErrorDialog(mContext, response.getMessage());
                        }

                    }
                });
    }


}
