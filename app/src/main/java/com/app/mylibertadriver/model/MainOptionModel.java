package com.app.mylibertadriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.app.mylibertadriver.model.orders.ItemDataModel;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class MainOptionModel extends BaseObservable {

    private String name;
    private String customerPrompt;
    private String _id;
    private String option_id;
    private ArrayList<SubOptionModel> suboptions;

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public String getCustomerPrompt() {
        return customerPrompt;
    }

    @Bindable
    public String get_id() {
        return _id;
    }

    @Bindable
    public String getOption_id() {
        return option_id;
    }

    @Bindable
    public ArrayList<SubOptionModel> getSuboptions() {
        return suboptions;
    }


    public void setName(String name) {
        this.name = name;
        this.notifyPropertyChanged(BR.name);
    }

    public void setCustomerPrompt(String customerPrompt) {
        this.customerPrompt = customerPrompt;
        this.notifyPropertyChanged(BR.customerPrompt);
    }

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
        this.notifyPropertyChanged(BR.option_id);
    }

    public void setSuboptions(ArrayList<SubOptionModel> suboptions) {
        this.suboptions = suboptions;
        this.notifyPropertyChanged(BR.suboptions);
    }
}
