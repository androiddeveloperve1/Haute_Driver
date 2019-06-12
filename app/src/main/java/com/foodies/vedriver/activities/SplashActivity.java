package com.foodies.vedriver.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.foodies.vedriver.R;
import com.foodies.vedriver.prefes.MySharedPreference;

public class SplashActivity extends AppCompatActivity {
    ActivityRunnable runnable;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        runnable = new ActivityRunnable();
        handler.postDelayed(runnable, 3000);
    }


    private class ActivityRunnable implements Runnable {
        @Override
        public void run() {
            if (MySharedPreference.getInstance(SplashActivity.this).getUser() != null) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }
    }
}
