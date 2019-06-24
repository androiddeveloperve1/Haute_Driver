package com.app.mylibertadriver.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.activities.AcceptOrderActivity;
import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.adapter.TodayEarningAdapter;
import com.app.mylibertadriver.databinding.FragmentTodayEarningBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.orders.TaskModelResponse;
import com.app.mylibertadriver.model.orders.TaskResponse;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.utils.SimpleDividerItemDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class FragmentEarning extends Fragment implements RecycleItemClickListener {
    @Inject
    APIInterface apiInterface;
    private FragmentTodayEarningBinding binder;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_today_earning, container, false);
        binder.rvEarnList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binder.rvEarnList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        View view = binder.getRoot();
        getDetails();
        return view;
    }

    @Override
    public void onItemClicked(int position, Object data) {
    }

    private void getDetails() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(getActivity());
        ((MyApplication) getActivity().getApplication()).getConfiguration().inject(this);
        apiInterface.getTaskList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<TaskModelResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(getActivity(), throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<TaskModelResponse> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            binder.setAdapt(new TodayEarningAdapter(response.getData().getList(), FragmentEarning.this));
                        } else {
                            ResponseDialog.showErrorDialog(getActivity(), response.getMessage());
                        }
                    }
                });
    }

    public class Presenter {
        public void onHomeClicked(View view) {
        }
    }
}