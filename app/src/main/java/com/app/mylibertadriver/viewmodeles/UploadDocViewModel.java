package com.app.mylibertadriver.viewmodeles;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.prefes.MySharedPreference;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class UploadDocViewModel extends AndroidViewModel {
    @Inject
    APIInterface apiInterface;
    MutableLiveData<DriverModel> modelData;

    public UploadDocViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<DriverModel> uploadImage(Context mContext, MultipartBody.Part[] part) {
        modelData = new MutableLiveData<DriverModel>();
        uploadToServer(mContext, part);
        return modelData;
    }


    private void uploadToServer(final Context mContext,MultipartBody.Part[] part) {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(mContext);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.uploadDoc(part)
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
                            modelData.postValue(response.getData());
                            MySharedPreference.getInstance(mContext).setUser(response.getData());
                        } else {
                            modelData = null;
                            ResponseDialog.showErrorDialog(mContext, response.getMessage());
                        }

                    }
                });
    }

}
