package com.app.mylibertadriver.interfaces;

import com.google.android.gms.location.LocationResult;

public interface CommanTaskListener {

    void onServicesReady();

    void onNoInternetFound();

    void onUpdatedLocation(LocationResult locationResult);


}
