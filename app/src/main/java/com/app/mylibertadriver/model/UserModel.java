package com.app.mylibertadriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class UserModel  extends BaseObservable{
    private String _id;
    private String fname;
    private String lname;
    private String email;
    private String avatar;
    private String mobile_no;
    private String country_code;
    private DeliveryAddress delivery_address;

    @Bindable
    public String get_id() {
        return _id;
    }

    @Bindable
    public String getFname() {
        return fname;
    }

    @Bindable
    public String getLname() {
        return lname;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    @Bindable
    public String getMobile_no() {
        return mobile_no;
    }

    @Bindable
    public String getCountry_code() {
        return country_code;
    }

    @Bindable
    public DeliveryAddress getDelivery_address() {
        return delivery_address;
    }

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
    }

    public void setFname(String fname) {
        this.fname = fname;
        this.notifyPropertyChanged(BR.fname);
    }

    public void setLname(String lname) {
        this.lname = lname;
        this.notifyPropertyChanged(BR.lname);
    }

    public void setEmail(String email) {
        this.email = email;
        this.notifyPropertyChanged(BR.email);
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        this.notifyPropertyChanged(BR.avatar);
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
        this.notifyPropertyChanged(BR.mobile_no);
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
        this.notifyPropertyChanged(BR.country_code);
    }

    public void setDelivery_address(DeliveryAddress delivery_address) {
        this.delivery_address = delivery_address;
        this.notifyPropertyChanged(BR.delivery_address);
    }
}



