package com.foodies.vedriver.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.foodies.vedriver.R;
import com.foodies.vedriver.adapter.TodayEarningAdapter;
import com.foodies.vedriver.databinding.FragmentTasksBinding;
import com.foodies.vedriver.databinding.FragmentTodayEarningBinding;
import com.foodies.vedriver.interfaces.RecycleItemClickListener;
import com.foodies.vedriver.utils.SimpleDividerItemDecoration;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class FragmentEarning extends Fragment implements RecycleItemClickListener {
    private FragmentTodayEarningBinding binder;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_today_earning, container, false);
        binder.rvEarnList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binder.rvEarnList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        binder.setAdapt(new TodayEarningAdapter(this));
        View view = binder.getRoot();

        return view;
    }

    @Override
    public void onItemClicked(int position, Object data) {

    }


    public class Presenter {
        public void onHomeClicked(View view) {
        }
    }
}