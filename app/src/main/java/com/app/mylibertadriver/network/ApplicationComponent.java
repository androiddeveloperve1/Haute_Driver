package com.app.mylibertadriver.network;


import com.app.mylibertadriver.activities.AcceptOrderActivity;
import com.app.mylibertadriver.activities.ForgotPasswordActivity;
import com.app.mylibertadriver.activities.MainActivity;
import com.app.mylibertadriver.activities.ReachedRestaurantActivty;
import com.app.mylibertadriver.activities.SplashActivity;
import com.app.mylibertadriver.activities.TrackingActivity;
import com.app.mylibertadriver.fragments.FragmentProfile;
import com.app.mylibertadriver.fragments.FragmentTasks;
import com.app.mylibertadriver.viewmodeles.LoginViewModel;
import com.app.mylibertadriver.viewmodeles.OtpVerifyViewModel;
import com.app.mylibertadriver.viewmodeles.ResetPassViewModel;
import com.app.mylibertadriver.viewmodeles.SignupViewModel;
import com.app.mylibertadriver.viewmodeles.UploadDocViewModel;
import com.app.mylibertadriver.worker.DriverLocationUpdateService;

import javax.inject.Singleton;

import dagger.Component;
/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */
@Singleton
@Component(modules = {NetworkModule.class})
public interface ApplicationComponent {
    void inject(SignupViewModel model);
    void inject(OtpVerifyViewModel model);
    void inject(ForgotPasswordActivity model);
    void inject(LoginViewModel model);
    void inject(UploadDocViewModel model);
    void inject(ResetPassViewModel model);
    void inject(SplashActivity model);
    void inject(FragmentProfile model);
    void inject(MainActivity model);
    void inject(FragmentTasks model);
    void inject(TrackingActivity model);
    void inject(DriverLocationUpdateService model);
    void inject(AcceptOrderActivity model);
    void inject(ReachedRestaurantActivty model);

}
