package com.app.mylibertadriver.fragments;

import android.app.Dialog;
import android.content.Intent;
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
import com.app.mylibertadriver.activities.WeeklyDetailActivity;
import com.app.mylibertadriver.adapter.EarningAdapter;
import com.app.mylibertadriver.adapter.OrderHistoryAdapter;
import com.app.mylibertadriver.databinding.FragmentTodayEarningBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.EarningModel;
import com.app.mylibertadriver.model.EarningModelResponse;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.utils.AppUtils;
import com.app.mylibertadriver.utils.SimpleDividerItemDecoration;

import java.io.File;
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
        binder.setClick(new Presenter());
        getDetails();

        return view;
    }

    @Override
    public void onItemClicked(int position, Object data) {
    }

    private void getDetails() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(getActivity());
        ((MyApplication) getActivity().getApplication()).getConfiguration().inject(this);
        apiInterface.getEarning()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<ArrayList<EarningModelResponse>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(getActivity(), throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<ArrayList<EarningModelResponse>> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {

                            if (response.getData().size() > 0) {
                                binder.viewAll.setVisibility(View.VISIBLE);
                                EarningModelResponse data = response.getData().get(0);
                                binder.setAdapt(new EarningAdapter(data));
                                binder.setData(data);
                                binder.transferDate.setText(AppUtils.getHumanReadableTimeFromUTCString(data.getEarningDetails().get(0).getTransfered_date()));
                                float totalAmt = 0;
                                for (EarningModel a : data.getEarningDetails()) {
                                    totalAmt = totalAmt + (Float.parseFloat(a.getDelivery_fees()) + Float.parseFloat(a.getGratitude_fees()));
                                }
                                binder.tvAmount.setText("$" + totalAmt);
                            }


                        } else {
                            ResponseDialog.showErrorDialog(getActivity(), response.getMessage());
                        }
                    }
                });
    }

    public class Presenter {
        public void viewAll(View view) {
            startActivity(new Intent(getActivity(), WeeklyDetailActivity.class));
        }
    }
}