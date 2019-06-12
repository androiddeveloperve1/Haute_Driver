package com.foodies.vedriver.network;


import com.foodies.vedriver.constants.UrlConstants;
import com.foodies.vedriver.model.ApiResponseModel;
import com.foodies.vedriver.model.UserModel;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface APIInterface {


    @Headers("Content-Type: application/json")
    @POST(UrlConstants.DRIVER)
    Observable<ApiResponseModel<UserModel>> userRegster(@Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST(UrlConstants.VERIFY_OTP)
    Observable<ApiResponseModel<UserModel>> verifyOTP(@Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST(UrlConstants.RESEND_OTP)
    Observable<ApiResponseModel<UserModel>> resendOTP(@Body HashMap<String, String> body);


    @Headers("Content-Type: application/json")
    @POST(UrlConstants.LOGIN)
    Observable<ApiResponseModel<UserModel>> userLogin(@Body HashMap<String, String> body);

    @Headers("Content-Type: application/json")
    @POST(UrlConstants.RESET_PASS)
    Observable<ApiResponseModel<UserModel>> resetPassword(@Body HashMap<String, String> body);


    @Multipart
    @POST(UrlConstants.UPLOAD_DOC)
    Observable<ApiResponseModel<UserModel>> uploadDoc(@Part MultipartBody.Part body);


}
