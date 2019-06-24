package com.app.mylibertadriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.model.Location;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class RestaurantInfoModel extends BaseObservable {
    private String name;
    private String address;
    private ArrayList<String> images;
    private String _id;
    private Location location;
    private String deliveryfees;
    private String deliverykm;
    private String contact_no;
    private String rating;
    private String is_approved;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDeliveryfees() {
        return deliveryfees;
    }

    public void setDeliveryfees(String deliveryfees) {
        this.deliveryfees = deliveryfees;
    }

    public String getDeliverykm() {
        return deliverykm;
    }

    public void setDeliverykm(String deliverykm) {
        this.deliverykm = deliverykm;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
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
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        this.notifyPropertyChanged(BR.address);
    }

    @Bindable
    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
        this.notifyPropertyChanged(BR.images);
    }
}
