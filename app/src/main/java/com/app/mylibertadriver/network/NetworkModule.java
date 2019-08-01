package com.app.mylibertadriver.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.app.mylibertadriver.BuildConfig;
import com.app.mylibertadriver.activities.LoginActivity;
import com.app.mylibertadriver.activities.MainActivity;
import com.app.mylibertadriver.prefes.MySharedPreference;
import com.app.mylibertadriver.worker.WorkUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
/**
 * Create By Rahul Mangal
 * Project Haute Delivery
 */

@Module
public class NetworkModule {
    File cacheFile;
    Context applicationContext;

    public NetworkModule(Context applicationContext, File cacheFile) {
        this.applicationContext = applicationContext;
        this.cacheFile = cacheFile;
    }

    @Provides
    @Singleton
    Retrofit provideCall() {
        Cache cache = null;
        try {
            cache = new Cache(cacheFile, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder request = original.newBuilder();
                        /*.header("Content-Type", "application/json")*/
                        if (MySharedPreference.getInstance(applicationContext).getSessionToken() != null)
                            request.header("x-auth", MySharedPreference.getInstance(applicationContext).getSessionToken());
                        Request d = request.build();

                        Response response = chain.proceed(d);

                        if (response.code() == 401) {
                            MySharedPreference.getInstance(applicationContext).clearMyPreference();
                            WorkUtils.stopBackgroundService();
                            Intent mIntent = new Intent(applicationContext, LoginActivity.class);
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            applicationContext.startActivity(mIntent);
                            ((Activity) applicationContext).finishAffinity();

                        } else {
                            String newToken = response.header("x-auth");
                            if (newToken != null) {
                                Log.e("TOKEN", "" + newToken);
                                MySharedPreference.getInstance(applicationContext).setSessionToken(newToken);
                            }
                        }
                        response.cacheResponse();
                        response.code();
                        // Customize or return the response
                        return response;
                    }
                })
                .cache(cache)

                .build();


        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public APIInterface providesNetworkService(Retrofit retrofit) {
        return retrofit.create(APIInterface.class);
    }
}
