package com.app.mylibertadriver.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.model.orders.OrderItemModel;
import com.app.mylibertadriver.model.orders.RestaurantInfoModel;

import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class TaskOrderInfo extends BaseObservable {

    private String _id;
    private String amount;
    private String deliveryfee;
    private String totalamount;
    private String payment_type;
    private String delivery_status;
    private String is_driver_assign;
    private String order_no;
    private String restaurant_id;
    private String user_id;
    private String address_id;
    //private ArrayList<OrderItemModel> order;
    private UserModel userInfo;
    private RestaurantInfoModel restaurantInfo;

    private String distance="Getting distance.....";

    @Bindable
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
        this.notifyPropertyChanged(BR._id);
    }

    @Bindable
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
        this.notifyPropertyChanged(BR.amount);
    }

    @Bindable
    public String getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(String deliveryfee) {
        this.deliveryfee = deliveryfee;
        this.notifyPropertyChanged(BR.deliveryfee);
    }

    @Bindable
    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
        this.notifyPropertyChanged(BR.totalamount);
    }

    @Bindable
    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
        this.notifyPropertyChanged(BR.payment_type);
    }

    @Bindable
    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
        this.notifyPropertyChanged(BR.delivery_status);
    }

    @Bindable
    public String getIs_driver_assign() {
        return is_driver_assign;
    }

    public void setIs_driver_assign(String is_driver_assign) {
        this.is_driver_assign = is_driver_assign;
        this.notifyPropertyChanged(BR.is_driver_assign);
    }

    @Bindable
    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
        this.notifyPropertyChanged(BR.order_no);
    }

    @Bindable
    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
        this.notifyPropertyChanged(BR.restaurant_id);
    }

    @Bindable
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
        this.notifyPropertyChanged(BR.user_id);
    }

    @Bindable
    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
        this.notifyPropertyChanged(BR.address_id);
    }

    /*@Bindable
    public ArrayList<OrderItemModel> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<OrderItemModel> order) {
        this.order = order;
        this.notifyPropertyChanged(BR.order);
    }*/

    @Bindable
    public UserModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserModel userInfo) {
        this.userInfo = userInfo;
        this.notifyPropertyChanged(BR.userInfo);
    }

    @Bindable
    public RestaurantInfoModel getRestaurantInfo() {
        return restaurantInfo;
    }

    public void setRestaurantInfo(RestaurantInfoModel restaurantInfo) {
        this.restaurantInfo = restaurantInfo;
        this.notifyPropertyChanged(BR.restaurantInfo);
    }

    @Bindable
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
        this.notifyPropertyChanged(BR.distance);
    }
}
