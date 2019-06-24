package com.app.mylibertadriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.model.TaskOrderInfo;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project Haute Driver
 */

public class TaskModelResponse extends BaseObservable {

    private ArrayList<TaskResponse> list;

    @Bindable
    public ArrayList<TaskResponse> getList() {
        return list;
    }

    public void setList(ArrayList<TaskResponse> list) {
        this.list = list;
        this.notifyPropertyChanged(BR.list);
    }
}
