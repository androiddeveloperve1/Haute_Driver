package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.adapter.OrderItemAdapter;
import com.app.mylibertadriver.databinding.ActivityReachedRestaurantActivtyBinding;
import com.app.mylibertadriver.dialogs.ResponseDialog;
import com.app.mylibertadriver.interfaces.RecycleItemClickListener;
import com.app.mylibertadriver.interfaces.SwipeListener;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.orders.OrderDetailsModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.utils.SwipeView;
import com.google.gson.Gson;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public class ReachedRestaurantActivty extends AppCompatActivity {
    ActivityReachedRestaurantActivtyBinding binder;
    @Inject
    APIInterface apiInterface;

    private String orderId;
    private OrderDetailsModel orderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_reached_restaurant_activty);
        binder.setClick(new ClickHandler());
        binder.rvItem.setLayoutManager(new LinearLayoutManager(this));
        orderId = getIntent().getStringExtra("order_id");

        orderDetails=new OrderDetailsModel();
        binder.setData(orderDetails);

        binder.swipeView.setEventListener(new SwipeListener() {
            @Override
            public void swipeStarted() {
            }

            @Override
            public void Swiped(int flag) {
                if (flag == SwipeView.SWIPED_RIGHT) {
                    itemCollected();

                }
            }
        });
        binder.swipeView.setText("ITEMS COLLECTED");
        getOrderDetails();
    }

    private void getOrderDetails() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(ReachedRestaurantActivty.this);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.getOrderDetails(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel<OrderDetailsModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(ReachedRestaurantActivty.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel<OrderDetailsModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(ReachedRestaurantActivty.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.getStatus().equals("200")) {
                            orderDetails = response.getData();
                            //binder.setData(orderDetails);
                            binder.setMAdapter(new OrderItemAdapter(orderDetails.getOrder(), new RecycleItemClickListener() {
                                @Override
                                public void onItemClicked(int position, Object data) {
                                }
                            }));

                        } else {
                            ResponseDialog.showErrorDialog(ReachedRestaurantActivty.this, response.getMessage());
                        }
                    }
                });
    }

    private void itemCollected() {
        final Dialog progressDialog = ResponseDialog.showProgressDialog(ReachedRestaurantActivty.this);
        ((MyApplication) getApplication()).getConfiguration().inject(this);
        apiInterface.orderPicked(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        progressDialog.dismiss();
                        ResponseDialog.showErrorDialog(ReachedRestaurantActivty.this, throwable.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ApiResponseModel response) {
                        progressDialog.dismiss();
                        Toast.makeText(ReachedRestaurantActivty.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.getStatus().equals("200")) {
                            Intent mIntent = new Intent(ReachedRestaurantActivty.this, OrderAcceptedAndDeliverActivity.class);
                            mIntent.putExtra("data", new Gson().toJson(orderDetails));
                            startActivity(mIntent);
                            finish();
                        } else {
                            binder.swipeView.swipeLeft();
                            ResponseDialog.showErrorDialog(ReachedRestaurantActivty.this, response.getMessage());
                        }
                    }
                });
    }

    public class ClickHandler {

        public void onBack(View v) {
            finish();
        }


        public void onHelp(View v) {
            startActivity(new Intent(ReachedRestaurantActivty.this, HelpActivity.class));

        }
    }
}
