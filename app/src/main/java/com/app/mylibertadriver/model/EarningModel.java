package com.app.mylibertadriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class EarningModel extends BaseObservable {
    private String order_no;
    private String delivery_fees;
    private String gratitude_fees;
    private String is_tranfered;
    private String transfered_date;
    private String delivery_date;
    private String _id;
    private String order_id;

    @Bindable
    public String getOrder_no() {
        return order_no;
    }
    @Bindable
    public String getDelivery_fees() {
        return delivery_fees;
    }
    @Bindable
    public String getGratitude_fees() {
        return gratitude_fees;
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
    @Bindable
    public String get_id() {
        return _id;
    }
    @Bindable
    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
        this.notifyPropertyChanged(BR.order_no);
    }

    public void setDelivery_fees(String delivery_fees) {
        this.delivery_fees = delivery_fees;
        this.notifyPropertyChanged(BR.delivery_fees);
    }

    public void setGratitude_fees(String gratitude_fees) {
        this.gratitude_fees = gratitude_fees;
        this.notifyPropertyChanged(BR.gratitude_fees);
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

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
        this.notifyPropertyChanged(BR.order_id);
    }
}
