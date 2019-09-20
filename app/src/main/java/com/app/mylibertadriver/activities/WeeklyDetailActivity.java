package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.adapter.WeeklyDetailsAdapter;
import com.app.mylibertadriver.databinding.ActivityWeeklyDetailBinding;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.model.orders.TaskResponse;

import java.util.ArrayList;

public class WeeklyDetailActivity extends AppCompatActivity {
    private ActivityWeeklyDetailBinding binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_weekly_detail);
        binder.setClick(new MyClick());
        binder.setAdapter(new WeeklyDetailsAdapter(new ArrayList<TaskResponse>(), new RecycleItemClickListener() {
            @Override
            public void onItemClicked(int position, Object data) {

            }
        }));
    }

    public class MyClick {
        public void onBack(View v) {
            finish();
        }
    }
}
