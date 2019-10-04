package com.app.mylibertadriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class WeeklyEarningModel extends BaseObservable {

    private String startDate;
    private String endDate;
    private String wallet;
    private String is_tranfered;
    private String transfered_date;
    private String delivery_date;

    @Bindable
    public String getStartDate() {
        return startDate;
    }

    @Bindable
    public String getEndDate() {
        return endDate;
    }

    @Bindable
    public String getWallet() {
        return wallet;
    }

    @Bindable
    public String getIs_tranfered() {
        return is_tranfered;
    }

    @Bindable
    public String getTransfered_date() {
        return transfered_date;
    }

    @Bindable
    public String getDelivery_date() {
        return delivery_date;
    }


    public void setStartDate(String startDate) {
        this.startDate = startDate;
        this.notifyPropertyChanged(BR.startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        this.notifyPropertyChanged(BR.endDate);
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
        this.notifyPropertyChanged(BR.wallet);
    }

    public void setIs_tranfered(String is_tranfered) {
        this.is_tranfered = is_tranfered;
        this.notifyPropertyChanged(BR.is_tranfered);
    }

    public void setTransfered_date(String transfered_date) {
        this.transfered_date = transfered_date;
        this.notifyPropertyChanged(BR.transfered_date);
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
        this.notifyPropertyChanged(BR.delivery_date);
    }
}
