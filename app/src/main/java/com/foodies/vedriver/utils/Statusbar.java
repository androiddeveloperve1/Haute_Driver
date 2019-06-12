package com.foodies.vedriver.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.foodies.vedriver.R;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class Statusbar {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void changeStatusColor(Activity actContext) {
        Window window = actContext.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(actContext.getResources().getColor(R.color.black));
    }
}
