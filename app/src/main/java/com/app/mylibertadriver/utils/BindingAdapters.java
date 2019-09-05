package com.app.mylibertadriver.utils;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.app.mylibertadriver.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

    @BindingAdapter("android:call")
    public static void callNow(@Nullable TextView view, final String number) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("@@@@@@",""+number);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
                view.getContext().startActivity(intent);
            }
        });


    }

    @BindingAdapter("android:setText")
    public static void setText(@Nullable TextView view, final String amount) {

        if (amount != null && amount.trim().length() > 0) {
            view.setText("$ " + amount);
        } else {
            view.setText("");
        }


    }

    @BindingAdapter("android:setTextWithoutDoller")
    public static void setText2(@Nullable TextView view, final String amount) {

        if (amount != null && amount.trim().length() > 0) {
            view.setText(amount);
        } else {
            view.setText("");
        }


    }
}
