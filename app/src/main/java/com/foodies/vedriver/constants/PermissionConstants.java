package com.foodies.vedriver.constants;

import android.Manifest;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class PermissionConstants {
    public static String permissionArrayForImageCapture[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static String permissionArrayForLocation[] = { Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public static int GALLERY_REQUEST_CODE = 12;
    public static int CAMERA_REQUEST_CODE = 14;


    public static final int AllPermission = 1000;
    public static final int NO_NETWORK = 1;
    public static final int PERMISSION_REJECTED = 2;
    public static final int GPS_DISABLED = 3;
    public static final int PLAYSERICE_ERROR = 4;
    public static final int GOOGLE_PLAY_CONNECTION_ERROR = 5;
    public static final int GOOGLE_PLAY_CONNECTION_SUSPEND = 6;
    public static final int NO_LAST_LOCATION_FOUND = 7;
    public static final int PERMISSION_RATIONALE = 8;


}
