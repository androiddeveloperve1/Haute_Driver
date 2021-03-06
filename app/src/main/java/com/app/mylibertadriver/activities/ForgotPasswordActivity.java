package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.databinding.ActivityForgotPasswordBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.prefes.MySharedPreference;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binder;
    @Inject
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        binder.setHandler(new Listener());

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
                        ResponseDialog.showErrorDialog(ForgotPasswordActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<DriverModel> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            MySharedPreference.getInstance(ForgotPasswordActivity.this).setUser(response.getData());
                            Intent mIntent = new Intent(ForgotPasswordActivity.this, EnterOTPActivity.class);
                            mIntent.putExtra("flag", Constants.FROM_FORGOT_PASS);
                            startActivity(mIntent);
                            finish();
                        } else {
                            ResponseDialog.showErrorDialog(ForgotPasswordActivity.this, response.getMessage());
                        }
                    }
                });
    }

    public class Listener {

        public void onSend(View e) {
            if (binder.etMobile.getText().toString().trim().length() <= 0) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binder.etMobile.getText().toString().trim().length() < 8) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter the mobile no. between 8 to 15", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String, String> param = new HashMap<>();
            param.put("mobile_no", binder.etMobile.getText().toString().trim());
            param.put("country_code", "1");
            sendOtp(param);

        }

        public void onBack(View e) {
            finish();
        }

    }
}
