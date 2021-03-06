package com.app.mylibertadriver.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.activities.MyApplication;
import com.app.mylibertadriver.adapter.OrderHistoryAdapter;
import com.app.mylibertadriver.databinding.FragmentOrderHistoryBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.orders.TaskModelResponse;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.utils.SimpleDividerItemDecoration;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class FragmentHistory extends Fragment implements RecycleItemClickListener {
    @Inject
    APIInterface apiInterface;
    private FragmentOrderHistoryBinding binder;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_order_history, container, false);
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
        apiInterface.getHIstory()
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
                            if(response.getData().getList().size()>0)
                            {
                                binder.rvEarnList.setVisibility(View.VISIBLE);
                                binder.noHistory.setVisibility(View.GONE);
                                binder.setAdapt(new OrderHistoryAdapter(response.getData().getList()));
                            }else {
                                binder.rvEarnList.setVisibility(View.GONE);
                                binder.noHistory.setVisibility(View.VISIBLE);
                            }
                        } else {
                            ResponseDialog.showErrorDialog(getActivity(), response.getMessage());
                        }
                    }
                });
    }


}