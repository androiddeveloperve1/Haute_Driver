package com.foodies.vedriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.foodies.vedriver.BR;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class Location extends BaseObservable {
    private String type="Point";
    private ArrayList<Double> coordinates;

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.notifyPropertyChanged(BR.type);
    }

    @Bindable
    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
        this.notifyPropertyChanged(BR.coordinates);
    }
}
