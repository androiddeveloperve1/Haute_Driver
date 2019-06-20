package com.app.mylibertadriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class ApiResponseModel<T> extends BaseObservable {
    private String message;

    @SerializedName("Status")
    private String status;

    private T data;


    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.notifyPropertyChanged(BR.message);
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.notifyPropertyChanged(BR.status);

    }

    @Bindable
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        this.notifyPropertyChanged(BR.data);
    }
}
