package com.app.mylibertadriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.app.mylibertadriver.model.TaskOrderInfo;
import com.google.android.gms.tasks.Task;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class TaskModel extends BaseObservable {
    private String _id;
    private String status;
    private String response;
    private String order_id;
    private String driver_id;
    private String createdAt;
    private TaskOrderInfo orderInfo;

    @Bindable
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
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

    @Bindable
    public String getResponse() {
        return response;
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

    @Bindable
    public String getDriver_id() {
        return driver_id;
    }

    public void setStatus(String status) {
        this.status = status;
        this.notifyPropertyChanged(com.app.mylibertadriver.BR.status);
    }

    public void setResponse(String response) {
        this.response = response;
        this.notifyPropertyChanged(com.app.mylibertadriver.BR.response);
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
        this.notifyPropertyChanged(com.app.mylibertadriver.BR.order_id);
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
        this.notifyPropertyChanged(com.app.mylibertadriver.BR.driver_id);
    }
}
