package com.app.mylibertadriver.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.DialogProgressBinding;
import com.app.mylibertadriver.databinding.DialogResponseErrorBinding;

public class ResponseDialog {
    public static void showErrorDialog(Context mContext, String message) {

        DialogResponseErrorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_response_error, null, false);
        final Dialog termAndConditionDialog = new Dialog(mContext);
        binding.tvMessage.setText(message);
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                termAndConditionDialog.dismiss();
            }
        });
        termAndConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        termAndConditionDialog.setContentView(binding.getRoot());
        termAndConditionDialog.setTitle("Error!");
        termAndConditionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        termAndConditionDialog.getWindow().setLayout(mContext.getResources().getDimensionPixelOffset(R.dimen._250_px), LinearLayout.LayoutParams.WRAP_CONTENT);
        termAndConditionDialog.getWindow().setGravity(Gravity.CENTER);
        termAndConditionDialog.setCancelable(true);
        termAndConditionDialog.show();
    }

    public static Dialog showProgressDialog(Context mContext) {
        DialogProgressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_progress, null, false);
        final Dialog termAndConditionDialog = new Dialog(mContext);
        termAndConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        termAndConditionDialog.setContentView(binding.getRoot());
        termAndConditionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        termAndConditionDialog.getWindow().setLayout(mContext.getResources().getDimensionPixelOffset(R.dimen._250_px), LinearLayout.LayoutParams.WRAP_CONTENT);
        termAndConditionDialog.getWindow().setGravity(Gravity.CENTER);
        termAndConditionDialog.setCancelable(true);
        termAndConditionDialog.show();
        return termAndConditionDialog;
    }

}