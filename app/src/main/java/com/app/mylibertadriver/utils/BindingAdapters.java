package com.app.mylibertadriver.utils;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.app.mylibertadriver.R;
import com.squareup.picasso.Picasso;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class BindingAdapters {


    @BindingAdapter("android:fontStyleName")
    public static void setFont(TextView textView, String fontName) {
        Typeface typeface = Typeface.createFromAsset(textView.getContext().getAssets(), fontName);
        textView.setTypeface(typeface);
    }

    @BindingAdapter("android:imageUrl")
    public static void loadImageRestaurant(@Nullable ImageView view, @Nullable String image) {
        Picasso.with(view.getContext()).load(image).placeholder(R.drawable.placeholder_squre).into(view);
    }


}
