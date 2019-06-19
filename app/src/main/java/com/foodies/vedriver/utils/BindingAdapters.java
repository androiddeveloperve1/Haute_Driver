package com.foodies.vedriver.utils;

import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.foodies.vedriver.R;
import com.squareup.picasso.Picasso;

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
