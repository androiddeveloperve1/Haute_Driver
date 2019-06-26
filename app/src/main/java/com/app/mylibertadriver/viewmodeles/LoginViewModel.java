package com.app.mylibertadriver.viewmodeles;

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

import com.app.mylibertadriver.activities.EnterOTPActivity;
import com.app.mylibertadriver.activities.MainActivity;
import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.activities.UploadDocumentActivity;
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
 * Project Haute Delivery
 */

public class LoginViewModel extends AndroidViewModel {
    @Inject
    APIInterface apiInterface;
    MutableLiveData<DriverModel> modelData;

    public LoginViewModel(@NonNull Application application) {
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
        apiInterface.userLogin(param)
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
                        Log.e("@@@@@@@@@@@", "" + new Gson().toJson(response.getData()));
                        if (response.getStatus().equals("200")) {
                            MySharedPreference.getInstance(mContext).setUser(response.getData());
                            if (response.getData().getIs_document_verify().equals("1") && response.getData().getIs_document_upload().equals("1")) {
                                modelData.postValue(response.getData());
                                mContext.startActivity(new Intent(mContext, MainActivity.class));
                                ((Activity) mContext).finish();
                            } else {
                                Toast.makeText(mContext, "Your documents are not approved yet", Toast.LENGTH_SHORT).show();
                                mContext.startActivity(new Intent(mContext, UploadDocumentActivity.class));
                                ((Activity) mContext).finish();
                            }
                        } else {
                            modelData = null;
                            ResponseDialog.showErrorDialog(mContext, response.getMessage());
                        }

                    }
                });
    }
}
