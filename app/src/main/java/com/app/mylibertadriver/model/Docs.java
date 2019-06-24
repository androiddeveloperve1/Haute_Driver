package com.app.mylibertadriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class Docs extends BaseObservable {

    private String path;
    private String status;


    @Bindable
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        this.notifyPropertyChanged(BR.path);
    }
    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.notifyPropertyChanged(BR.status);
    }
}
