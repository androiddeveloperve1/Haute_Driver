package com.app.mylibertadriver.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.constants.PermissionConstants;
import com.app.mylibertadriver.databinding.ActivitySignupBinding;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.permission.PermissionHandlerListener;
import com.app.mylibertadriver.permission.PermissionUtils;
import com.app.mylibertadriver.utils.AppUtils;
import com.app.mylibertadriver.viewmodeles.SignupViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class SignupActivity extends AppCompatActivity {

    public static final int LocationTag = 1000;

    ActivitySignupBinding binder;
    private boolean isPasswordShow = false;

    private boolean isCBSelected = false;
    private LatLng mlaLatLng;
    private SignupViewModel mSignupViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        mSignupViewModel = ViewModelProviders.of(this).get(SignupViewModel.class);
        binder.setHandler(new Listener());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationTag) {
            if (data != null) {
                mlaLatLng = new LatLng(data.getDoubleExtra("lat", 0.0), data.getDoubleExtra("long", 0.0));
                binder.etServiceArea.setText(data.getStringExtra("address"));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.getInstance().hendlePermission(SignupActivity.this, requestCode, permissions, grantResults);
    }

    void hitSignUp() {
        HashMap<String, String> param = new HashMap<>();
        param.put("service_area", "" + binder.etServiceArea.getText().toString());
        param.put("fname", binder.etFname.getText().toString().trim());
        param.put("lname", binder.etLname.getText().toString().trim());
        param.put("email", binder.etEmail.getText().toString().trim());
        param.put("password", binder.etPass.getText().toString().trim());
        param.put("country_code", binder.spnrCountry.getSelectedItem().toString().replace("+", ""));
        param.put("mobile_no", binder.etMob.getText().toString().trim());
        HashMap<String, String> location = new HashMap<>();
        location.put("type", "Point");
        if (mlaLatLng != null) {
            location.put("coordinates", "[" + mlaLatLng.latitude + "," + mlaLatLng.longitude + "]");
        } else {
            location.put("coordinates", "[0.0,0.0]");
        }
        param.put("location", new Gson().toJson(location));
        Log.e("@@@@@@@@params", new Gson().toJson(param));
        mSignupViewModel.getData(SignupActivity.this, param).observe(this, new Observer<DriverModel>() {
            @Override
            public void onChanged(DriverModel apiResponseModel) {
            }
        });

    }


    public class Listener {
        public void onCurrentLocation(View e) {
            PermissionUtils.getInstance().checkAllPermission(SignupActivity.this, PermissionConstants.permissionArrayForLocation, new PermissionHandlerListener() {
                @Override
                public void onGrant() {
                    startActivityForResult(new Intent(SignupActivity.this, Maps2Activity.class), LocationTag);
                }

                @Override
                public void onReject(ArrayList<String> remainsPermissonList) {
                }

                @Override
                public void onRationalPermission(ArrayList<String> rationalPermissonList) {
                    PermissionUtils.firePerimisionActivity(SignupActivity.this);
                }
            });

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void onSignIn(View e) {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finishAffinity();
        }

        public void onCheckBox(View e) {
            if (!isCBSelected) {
                binder.imgCb.setBackgroundResource(R.drawable.ic_check_on);
                isCBSelected = true;
            } else {
                binder.imgCb.setBackgroundResource(R.drawable.ic_check_off);
                isCBSelected = false;
            }
        }

        public void onTermClick(View e) {
        }


        public void onPassView(View e) {
            if (!isPasswordShow) {
                binder.etPass.setInputType(InputType.TYPE_CLASS_TEXT);
                binder.etPass.setTransformationMethod(null);
                binder.imgPass.setBackgroundResource(R.drawable.ic_eye_on);
                isPasswordShow = true;
            } else {
                binder.imgPass.setBackgroundResource(R.drawable.ic_eye_off);
                binder.etPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binder.etPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isPasswordShow = false;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void onSignUp(View e) {
            if (binder.etFname.getText().toString().trim().length() <= 0) {
                Toast.makeText(SignupActivity.this, "Please enter the First name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binder.etLname.getText().toString().trim().length() <= 0) {
                Toast.makeText(SignupActivity.this, "Please enter the last name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binder.etMob.getText().toString().trim().length() <= 0) {
                Toast.makeText(SignupActivity.this, "Please enter the mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binder.etMob.getText().toString().trim().length() < 10) {
                Toast.makeText(SignupActivity.this, "Please enter the valid mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binder.etEmail.getText().toString().trim().length() <= 0) {
                Toast.makeText(SignupActivity.this, "Please enter the email id", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!AppUtils.eMailValidation(binder.etEmail.getText().toString().trim())) {
                Toast.makeText(SignupActivity.this, "Please enter the valid email id", Toast.LENGTH_SHORT).show();
                return;
            }
            if (binder.etServiceArea.getText().toString().trim().length() <= 0) {
                Toast.makeText(SignupActivity.this, "Please enter the address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isCBSelected) {
                Toast.makeText(SignupActivity.this, "Please check the check box to agree terms and service", Toast.LENGTH_SHORT).show();
                return;
            }
            hitSignUp();
        }
    }

}
