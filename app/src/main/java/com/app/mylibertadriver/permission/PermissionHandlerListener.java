package com.app.mylibertadriver.permission;

import java.util.ArrayList;

/**
 * Created by Rahul on 18/4/18.
 */

public abstract class PermissionHandlerListener {
    public abstract void onGrant();
    public abstract void onReject(ArrayList<String> remainsPermissonList);
    public void onRationalPermission(ArrayList<String> rationalPermissonList) {}
    public void onRequestPermissionNow(String arr[],int req) {}


}
