package com.foodies.vedriver.activities;

import android.app.Application;


import com.foodies.vedriver.network.ApplicationComponent;
import com.foodies.vedriver.network.DaggerApplicationComponent;
import com.foodies.vedriver.network.NetworkModule;

import java.io.File;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class MyApplication extends Application {
    private ApplicationComponent configuration;

    public void onCreate() {
        initDagger();
        super.onCreate();
    }

    private void initDagger() {
        File cacheFile = new File(getCacheDir(), "responses");
        configuration = DaggerApplicationComponent.builder().networkModule(new NetworkModule(getApplicationContext(), cacheFile)).build();
    }


    public ApplicationComponent getConfiguration() {
        return configuration;
    }

}
