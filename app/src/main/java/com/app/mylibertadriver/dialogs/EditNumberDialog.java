package com.app.mylibertadriver.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.DialogEditMobileBinding;
import com.app.mylibertadriver.databinding.DialogProgressBinding;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class EditNumberDialog {
    DialogEditMobileBinding binding;
    Dialog termAndConditionDialog;
    private Context mContext;
    private OnSelect listner;

    public Dialog showProgressDialog(Context mContext, String mobile) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_edit_mobile, null, false);
        this.mContext=mContext;
        this.listner=(OnSelect)mContext;
        termAndConditionDialog = new Dialog(mContext);
        termAndConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding.etMobile.setText(mobile);
        binding.setClick(new Click());
        termAndConditionDialog.setContentView(binding.getRoot());
        termAndConditionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        termAndConditionDialog.getWindow().setLayout(mContext.getResources().getDimensionPixelOffset(R.dimen._250_px), LinearLayout.LayoutParams.WRAP_CONTENT);
        termAndConditionDialog.getWindow().setGravity(Gravity.CENTER);
        termAndConditionDialog.setCancelable(false);
        termAndConditionDialog.show();
        return termAndConditionDialog;
    }

    public interface OnSelect {
        void onOk(String msg);
    }

    public class Click {
        public void onOk(View v) {
            if (binding.etMobile.getText().toString().trim().length() < 8) {
                Toast.makeText(mContext, "Please enter the mobile no. between 8 to 15", Toast.LENGTH_SHORT).show();
            } else {
                listner.onOk(binding.etMobile.getText().toString().trim());
                termAndConditionDialog.dismiss();
            }
        }

        public void onCancel(View v) {
            termAndConditionDialog.dismiss();
        }


    }

}