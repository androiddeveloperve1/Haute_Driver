package com.foodies.vedriver.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.foodies.vedriver.R;
import com.foodies.vedriver.interfaces.SwipeListener;
import com.foodies.vedriver.utils.SwipeView;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class SwipeViewDialog extends Dialog {
    private Context mContext;
    private ImageView iv_close;
    private SwipeView mSwipeView;
    private SwipeListener mSwipeListener;

    public SwipeViewDialog(@NonNull Context context, SwipeListener mSwipeListener) {
        super(context);
        this.mContext = context;
        this.mSwipeListener = mSwipeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_order_delivered);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        iv_close = findViewById(R.id.iv_close);
        mSwipeView = findViewById(R.id.mSwipeView);
        mSwipeView.setEventListener(mSwipeListener);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setCancelable(false);

    }

}
