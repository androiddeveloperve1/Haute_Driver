package com.app.mylibertadriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.app.mylibertadriver.model.MainOptionModel;

import java.util.ArrayList;


/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class OrderItemModel extends BaseObservable {

    private String quantity;
    private String price;
    private String _id;
    private String description;
    private ItemDataModel item_id;
    private String item_name;
    private ArrayList<MainOptionModel> options;

@Bindable
    public ArrayList<MainOptionModel> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<MainOptionModel> options) {
        this.options = options;
        this.notifyPropertyChanged(BR.options);
    }

    @Bindable
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
        this.notifyPropertyChanged(BR.quantity);
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        this.notifyPropertyChanged(BR.price);
    }

    @Bindable
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.notifyPropertyChanged(BR.description);
    }

    @Bindable
    public ItemDataModel getItem_id() {
        return item_id;
    }

    public void setItem_id(ItemDataModel item_id) {
        this.item_id = item_id;
        this.notifyPropertyChanged(BR.item_id);
    }

    @Bindable
    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
        this.notifyPropertyChanged(BR.item_name);
    }
}
