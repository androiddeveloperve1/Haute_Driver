package com.foodies.vedriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.foodies.vedriver.R;
import com.foodies.vedriver.constants.Constants;
import com.foodies.vedriver.databinding.ActivityForgotPasswordBinding;
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
                .subscribe(new Subscriber<ApiResponseModel<UserModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(ForgotPasswordActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<UserModel> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            MySharedPreference.getInstance(ForgotPasswordActivity.this).setUser(response.getData());
                            Intent mIntent = new Intent(ForgotPasswordActivity.this, EnterOTPActivity.class);
                            mIntent.putExtra("flag", Constants.FROM_FORGOT_PASS);
                            startActivity(mIntent);
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
            if (binder.etMobile.getText().toString().trim().length() < 10) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter the valid mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String, String> param = new HashMap<>();
            param.put("mobile_no", binder.etMobile.getText().toString().trim());
            sendOtp(param);

        }

        public void onBack(View e) {
            finish();
        }

    }
}
