package com.app.mylibertadriver.utils;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.interfaces.SwipeListener;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class SwipeView extends RelativeLayout {
    public static int SWIPED_LEFT = 0;
    public static int SWIPED_RIGHT = 1;
    private RelativeLayout rl_swipe_area;
    private ImageView swipe_thumb;
    private TextView tv_taxt;
    private SwipeListener mSwipeListener;
    OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (mSwipeListener != null)
                        mSwipeListener.swipeStarted();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (rl_swipe_area.getRight() >= (motionEvent.getRawX() + swipe_thumb.getWidth())) {
                        swipe_thumb.setX(motionEvent.getRawX());
                    }
                    return true;
                case MotionEvent.ACTION_UP:

                    if ((motionEvent.getRawX() + swipe_thumb.getWidth()) < (rl_swipe_area.getRight() / 2)) {

                        if (mSwipeListener != null)
                            mSwipeListener.Swiped(SWIPED_LEFT);
                        swipe_thumb.setX(0);


                    } else if ((motionEvent.getRawX() + swipe_thumb.getWidth()) > (rl_swipe_area.getRight() / 2)) {

                        if (mSwipeListener != null)
                            mSwipeListener.Swiped(SWIPED_RIGHT);
                        swipe_thumb.setX(rl_swipe_area.getRight() - swipe_thumb.getWidth());

                    }
                    return true;
                case MotionEvent.ACTION_CANCEL:
                    return true;
            }
            return true;
        }
    };

    public SwipeView(Context context) {
        super(context);
        init();
    }

    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwipeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setEventListener(SwipeListener mSwipeListener) {
        this.mSwipeListener = mSwipeListener;
    }

    private void init() {
        inflate(getContext(), R.layout.swipe_view, this);
        this.rl_swipe_area = (RelativeLayout) findViewById(R.id.rl_swipe_area);
        this.swipe_thumb = (ImageView) findViewById(R.id.swipe_thumb);
        this.tv_taxt = (TextView) findViewById(R.id.tv_taxt);
        rl_swipe_area.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                swipe_thumb.setOnTouchListener(touchListener);
                rl_swipe_area.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void setText(String text) {
        tv_taxt.setText(text);
    }


    public void enableSwipe() {
        swipe_thumb.setEnabled(true);
        swipe_thumb.setClickable(true);
        swipe_thumb.setOnTouchListener(touchListener);
    }

    public void disableSwipe() {
        swipe_thumb.setEnabled(false);
        swipe_thumb.setClickable(false);
        swipe_thumb.setOnTouchListener(null);
    }

    public void swipeLeft() {
        swipe_thumb.setX(0);
    }



}
