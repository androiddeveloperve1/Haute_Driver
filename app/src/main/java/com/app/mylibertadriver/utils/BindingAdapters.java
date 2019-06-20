package com.app.mylibertadriver.utils;

import android.graphics.Typeface;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class BindingAdapters {


    @BindingAdapter("android:fontStyleName")
    public static void setFont(TextView textView, String fontName) {
        Typeface typeface = Typeface.createFromAsset(textView.getContext().getAssets(), fontName);
        textView.setTypeface(typeface);
    }


}
