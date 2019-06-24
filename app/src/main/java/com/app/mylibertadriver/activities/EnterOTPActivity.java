package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.Constants;
import com.app.mylibertadriver.databinding.ActivityEnterOtpBinding;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.prefes.MySharedPreference;
import com.app.mylibertadriver.viewmodeles.OtpVerifyViewModel;

import java.util.HashMap;
/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class EnterOTPActivity extends AppCompatActivity {
    ActivityEnterOtpBinding binder;
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
        isFromForgotPassScreen = getIntent().getIntExtra("flag", 0);

        if (isFromForgotPassScreen != Constants.FROM_SIGNUP) {
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

    public class Listener {

        public void onBack(View e) {
            finish();
        }

        public void onResend(View e) {
            binder.otp1.requestFocus();
            binder.otp1.setText("");
            binder.otp2.setText("");
            binder.otp3.setText("");
            binder.otp4.setText("");
            HashMap<String, String> param = new HashMap<>();
            param.put("mobile_no", userData.getMobile_no());
            otpVerifyViewModel.getDataResendOtp(EnterOTPActivity.this, param);
        }

        public void onEdit(View e) {
            finish();
        }

        public void onVerify(View e) {
            HashMap<String, String> param = new HashMap<>();
            param.put("deviceId", MySharedPreference.getInstance(EnterOTPActivity.this).getFCM());
            param.put("deviceType", Constants.ANDROID_KEY);
            param.put("otp", binder.otp1.getText().toString() + binder.otp2.getText().toString() + binder.otp3.getText().toString() + binder.otp4.getText().toString());
            otpVerifyViewModel.getData(EnterOTPActivity.this, param).observe(EnterOTPActivity.this, new Observer<DriverModel>() {
                @Override
                public void onChanged(DriverModel driverModel) {
                    MySharedPreference.getInstance(EnterOTPActivity.this).setUser(driverModel);
                    if (isFromForgotPassScreen == Constants.FROM_FORGOT_PASS) {
                        startActivity(new Intent(EnterOTPActivity.this, ResetPasswordActivity.class));
                        finishAffinity();
                    } else if (isFromForgotPassScreen == Constants.FROM_SIGNUP) {
                        startActivity(new Intent(EnterOTPActivity.this, UploadDocumentActivity.class));
                        finishAffinity();
                    } else {
                        if (driverModel.getIs_document_upload().equals("1")) {
                            startActivity(new Intent(EnterOTPActivity.this, MainActivity.class));
                        } else {
                            startActivity(new Intent(EnterOTPActivity.this, UploadDocumentActivity.class));
                        }
                        finishAffinity();
                    }
                }
            });

        }
    }
}