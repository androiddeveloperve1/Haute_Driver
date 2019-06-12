package com.foodies.vedriver.interfaces;

import android.location.Location;

/**
 * Created by Rahul on 13/12/17.
 */

public abstract class LocationDetector {
    public abstract void OnLocationChange(Location location);

    public abstract void OnLastLocationFound(Location location);

    public void onErrors(int errorCode) {
    }
}
