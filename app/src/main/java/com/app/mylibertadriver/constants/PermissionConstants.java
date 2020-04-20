package com.app.mylibertadriver.constants;

import android.Manifest;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class PermissionConstants {
    public static String permissionArrayForImageCapture[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static String permissionArrayForLocation[] = { Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    public static String permissionArrayForWRITE[] = { Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static int GALLERY_REQUEST_CODE = 12;
    public static int CAMERA_REQUEST_CODE = 14;


    public static final int AllPermission = 1000;


}
