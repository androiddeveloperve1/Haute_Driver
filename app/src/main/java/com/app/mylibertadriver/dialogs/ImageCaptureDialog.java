package com.app.mylibertadriver.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.DialogCaptureImageBinding;
import com.app.mylibertadriver.interfaces.ImageOrGalarySelector;


/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class ImageCaptureDialog extends Dialog {
    public ImageCaptureDialog(Context context, ImageOrGalarySelector selector) {
        super(context, R.style.newDialogTransparent);
        DialogCaptureImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_capture_image, null, false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());
        binding.setClickHandler(selector);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(true);
    }

    public void showDoalog() {
        this.show();
    }

    public void dismissDoalog() {
        this.dismiss();
    }


}
