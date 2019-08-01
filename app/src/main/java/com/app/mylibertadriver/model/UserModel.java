package com.app.mylibertadriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class UserModel extends BaseObservable {
    private String _id;
    private String fname;
    private String lname;
    private String name;
    private String email;
    private String avatar;
    private String mobile_no;
    private String country_code;
    private ArrayList<DeliveryAddress> delivery_address;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.notifyPropertyChanged(BR.name);
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
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
        this.notifyPropertyChanged(BR.fname);
    }

    @Bindable
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
        this.notifyPropertyChanged(BR.lname);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        this.notifyPropertyChanged(BR.avatar);
    }

    @Bindable
    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
        this.notifyPropertyChanged(BR.mobile_no);
    }

    @Bindable
    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
        this.notifyPropertyChanged(BR.country_code);
    }

    @Bindable
    public ArrayList<DeliveryAddress> getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(ArrayList<DeliveryAddress> delivery_address) {
        this.delivery_address = delivery_address;
        this.notifyPropertyChanged(BR.delivery_address);
    }
}




