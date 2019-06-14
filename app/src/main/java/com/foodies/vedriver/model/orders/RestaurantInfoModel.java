package com.foodies.vedriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.foodies.vedriver.BR;
import com.foodies.vedriver.model.UserModel;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class RestaurantInfoModel extends BaseObservable {
    private String name;
    private String address;
    private ArrayList<String> images;


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        this.notifyPropertyChanged(BR.address);
    }

    @Bindable
    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
        this.notifyPropertyChanged(BR.images);
    }
}
