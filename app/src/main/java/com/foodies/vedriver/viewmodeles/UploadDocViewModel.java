package com.foodies.vedriver.viewmodeles;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.foodies.vedriver.activities.MainActivity;
import com.foodies.vedriver.activities.MyApplication;
import com.foodies.vedriver.dialogs.ResponseDialog;
import com.foodies.vedriver.model.ApiResponseModel;
import com.foodies.vedriver.model.UserModel;
import com.foodies.vedriver.network.APIInterface;
import com.foodies.vedriver.prefes.MySharedPreference;
import com.foodies.vedriver.utils.MultipartUtils;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class UploadDocViewModel extends AndroidViewModel {
    @Inject
    APIInterface apiInterface;
    MutableLiveData<UserModel> modelData;

    public UploadDocViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<UserModel> uploadImage(Context mContext, Bitmap mBitmap, String key) {
        modelData = new MutableLiveData<UserModel>();
        uploadToServer(mContext, MultipartUtils.createFile(mContext, mBitmap, key));
        return modelData;
    }


    private void uploadToServer(final Context mContext, MultipartBody.Part body) {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(mContext);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.uploadDoc(body)
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
                        Log.e("@@@@@@@", new Gson().toJson(response.getData()));
                        if (response.getStatus().equals("200")) {
                            modelData.postValue(response.getData());
                            MySharedPreference.getInstance(mContext).setUser(response.getData());
                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                            ((Activity) mContext).finish();
                        } else {
                            modelData = null;
                            ResponseDialog.showErrorDialog(mContext, response.getMessage());
                        }

                    }
                });
    }

}
