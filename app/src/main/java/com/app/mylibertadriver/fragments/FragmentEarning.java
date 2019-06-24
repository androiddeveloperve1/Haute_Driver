package com.app.mylibertadriver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.adapter.TodayEarningAdapter;
import com.app.mylibertadriver.databinding.FragmentTodayEarningBinding;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.utils.SimpleDividerItemDecoration;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
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