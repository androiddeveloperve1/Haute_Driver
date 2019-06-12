package com.foodies.vedriver.viewmodeles;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

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

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class ResetPassViewModel extends AndroidViewModel {
    @Inject
    APIInterface apiInterface;
    MutableLiveData<UserModel> modelData;

    public ResetPassViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<UserModel> getData(Context mContext, final HashMap param) {
        if (modelData == null) {
            modelData = new MutableLiveData<UserModel>();
            getDataFromServer(mContext, param);
        }
        return modelData;
    }


    private void getDataFromServer(final Context mContext, final HashMap param) {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(mContext);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.resetPassword(param)
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
                        if (response.getStatus().equals("200")) {
                            modelData.postValue(response.getData());
                            MySharedPreference.getInstance(mContext).setUser(response.getData());
                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                            ((Activity) mContext).finishAffinity();
                        } else {
                            modelData = null;
                            ResponseDialog.showErrorDialog(mContext, response.getMessage());
                        }

                    }
                });
    }
}