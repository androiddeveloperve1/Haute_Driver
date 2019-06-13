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

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class SwipeViewDialog extends Dialog {
    private Context mContext;
    private ImageView iv_close;
    private RelativeLayout rl_swipe_area;
    private ImageView swipe_thumb;

    public SwipeViewDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
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
        rl_swipe_area = findViewById(R.id.rl_swipe_area);
        swipe_thumb = findViewById(R.id.swipe_thumb);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        initSlider();
        setCancelable(false);

    }



    void initSlider() {
        rl_swipe_area.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                swipe_thumb.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        int action = motionEvent.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                return true;
                            case MotionEvent.ACTION_MOVE:
                                if (rl_swipe_area.getRight() >= (motionEvent.getRawX() + swipe_thumb.getWidth())) {
                                    swipe_thumb.setX(motionEvent.getRawX());
                                }
                                return true;
                            case MotionEvent.ACTION_UP:

                                if ((motionEvent.getRawX() + swipe_thumb.getWidth()) < (rl_swipe_area.getRight() / 2)) {
                                    swipe_thumb.setX(0);
                                } else if ((motionEvent.getRawX() + swipe_thumb.getWidth()) > (rl_swipe_area.getRight() / 2)) {
                                    swipe_thumb.setX(rl_swipe_area.getRight() - swipe_thumb.getWidth());
                                }
                                return true;
                            case MotionEvent.ACTION_CANCEL:
                                return true;
                        }
                        return true;
                    }
                });
                rl_swipe_area.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
