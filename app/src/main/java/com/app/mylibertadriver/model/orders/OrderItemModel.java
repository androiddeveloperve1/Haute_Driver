package com.app.mylibertadriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.model.AttributeModel;

import org.w3c.dom.Attr;

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
    private ItemDataModel item_id;
    private ArrayList<AttributeModel> attribute;
@Bindable
    public ArrayList<AttributeModel> getAttribute() {
        return attribute;
    }

    public void setAttribute(ArrayList<AttributeModel> attribute) {
        this.attribute = attribute;
        this.notifyPropertyChanged(BR.attribute);
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
    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
        this.notifyPropertyChanged(BR.item_type);
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
    public ItemDataModel getItem_id() {
        return item_id;
    }

    public void setItem_id(ItemDataModel item_id) {
        this.item_id = item_id;
        this.notifyPropertyChanged(BR.item_id);
    }


}
