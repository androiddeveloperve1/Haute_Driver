package com.foodies.vedriver.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.foodies.vedriver.R;
import com.foodies.vedriver.constants.Constants;
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

public class SplashActivity extends AppCompatActivity {
    ActivityRunnable runnable;
    Handler handler = new Handler();
    @Inject
    APIInterface apiInterface;

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
                .subscribe(new Subscriber<ApiResponseModel<UserModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(SplashActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<UserModel> response) {
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

    private class ActivityRunnable implements Runnable {
        private UserModel model;
        @Override
        public void run() {
            model = MySharedPreference.getInstance(SplashActivity.this).getUser();
            if (model != null) {
                if (model.getIs_mobile_verify().equals("1")) {
                    if (model.getIs_document_upload().equals("1")) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, UploadDocumentActivity.class));
                        finish();
                    }
                } else {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("mobile_no", model.getMobile_no());
                    sendOtp(param);
                }
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }
    }


}
