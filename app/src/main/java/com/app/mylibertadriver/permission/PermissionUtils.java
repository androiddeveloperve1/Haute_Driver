package com.app.mylibertadriver.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.mylibertadriver.constants.PermissionConstants;

import java.util.ArrayList;
import java.util.List;


// PermissionUtils.getInstance().checkAllPermission(mAcivity, permissonArray, listener);
//PermissionUtils.getInstance().hendlePermission(mAcivity, requestCode, permissions, grantResults);

public class PermissionUtils {
    public static final int RequestCode = 1000;
    private static PermissionUtils instance = null;
    private static String permissionArrayForLocation[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private PermissionHandlerListener listener;

    public static PermissionUtils getInstance() {
        if (instance == null) {
            instance = new PermissionUtils();
        }
        return instance;
    }

    /****************************************************************END************/
    public static boolean checkBuildLess23() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return false;
    }

    public static boolean checkAllPermissionForLocation(Context mContext) {
        if (checkBuildLess23()) {
            return true;
        } else {
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (int i = 0; i < permissionArrayForLocation.length; i++) {
                if (ContextCompat.checkSelfPermission(mContext, permissionArrayForLocation[i]) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissionArrayForLocation[i]);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions((Activity) mContext, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PermissionConstants.AllPermission);
                return false;
            }
        }
        return true;
    }

    public static boolean checkResultAllPrmission(Context mContext, String permissions[], int[] grantResults) {
        boolean isPermissionGranted = true;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = false;
                break;
            }
        }
        return isPermissionGranted;
    }

    public void checkAllPermission(Activity mContext, String permissionArray[], PermissionHandlerListener listener) {
        this.listener = listener;
        if (checkBuildLess23()) {
            this.listener.onGrant();
        } else {
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (int j = 0; j < permissionArray.length; j++) {
                if (ContextCompat.checkSelfPermission(mContext, permissionArray[j]) != PackageManager.PERMISSION_GRANTED) {
                    // add the denide permission in the list
                    listPermissionsNeeded.add(permissionArray[j]);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(mContext, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), RequestCode);
            } else {
                this.listener.onGrant();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkAllPermissionFragment(Activity mContext, String permissionArray[], PermissionHandlerListener listener) {
        this.listener = listener;
        if (checkBuildLess23()) {
            this.listener.onGrant();
        } else {
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (int j = 0; j < permissionArray.length; j++) {
                if (ContextCompat.checkSelfPermission(mContext, permissionArray[j]) != PackageManager.PERMISSION_GRANTED) {
                    // add the denide permission in the list
                    listPermissionsNeeded.add(permissionArray[j]);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                this.listener.onRequestPermissionNow(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), RequestCode);
            } else {
                this.listener.onGrant();
            }
        }
    }

    public void hendlePermission(Activity mActivity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> remiansPermission = new ArrayList<>();
        ArrayList<String> rationalPermission = new ArrayList<>();
        switch (requestCode) {
            case RequestCode:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        remiansPermission.add(permissions[i]);
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissions[i]);

                        if (!showRationale) {
                            // add permission in list , if user used the never ask again
                            rationalPermission.add(permissions[i]);
                        }
                    }
                }
                if (remiansPermission.size() == 0) {
                    listener.onGrant();
                } else if (remiansPermission.size() > 0) {
                    if (rationalPermission.size() > 0) {
                        listener.onRationalPermission(rationalPermission);
                    } else {
                        listener.onReject(remiansPermission);
                    }
                }

                break;
        }
    }

    public void hendlePermissionForFragment(Activity mActivity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> remiansPermission = new ArrayList<>();
        ArrayList<String> rationalPermission = new ArrayList<>();
        switch (requestCode) {
            case RequestCode:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        remiansPermission.add(permissions[i]);
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissions[i]);

                        if (!showRationale) {
                            // add permission in list , if user used the never ask again
                            rationalPermission.add(permissions[i]);
                        }
                    }
                }
                if (remiansPermission.size() == 0) {
                    listener.onGrant();
                } else if (remiansPermission.size() > 0) {
                    if (rationalPermission.size() > 0) {
                        listener.onRationalPermission(rationalPermission);
                    } else {
                        listener.onReject(remiansPermission);
                    }
                }

                break;
        }
    }


}
