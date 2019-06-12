package com.foodies.vedriver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.foodies.vedriver.R;
import com.foodies.vedriver.databinding.ActivityLoginBinding;
import com.foodies.vedriver.model.UserModel;
import com.foodies.vedriver.viewmodeles.LoginViewModel;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binder;
    private boolean isPasswordShow = false;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binder.setHandler(new Listener());
    }


    public class Listener {

        public void onForgot(View e) {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            finish();
        }

        public void onSignIn(View e) {

            if (binder.etEmpid.getText().toString().trim().length() <= 0) {
                Toast.makeText(LoginActivity.this, "Please  enter the emp Id", Toast.LENGTH_SHORT).show();
                return;
            }

            if (binder.etPass.getText().toString().trim().length() <= 0) {
                Toast.makeText(LoginActivity.this, "Please  enter the password", Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, String> param = new HashMap<>();
            param.put("employee_id", binder.etEmpid.getText().toString().trim());
            param.put("password", binder.etPass.getText().toString().trim());

            loginViewModel.getData(LoginActivity.this, param).observe(LoginActivity.this, new Observer<UserModel>() {
                @Override
                public void onChanged(UserModel userModel) {

                }
            });


        }

        public void onSignUp(View e) {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        }

        public void onPassView(View e) {
            if (!isPasswordShow) {
                binder.etPass.setInputType(InputType.TYPE_CLASS_TEXT);
                binder.etPass.setTransformationMethod(null);
                binder.imgPass.setBackgroundResource(R.drawable.ic_eye_off);
                isPasswordShow = true;
            } else {
                binder.imgPass.setBackgroundResource(R.drawable.ic_eye_on);
                binder.etPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binder.etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isPasswordShow = false;
            }
        }


    }
}
