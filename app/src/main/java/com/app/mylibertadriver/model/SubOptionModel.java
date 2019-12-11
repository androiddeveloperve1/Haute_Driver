package com.app.mylibertadriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class SubOptionModel extends BaseObservable {

    private String name;
    private String price;
    private String bestPrice;
    private String markupStructure;
    private String _id;
    private String suboption_id;

    @Bindable
    public String getName() {
        return name;
    }
    @Bindable
    public String getPrice() {
        return price;
    }
    @Bindable
    public String getBestPrice() {
        return bestPrice;
    }
    @Bindable
    public String getMarkupStructure() {
        return markupStructure;
    }
    @Bindable
    public String get_id() {
        return _id;
    }
    @Bindable
    public String getSuboption_id() {
        return suboption_id;
    }

    public void setName(String name) {
        this.name = name;
        this.notifyPropertyChanged(BR.name);
    }

    public void setPrice(String price) {
        this.price = price;
        this.notifyPropertyChanged(BR.price);
    }

    public void setBestPrice(String bestPrice) {
        this.bestPrice = bestPrice;
        this.notifyPropertyChanged(BR.bestPrice);
    }

    public void setMarkupStructure(String markupStructure) {
        this.markupStructure = markupStructure;
        this.notifyPropertyChanged(BR.markupStructure);
    }

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
    }

    public void setSuboption_id(String suboption_id) {
        this.suboption_id = suboption_id;
        this.notifyPropertyChanged(BR.suboption_id);
    }
}
