package com.foodies.vedriver.network;


import com.foodies.vedriver.activities.ForgotPasswordActivity;
import com.foodies.vedriver.activities.MainActivity;
import com.foodies.vedriver.activities.SplashActivity;
import com.foodies.vedriver.fragments.FragmentProfile;
import com.foodies.vedriver.viewmodeles.LoginViewModel;
import com.foodies.vedriver.viewmodeles.OtpVerifyViewModel;
import com.foodies.vedriver.viewmodeles.ResetPassViewModel;
import com.foodies.vedriver.viewmodeles.SignupViewModel;
import com.foodies.vedriver.viewmodeles.UploadDocViewModel;

import javax.inject.Singleton;

import dagger.Component;

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

}
