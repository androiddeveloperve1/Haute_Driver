package com.app.mylibertadriver.network;


import com.app.mylibertadriver.constants.UrlConstants;
import com.app.mylibertadriver.model.ApiResponseModel;
import com.app.mylibertadriver.model.DriverModel;
import com.app.mylibertadriver.model.orders.OrderDetailsModel;
import com.app.mylibertadriver.model.orders.TaskModel;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

public interface APIInterface {


    @Headers("Content-Type: application/json")
    @POST(UrlConstants.DRIVER)
    Observable<ApiResponseModel<DriverModel>> userRegster(@Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST(UrlConstants.VERIFY_OTP)
    Observable<ApiResponseModel<DriverModel>> verifyOTP(@Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST(UrlConstants.RESEND_OTP)
    Observable<ApiResponseModel<DriverModel>> resendOTP(@Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST(UrlConstants.LOGIN)
    Observable<ApiResponseModel<DriverModel>> userLogin(@Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST(UrlConstants.RESET_PASS)
    Observable<ApiResponseModel<DriverModel>> resetPassword(@Body HashMap<String, String> body);


    @Multipart
    @POST(UrlConstants.UPLOAD_DOC)
    Observable<ApiResponseModel<DriverModel>> uploadDoc(@Part MultipartBody.Part[] body);


    @Multipart
    @POST(UrlConstants.UPLOAD_PROFILE)
    Observable<ApiResponseModel<DriverModel>> uploadProfile(@Part MultipartBody.Part body);


    @GET(UrlConstants.LOGOUT)
    Observable<ApiResponseModel> doLogout();


    @GET(UrlConstants.PROFILE)
    Observable<ApiResponseModel<DriverModel>> getUserProfile();


    @GET(UrlConstants.ORDER_DETAIL + "{id}")
    Observable<ApiResponseModel<OrderDetailsModel>> getOrderDetails(@Path("id") String orderId);


    @Headers("Content-Type: application/json")
    @POST(UrlConstants.UPDATE_DRIVER_LOCATION)
    Observable<ApiResponseModel> updateDriverLocation(@Body HashMap<String, String> body);


    @GET(UrlConstants.TASK_DETAILS)
    Observable<ApiResponseModel<TaskModel>> getTaskDetails();


    @GET(UrlConstants.ACCEPT_ORDER + "{id}")
    Observable<ApiResponseModel> acceptOrder(@Path("id") String orderId);


}
