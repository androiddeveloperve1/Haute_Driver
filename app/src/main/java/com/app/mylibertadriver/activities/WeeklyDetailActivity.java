package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.adapter.EarningAdapter;
import com.app.mylibertadriver.adapter.WeeklyDetailsAdapter;
import com.app.mylibertadriver.databinding.ActivityWeeklyDetailBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.EarningModel;
import com.app.mylibertadriver.model.EarningModelResponse;
import com.app.mylibertadriver.model.WeeklyEarningModel;
import com.app.mylibertadriver.model.orders.TaskResponse;
import com.app.mylibertadriver.network.APIInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeeklyDetailActivity extends AppCompatActivity {
    @Inject
    APIInterface apiInterface;
    private ActivityWeeklyDetailBinding binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_weekly_detail);
        binder.setClick(new MyClick());
        binder.rvEarnList.setLayoutManager(new LinearLayoutManager(this));
        getList();

    }

    private void getList() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(WeeklyDetailActivity.this);
        ((MyApplication) getApplication()).getConfiguration().inject(WeeklyDetailActivity.this);
        apiInterface.getWalletList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<ArrayList<WeeklyEarningModel>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(WeeklyDetailActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<ArrayList<WeeklyEarningModel>> response) {
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {
                            binder.setAdapter(new WeeklyDetailsAdapter(response.getData(), new RecycleItemClickListener<WeeklyEarningModel>() {
                                @Override
                                public void onItemClicked(int position, WeeklyEarningModel data) {
                                    startActivity(new Intent(WeeklyDetailActivity.this, WeekDetailsActivity.class));
                                }
                            }));
                        } else {
                            ResponseDialog.showErrorDialog(WeeklyDetailActivity.this, response.getMessage());
                        }
                    }
                });
    }

    public class MyClick {
        public void onBack(View v) {
            finish();
        }
    }
}
