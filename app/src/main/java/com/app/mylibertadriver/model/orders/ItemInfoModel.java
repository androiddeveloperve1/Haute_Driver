package com.app.mylibertadriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class ItemInfoModel extends BaseObservable {


    private String _id;
    private String price_devide;
    private String half_price;
    private String image;
    private String food_type;

    private String is_available;
    private String item_id;
    private String restaurent_id;
    private String category_id;
    private String full_price;

    private String attribute_id;
    private String description;
    private String is_delete;
    private String createdAt;
    private String updatedAt;
    private String __v;

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
    }

    public void setPrice_devide(String price_devide) {
        this.price_devide = price_devide;
        this.notifyPropertyChanged(BR.price_devide);
    }

    public void setHalf_price(String half_price) {
        this.half_price = half_price;
        this.notifyPropertyChanged(BR.half_price);
    }

    public void setImage(String image) {
        this.image = image;
        this.notifyPropertyChanged(BR.image);
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
        this.notifyPropertyChanged(BR.food_type);
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
        this.notifyPropertyChanged(BR.is_available);
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
        this.notifyPropertyChanged(BR.item_id);
    }

    public void setRestaurent_id(String restaurent_id) {
        this.restaurent_id = restaurent_id;
        this.notifyPropertyChanged(BR.restaurent_id);
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
        this.notifyPropertyChanged(BR.category_id);
    }

    public void setFull_price(String full_price) {
        this.full_price = full_price;
        this.notifyPropertyChanged(BR.full_price);
    }

    public void setAttribute_id(String attribute_id) {
        this.attribute_id = attribute_id;
        this.notifyPropertyChanged(BR.attribute_id);
    }

    public void setDescription(String description) {
        this.description = description;
        this.notifyPropertyChanged(BR.description);
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
        this.notifyPropertyChanged(BR.is_delete);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        this.notifyPropertyChanged(BR.createdAt);
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        this.notifyPropertyChanged(BR.updatedAt);
    }

    public void set__v(String __v) {
        this.__v = __v;
        this.notifyPropertyChanged(BR.__v);
    }

    @Bindable
    public String get_id() {
        return _id;
    }

    @Bindable
    public String getPrice_devide() {
        return price_devide;
    }

    @Bindable
    public String getHalf_price() {
        return half_price;
    }

    @Bindable
    public String getImage() {
        return image;
    }

    @Bindable
    public String getFood_type() {
        return food_type;
    }

    @Bindable
    public String getIs_available() {
        return is_available;
    }

    @Bindable
    public String getItem_id() {
        return item_id;
    }

    @Bindable
    public String getRestaurent_id() {
        return restaurent_id;
    }

    @Bindable
    public String getCategory_id() {
        return category_id;
    }

    @Bindable
    public String getFull_price() {
        return full_price;
    }

    @Bindable
    public String getAttribute_id() {
        return attribute_id;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    @Bindable
    public String getIs_delete() {
        return is_delete;
    }

    @Bindable
    public String getCreatedAt() {
        return createdAt;
    }

    @Bindable
    public String getUpdatedAt() {
        return updatedAt;
    }

    @Bindable
    public String get__v() {
        return __v;
    }
}
