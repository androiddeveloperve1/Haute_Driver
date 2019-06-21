package com.app.mylibertadriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class ItemDataModel extends BaseObservable {

    private String _id;
    private String name;
    private ItemInfoModel itemInfo;


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
    public ItemInfoModel getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(ItemInfoModel itemInfo) {
        this.itemInfo = itemInfo;
        this.notifyPropertyChanged(BR.itemInfo);
    }
}
