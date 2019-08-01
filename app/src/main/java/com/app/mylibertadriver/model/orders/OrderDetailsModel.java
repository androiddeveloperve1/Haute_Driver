package com.app.mylibertadriver.model.orders;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.app.mylibertadriver.BR;
import com.app.mylibertadriver.model.UserModel;
import java.util.ArrayList;

/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

public class OrderDetailsModel extends BaseObservable {
    private UserModel user_id;
    private RestaurantInfoModel restaurant_id;
    private ArrayList<OrderItemModel> order;
    private String _id;
    private String amount;
    private String deliveryfee;
    private String totalamount;
    private String payment_type;
    private String promo_code;
    private String stripe_charge_id;
    private String address_id;
    private String createdAt;
    private String updatedAt;
    private String address_type;
    private String delivery_status;
    private String distance = "Getting distance.....";
    private String order_no;

    private String travelTime = "0.0";


    @Bindable
    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {

        this.travelTime = travelTime;
        this.notifyPropertyChanged(BR.travelTime);
    }

    @Bindable
    public UserModel getUser_id() {
        return user_id;
    }

    public void setUser_id(UserModel user_id) {
        this.user_id = user_id;
        this.notifyPropertyChanged(BR.user_id);
    }

    @Bindable
    public RestaurantInfoModel getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(RestaurantInfoModel restaurant_id) {
        this.restaurant_id = restaurant_id;
        this.notifyPropertyChanged(BR.restaurant_id);
    }

    @Bindable
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
        this.notifyPropertyChanged(BR.distance);
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
    public RestaurantInfoModel getRestaurantInfo() {
        return restaurant_id;
    }

    public void setRestaurantInfo(RestaurantInfoModel restaurantInfo) {
        this.restaurant_id = restaurantInfo;
        this.notifyPropertyChanged(BR.userInfo);
    }

    @Bindable
    public ArrayList<OrderItemModel> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<OrderItemModel> order) {
        this.order = order;
        this.notifyPropertyChanged(BR.order);
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
    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
        this.notifyPropertyChanged(BR.promo_code);
    }

    @Bindable
    public String getStripe_charge_id() {
        return stripe_charge_id;
    }

    public void setStripe_charge_id(String stripe_charge_id) {
        this.stripe_charge_id = stripe_charge_id;
        this.notifyPropertyChanged(BR.stripe_charge_id);
    }

    @Bindable
    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
        this.notifyPropertyChanged(BR.address_id);
    }

    @Bindable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        this.notifyPropertyChanged(BR.createdAt);
    }

    @Bindable
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        this.notifyPropertyChanged(BR.updatedAt);
    }

    @Bindable
    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
        this.notifyPropertyChanged(BR.address_type);
    }

    @Bindable
    public String getDelivery_status() {
        return delivery_status;

    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
        this.notifyPropertyChanged(BR.delivery_status);
    }
}



