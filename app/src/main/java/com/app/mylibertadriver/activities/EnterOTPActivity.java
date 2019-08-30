package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.databinding.ActivityEnterOtpBinding;
import com.app.mylibertadriver.dialogs.EditNumberDialog;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.prefes.MySharedPreference;
import com.app.mylibertadriver.viewmodeles.OtpVerifyViewModel;
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
public class EnterOTPActivity extends AppCompatActivity implements EditNumberDialog.OnSelect {
    ActivityEnterOtpBinding binder;
    @Inject
    APIInterface apiInterface;
    private DriverModel userData;
    private OtpVerifyViewModel otpVerifyViewModel;
    private int isFromForgotPassScreen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_enter_otp);
        otpVerifyViewModel = ViewModelProviders.of(this).get(OtpVerifyViewModel.class);
        binder.setHandler(new Listener());
        userData = MySharedPreference.getInstance(this).getUser();
        binder.textMobile.setText("We have send an OTP to " + userData.getMobile_no());
        binder.textMobile.setEnabled(true);
        isFromForgotPassScreen = getIntent().getIntExtra("flag", 0);
        binder.otp1.requestFocus();
        if (isFromForgotPassScreen == Constants.FROM_FORGOT_PASS) {
            binder.textMobileEdit.setVisibility(View.GONE);
        }
        setOtpEditTextListener();

    }

    void setOtpEditTextListener() {

        binder.otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    binder.otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        binder.otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    binder.otp3.requestFocus();
                }
                if (i2 == 0) {
                    binder.otp1.requestFocus(1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binder.otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    binder.otp4.requestFocus();
                }
                if (i2 == 0) {
                    binder.otp2.requestFocus(1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binder.otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 == 0) {
                    binder.otp3.requestFocus(1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onOk(String msg) {
        editMobile(msg);
    }


    private void editMobile(final String mobile) {
        HashMap<String, String> param = new HashMap<>();
        param.put("mobile_no", mobile);
        param.put("country_code", userData.getCountry_code());
        final Dialog progressDialog = ResponseDialog.showProgressDialog(EnterOTPActivity.this);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.changeMobileNumber(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<DriverModel>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(EnterOTPActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<DriverModel> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            binder.textMobile.setText("We have send an OTP to " + mobile);
                            MySharedPreference.getInstance(EnterOTPActivity.this).setUser(response.getData());
                            Toast.makeText(EnterOTPActivity.this, "Mobile no. changed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            ResponseDialog.showErrorDialog(EnterOTPActivity.this, response.getMessage());
                        }
                    }
                });
    }

    public class Listener {

        public void onBack(View e) {
            finish();
        }

        public void onResend(View e) {
            //userData.setMobile_no(binder.textMobile.getText().toString().trim().replace("We have send an OTP to ", ""));
            binder.otp1.requestFocus();
            binder.otp1.setText("");
            binder.otp2.setText("");
            binder.otp3.setText("");
            binder.otp4.setText("");
            HashMap<String, String> param = new HashMap<>();
            param.put("mobile_no", userData.getMobile_no());
            param.put("country_code", userData.getCountry_code());
            otpVerifyViewModel.getDataResendOtp(EnterOTPActivity.this, param);
        }

        public void onEdit(View e) {
            userData = MySharedPreference.getInstance(EnterOTPActivity.this).getUser();
            new EditNumberDialog().showProgressDialog(EnterOTPActivity.this, userData.getMobile_no());
        }

        public void onVerify(View e) {

            String otp = "";
            otp = binder.otp1.getText().toString() + binder.otp2.getText().toString() + binder.otp3.getText().toString() + binder.otp4.getText().toString();
            if (otp.length() < 4) {
                Toast.makeText(EnterOTPActivity.this, "Please enter 4 digit otp", Toast.LENGTH_SHORT).show();
                return;
            }


            HashMap<String, String> param = new HashMap<>();
            param.put("deviceId", MySharedPreference.getInstance(EnterOTPActivity.this).getFCM());
            param.put("deviceType", Constants.ANDROID_KEY);
            param.put("otp", binder.otp1.getText().toString() + binder.otp2.getText().toString() + binder.otp3.getText().toString() + binder.otp4.getText().toString());
            otpVerifyViewModel.getData(EnterOTPActivity.this, param).observe(EnterOTPActivity.this, new Observer<ApiResponseModel<DriverModel>>() {
                @Override
                public void onChanged(ApiResponseModel<DriverModel> driverModel) {
                    if (driverModel.getStatus().equals("200")) {
                        MySharedPreference.getInstance(EnterOTPActivity.this).setUser(driverModel.getData());
                        if (isFromForgotPassScreen == Constants.FROM_FORGOT_PASS) {
                            startActivity(new Intent(EnterOTPActivity.this, ResetPasswordActivity.class));
                            finishAffinity();
                        } else if (isFromForgotPassScreen == Constants.FROM_SIGNUP) {
                            startActivity(new Intent(EnterOTPActivity.this, UploadDocumentActivity.class));
                            finishAffinity();
                        } else {
                            if (driverModel.getData().getIs_document_upload().equals("1") && driverModel.getData().getIs_document_verify().equals("1")) {
                                startActivity(new Intent(EnterOTPActivity.this, MainActivity.class));
                            } else {
                                startActivity(new Intent(EnterOTPActivity.this, UploadDocumentActivity.class));
                            }
                            finishAffinity();
                        }
                    } else {
                        binder.otp1.setText("");
                        binder.otp2.setText("");
                        binder.otp3.setText("");
                        binder.otp4.setText("");
                        binder.otp1.requestFocus();
                        ResponseDialog.showErrorDialog(EnterOTPActivity.this, driverModel.getMessage());
                    }


                }
            });
        }
    }
}
