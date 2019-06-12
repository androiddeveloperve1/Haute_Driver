package com.foodies.vedriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.foodies.vedriver.BR;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class UserModel extends BaseObservable {


    private String _id;
    private String employee_id;
    private Docs driverlicense;
    private Docs insurance;


    @Bindable
    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
        this.notifyPropertyChanged(BR.employee_id);
    }

    @Bindable
    public Docs getDriverlicense() {
        return driverlicense;
    }

    public void setDriverlicense(Docs driverlicense) {
        this.driverlicense = driverlicense;
        this.notifyPropertyChanged(BR.driverlicense);
    }

    @Bindable
    public Docs getInsurance() {
        return insurance;
    }

    public void setInsurance(Docs insurance) {
        this.insurance = insurance;
        this.notifyPropertyChanged(BR.insurance);
    }

    private String is_mobile_verify;
    private String is_document_verify;
    private String fname;
    private String country_code;
    private String lname;
    private String mobile_no;
    private String service_area;
    private Location location;
    private String email;

    @Bindable
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
    }

    @Bindable
    public String getIs_mobile_verify() {
        return is_mobile_verify;
    }

    public void setIs_mobile_verify(String is_mobile_verify) {
        this.is_mobile_verify = is_mobile_verify;
        this.notifyPropertyChanged(BR.is_mobile_verify);
    }

    @Bindable
    public String getIs_document_verify() {
        return is_document_verify;
    }

    public void setIs_document_verify(String is_document_verify) {
        this.is_document_verify = is_document_verify;
        this.notifyPropertyChanged(BR.is_document_verify);
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
    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
        this.notifyPropertyChanged(BR.country_code);
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
    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
        this.notifyPropertyChanged(BR.mobile_no);
    }

    @Bindable
    public String getService_area() {
        return service_area;
    }

    public void setService_area(String service_area) {
        this.service_area = service_area;
        this.notifyPropertyChanged(BR.service_area);
    }

    @Bindable
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        this.notifyPropertyChanged(BR.location);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.notifyPropertyChanged(BR.email);
    }
}
