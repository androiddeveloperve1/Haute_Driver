package com.foodies.vedriver.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.foodies.vedriver.constants.PermissionConstants;
import com.foodies.vedriver.interfaces.LocationDetector;
import com.foodies.vedriver.permission.PermissionUtils;
import com.foodies.vedriver.utils.LocationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Rahul on 19/12/17.
 */

public class LocationListenerActivity extends AppCompatActivity {
    private LocationUtils locationUtils = null;
    private ProgressDialog progressDoalog;
    private LocationDetector dector = null;




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (locationUtils != null)
            locationUtils.onStop();
    }




    /*
     * Handle Gps Enable event
     * */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LocationUtils.LocationTag) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
                    stopProgress();
                    dector.onErrors(PermissionConstants.GPS_DISABLED);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    protected void onStop() {
        if (locationUtils != null)
            locationUtils.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (locationUtils != null)
            locationUtils.onStop();
        super.onDestroy();
    }

    /*
     *  Progress dialog Will Appear here
     * */
    public void listenLocation(LocationDetector dector) {
        this.dector = dector;
        if (this.progressDoalog != null && this.progressDoalog.isShowing()) {
            return;
        }
        this.progressDoalog = new ProgressDialog(this);
        this.progressDoalog.setMessage("Please wait....");
        this.progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDoalog.setCancelable(false);
        this.progressDoalog.show();
    }

    /*
     * Progress dialog will cancel here
     * */
    public void stopProgress() {
        if (this.progressDoalog != null && progressDoalog.isShowing()) {
            this.progressDoalog.dismiss();
        }
    }
}
