package com.app.mylibertadriver.interfaces;

import com.google.android.gms.location.LocationResult;
/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
public interface CommanTaskListener {

    void onServicesReady();

    void onNoInternetFound();

    void onUpdatedLocation(LocationResult locationResult);


}
