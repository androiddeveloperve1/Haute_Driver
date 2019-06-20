package com.app.mylibertadriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class OrderItemModel extends BaseObservable {

    private String quantity;
    private String price;
    private String item_type;
    private String _id;
    private String item_id;
    private ArrayList<ItemInfoModel> itemInfo;
    private ArrayList<ItemDataModel> itemData;


    @Bindable
    public String getQuantity() {
        return quantity;
    }
    @Bindable
    public String getPrice() {
        return price;
    }
    @Bindable
    public String getItem_type() {
        return item_type;
    }
    @Bindable
    public String get_id() {
        return _id;
    }
    @Bindable
    public String getItem_id() {
        return item_id;
    }
    @Bindable
    public ArrayList<ItemInfoModel> getItemInfo() {
        return itemInfo;
    }
    @Bindable
    public ArrayList<ItemDataModel> getItemData() {
        return itemData;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
        this.notifyPropertyChanged(BR.quantity);
    }

    public void setPrice(String price) {
        this.price = price;
        this.notifyPropertyChanged(BR.price);
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
        this.notifyPropertyChanged(BR.item_type);
    }

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
        this.notifyPropertyChanged(BR.item_id);
    }

    public void setItemInfo(ArrayList<ItemInfoModel> itemInfo) {
        this.itemInfo = itemInfo;
        this.notifyPropertyChanged(BR.itemInfo);
    }

    public void setItemData(ArrayList<ItemDataModel> itemData) {
        this.itemData = itemData;
        this.notifyPropertyChanged(BR.itemData);
    }
}
