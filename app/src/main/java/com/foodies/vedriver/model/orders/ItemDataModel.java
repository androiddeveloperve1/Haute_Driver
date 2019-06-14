package com.foodies.vedriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.foodies.vedriver.BR;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class ItemDataModel extends BaseObservable {

    private String _id;
    private String name;
    private String createdAt;
    private String updatedAt;
    private String __v;

    @Bindable
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        this.notifyPropertyChanged(BR.createdAt);
    }

    @Bindable
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        this.notifyPropertyChanged(BR.updatedAt);
    }

    @Bindable
    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
        this.notifyPropertyChanged(BR.__v);
    }
}
