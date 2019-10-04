package com.app.mylibertadriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class EarningModelResponse extends BaseObservable {

    private ArrayList<EarningModel> earningDetails;
    private String totalDeliveryFeeEarning;
    private String totalGratitudeEarning;

    @Bindable
    public ArrayList<EarningModel> getEarningDetails() {
        return earningDetails;
    }

    @Bindable
    public String getTotalDeliveryFeeEarning() {
        return totalDeliveryFeeEarning;
    }

    @Bindable
    public String getTotalGratitudeEarning() {
        return totalGratitudeEarning;
    }

    public void setEarningDetails(ArrayList<EarningModel> earningDetails) {
        this.earningDetails = earningDetails;
        this.notifyPropertyChanged(BR.earningDetails);
    }

    public void setTotalDeliveryFeeEarning(String totalDeliveryFeeEarning) {
        this.totalDeliveryFeeEarning = totalDeliveryFeeEarning;
        this.notifyPropertyChanged(BR.totalDeliveryFeeEarning);
    }

    public void setTotalGratitudeEarning(String totalGratitudeEarning) {
        this.totalGratitudeEarning = totalGratitudeEarning;
        this.notifyPropertyChanged(BR.totalGratitudeEarning);
    }
}
