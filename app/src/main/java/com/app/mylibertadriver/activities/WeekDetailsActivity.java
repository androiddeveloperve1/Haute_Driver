package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.adapter.EarningAdapter;
import com.app.mylibertadriver.databinding.ActivityWeekDetailsBinding;
import com.app.mylibertadriver.databinding.ActivityWeeklyDetailBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.EarningModel;
import com.app.mylibertadriver.model.EarningModelResponse;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.utils.AppUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeekDetailsActivity extends AppCompatActivity {
    @Inject
    APIInterface apiInterface;
    private ActivityWeekDetailsBinding binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_week_details);
        binder.setClick(new Presenter());
        getDetails();


    }

    private void getDetails() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(this);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.walletDetail()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<ArrayList<EarningModelResponse>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(WeekDetailsActivity.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<ArrayList<EarningModelResponse>> response) {
                        Log.e("@@@@@@@@@@", "" + new Gson().toJson(response));
                        progressDialog.dismiss();
                        if (response.getStatus().equals("200")) {

                            if (response.getData().size() > 0) {
                                EarningModelResponse data = response.getData().get(0);
                                binder.setAdapt(new EarningAdapter(data));
                                binder.setData(data);

                                try{
                                binder.transferDate.setText(AppUtils.getHumanReadableTimeFromUTCString(data.getEarningDetails().get(0).getTransfered_date()));
                                }catch (Exception e){}

                                float totalAmt = 0;
                                for (EarningModel a : data.getEarningDetails()) {
                                    totalAmt = totalAmt + (Float.parseFloat(a.getDelivery_fees()) + Float.parseFloat(a.getGratitude_fees()));
                                }
                                binder.tvAmount.setText("$" + totalAmt);
                            }

                        } else {
                            ResponseDialog.showErrorDialog(WeekDetailsActivity.this, response.getMessage());
                        }
                    }
                });
    }

    public class Presenter {
        public void onBack(View v) {
            finish();
        }

    }
}
