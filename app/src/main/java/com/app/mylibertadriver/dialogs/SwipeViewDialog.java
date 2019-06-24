package com.app.mylibertadriver.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.DialogOrderDeliveredBinding;
import com.app.mylibertadriver.interfaces.SwipeListener;
import com.app.mylibertadriver.model.orders.OrderDetailsModel;
import com.app.mylibertadriver.utils.AppUtils;
import com.app.mylibertadriver.utils.SwipeView;
import com.google.gson.Gson;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class SwipeViewDialog extends Dialog {
    private Context mContext;
    private SwipeListener mSwipeListener;
    private OrderDetailsModel orderDetails;

    public SwipeViewDialog(@NonNull Context context, OrderDetailsModel orderDetails, SwipeListener mSwipeListener) {
        super(context);
        this.mContext = context;
        this.mSwipeListener = mSwipeListener;
        this.orderDetails = orderDetails;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogOrderDeliveredBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_order_delivered, null, false);
        binding.setData(orderDetails);
        setContentView(binding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        binding.mSwipeView.setEventListener(mSwipeListener);
        binding.mSwipeView.setText("DELIVERED");
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mSwipeListener.onCrossButton();
            }
        });
        binding.imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.requestCall(mContext,orderDetails.getUser_id().getMobile_no());
            }
        });
        binding.tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.requestCall(mContext,orderDetails.getUser_id().getMobile_no());
            }
        });
        setCancelable(false);


    }

}
