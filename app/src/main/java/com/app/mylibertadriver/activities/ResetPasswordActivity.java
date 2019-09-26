package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.ActivityResetPasswordBinding;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.prefes.MySharedPreference;
import com.app.mylibertadriver.viewmodeles.ResetPassViewModel;

import java.util.HashMap;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class ResetPasswordActivity extends AppCompatActivity {
    ActivityResetPasswordBinding binder;
    private boolean isPasswordShow = false;
    private boolean isConfirmPasswordShow = false;
    private ResetPassViewModel resetPassViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        resetPassViewModel = ViewModelProviders.of(ResetPasswordActivity.this).get(ResetPassViewModel.class);
        binder.setHandler(new Listener());
    }



    @Override
    protected void onDestroy() {
        MySharedPreference.getInstance(ResetPasswordActivity.this).clearMyPreference();
        super.onDestroy();
    }

    public class Listener {

        public void onSend(View e) {

            if (binder.etPass.getText().toString().trim().length() <= 0) {
                Toast.makeText(ResetPasswordActivity.this, "Please enter the Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binder.etPassConfirm.getText().toString().trim().length() <= 0) {
                Toast.makeText(ResetPasswordActivity.this, "Please enter the confirm Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!binder.etPassConfirm.getText().toString().trim().equals(binder.etPass.getText().toString().trim())) {
                Toast.makeText(ResetPasswordActivity.this, "Password mismatched", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String, String> param = new HashMap<>();
            param.put("password", binder.etPass.getText().toString().trim());
            resetPassViewModel.getData(ResetPasswordActivity.this, param).observe(ResetPasswordActivity.this, new Observer<DriverModel>() {
                @Override
                public void onChanged(DriverModel driverModel) {

                }
            });

        }
        public void onBack(View e) {
            onBackPressed();
        }


        public void onPassClick(View e) {
            if (!isPasswordShow) {
                binder.etPass.setInputType(InputType.TYPE_CLASS_TEXT);
                binder.etPass.setTransformationMethod(null);
                binder.imgPass1.setBackgroundResource(R.drawable.ic_eye_off);
                isPasswordShow = true;
            } else {
                binder.imgPass1.setBackgroundResource(R.drawable.ic_eye_on);
                binder.etPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binder.etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isPasswordShow = false;
            }


        }

        public void onconfirmPassClick(View e) {
            if (!isConfirmPasswordShow) {
                binder.imgPassConfirm.setBackgroundResource(R.drawable.ic_eye_off);
                binder.etPassConfirm.setInputType(InputType.TYPE_CLASS_TEXT);
                binder.etPassConfirm.setTransformationMethod(null);
                isConfirmPasswordShow = true;
            } else {
                binder.imgPassConfirm.setBackgroundResource(R.drawable.ic_eye_on);
                binder.etPassConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binder.etPassConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isConfirmPasswordShow = false;
            }
        }


    }
}
