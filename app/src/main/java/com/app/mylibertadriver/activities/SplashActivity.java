package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.DocsStatusModel;
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


public class SplashActivity extends AppCompatActivity {
    ActivityRunnable runnable;
    Handler handler = new Handler();
    @Inject
    APIInterface apiInterface;
    private DriverModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        runnable = new ActivityRunnable();
        handler.postDelayed(runnable, 3000);
    }

    private void sendOtp(final HashMap param) {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(this);
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
                        ResponseDialog.showErrorDialog(SplashActivity.this, Constants.NO_INTERNET_CONNECTION_FOUND_TAG);
                    }

                    @Override
                    public void onNext(ApiResponseModel<DriverModel> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            MySharedPreference.getInstance(SplashActivity.this).setUser(response.getData());
                            Intent mIntent = new Intent(SplashActivity.this, EnterOTPActivity.class);
                            mIntent.putExtra("flag", Constants.FROM_SPLASH);
                            startActivity(mIntent);
                            finish();
                        } else {
                            ResponseDialog.showErrorDialog(SplashActivity.this, response.getMessage());
                        }
                    }
                });
    }

    private void getDocsInfo() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(SplashActivity.this);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.getDocStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<DocsStatusModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(SplashActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<DocsStatusModel> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            DriverModel model = MySharedPreference.getInstance(SplashActivity.this).getUser();
                            model.setIs_document_upload(response.getData().getIs_document_upload());
                            model.setIs_document_verify(response.getData().getIs_document_verify());
                            model.setDriverlicense(response.getData().getDriverlicense());
                            model.setInsurance(response.getData().getInsurance());
                            MySharedPreference.getInstance(SplashActivity.this).setUser(model);
                            if (response.getData().getIs_document_upload().equals("1") && response.getData().getIs_document_verify().equals("1")) {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(SplashActivity.this, UploadDocumentActivity.class));
                                finish();
                            }

                        } else {
                            ResponseDialog.showErrorDialog(SplashActivity.this, response.getMessage());
                        }
                    }
                });
    }

    private class ActivityRunnable implements Runnable {
        @Override
        public void run() {
            model = MySharedPreference.getInstance(SplashActivity.this).getUser();
            if (model != null && model.get_id() != null) {
                if (model.getIs_mobile_verify() != null && model.getIs_mobile_verify().equals("1") ) {
                    getDocsInfo();
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("mobile_no", model.getMobile_no());
                    param.put("country_code", model.getCountry_code());
                    sendOtp(param);
                }
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }
    }

}
