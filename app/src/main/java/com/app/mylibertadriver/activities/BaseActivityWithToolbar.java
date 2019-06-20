package com.app.mylibertadriver.activities;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.mylibertadriver.R;

public class BaseActivityWithToolbar extends AppCompatActivity {

    public void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    protected void setToolbarClicks(View mView, String title) {
        ImageView toolbar_humburgur = mView.findViewById(R.id.toolbar_humburgur);
        TextView toolbar_title = mView.findViewById(R.id.toolbar_title);
        ImageView toolbar_refresh = mView.findViewById(R.id.toolbar_refresh);
        toolbar_title.setText(title);


        toolbar_humburgur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


    }
}