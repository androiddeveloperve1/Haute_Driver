package com.foodies.vedriver.network;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.foodies.vedriver.BuildConfig;
import com.foodies.vedriver.constants.UrlConstants;
import com.foodies.vedriver.prefes.MySharedPreference;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


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
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder request = original.newBuilder();
                        /*.header("Content-Type", "application/json")*/
                        if (MySharedPreference.getInstance(applicationContext).getSessionToken() != null)
                            request.header("x-auth", MySharedPreference.getInstance(applicationContext).getSessionToken());
                        Request d = request.build();

                        okhttp3.Response response = chain.proceed(d);
                        Log.e("Request", d.url().toString());
                        Log.e("params", new Gson().toJson(d.body()));
                        String newToken = response.header("x-auth");
                        if (newToken != null) {
                            Log.e("TOKEN", "" + newToken);
                            MySharedPreference.getInstance(applicationContext).setSessionToken(newToken);
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
