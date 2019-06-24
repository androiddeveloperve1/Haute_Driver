package com.app.mylibertadriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.model.TaskOrderInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class TaskResponse extends BaseObservable {

    private String _id;
    private String status;
    private String response;
    private String order_id;
    private String order_no;
    private String driver_id;
    private String createdAt;
    private String updatedAt;
    @SerializedName("OrderInfo")
    private TaskOrderInfo orderInfo;

    @Bindable
    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
        this.notifyPropertyChanged(BR.order_no);
    }

    @Bindable
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        this.notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR.updatedAt);
    }

    @Bindable
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(androidx.databinding.library.baseAdapters.BR._id);
    }

    @Bindable
    public TaskOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(TaskOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
        this.notifyPropertyChanged(BR.orderInfo);
    }


    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.notifyPropertyChanged(com.app.mylibertadriver.BR.status);
    }

    @Bindable
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
        this.notifyPropertyChanged(com.app.mylibertadriver.BR.response);
    }

    @Bindable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        this.notifyPropertyChanged(com.app.mylibertadriver.BR.createdAt);
    }

    @Bindable
    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
        this.notifyPropertyChanged(com.app.mylibertadriver.BR.order_id);
    }

    @Bindable
    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
        this.notifyPropertyChanged(com.app.mylibertadriver.BR.driver_id);
    }
}
