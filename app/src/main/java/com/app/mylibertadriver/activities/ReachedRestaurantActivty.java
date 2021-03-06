package com.app.mylibertadriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.app.mylibertadriver.interfaces.TimerListener;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.orders.OrderDetailsModel;
import com.app.mylibertadriver.model.orders.OrderItemModel;
import com.app.mylibertadriver.network.APIInterface;
import com.app.mylibertadriver.utils.MyCountDownTimer;
import com.app.mylibertadriver.utils.SwipeView;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

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
    private CountDownTimer myTimer;
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
                        Log.e("@@@@@@@", "" + new Gson().toJson(response));
                        if (response.getStatus().equals("200")) {
                            orderDetails = response.getData();
                            binder.setData(orderDetails);
                            binder.setMAdapter(new OrderItemAdapter(ReachedRestaurantActivty.this, orderDetails.getOrder(), new RecycleItemClickListener<OrderItemModel>() {
                                @Override
                                public void onItemClicked(int position, OrderItemModel data) {
                                    Intent mIntent = new Intent(ReachedRestaurantActivty.this, OptionDetailActivity.class);
                                    mIntent.putExtra("data", new Gson().toJson(data));
                                    startActivity(mIntent);
                                }
                            }));
                       binder.tvCollectAmount.setText( "$" + String.format("%.02f", Float.parseFloat(orderDetails.getTotalamount())));


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
                            if (response.getMessage().equals("Order already Pickedup")) {
                                Intent mIntent = new Intent(ReachedRestaurantActivty.this, OrderAcceptedAndDeliverActivity.class);
                                mIntent.putExtra("data", new Gson().toJson(orderDetails));
                                startActivity(mIntent);
                                finish();
                            } else {
                                binder.swipeView.swipeLeft();
                                ResponseDialog.showErrorDialog(ReachedRestaurantActivty.this, response.getMessage());
                            }
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

    void showTiming() {
        try {
            myTimer.cancel();
            Log.e("@@@@@@@@@@@@","Timer canceled");
        } catch (Exception e) {
        }
        myTimer = new MyCountDownTimer.MyTimer().startNow(this, new TimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                String s = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                binder.tvTime.setText(s);
            }

            @Override
            public void onFinish() {
                binder.tvTime.setText("Time Expired!");
                binder.secText.setVisibility(View.GONE);
            }

            @Override
            public void onExpire() {
                binder.tvTime.setText("Time Expired!");
                binder.secText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showTiming();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            myTimer.cancel();
        } catch (Exception e) {
        }
    }
}
