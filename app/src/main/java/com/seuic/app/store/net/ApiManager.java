package com.seuic.app.store.net;

import com.seuic.app.store.net.okhttp.CustomInterceptor;
import com.seuic.app.store.net.okhttp.OkHttpLogger;
import com.seuic.app.store.utils.AppStoreUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class ApiManager {
    private static ApiManager mApiManager = new ApiManager();

    public static ApiManager getInstance() {
        return mApiManager;
    }

    public ApiService getService() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new OkHttpLogger());
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();

        client = client.newBuilder()
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(new CustomInterceptor())
                .build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(AppStoreUtils.APPSTORE_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return mRetrofit.create(ApiService.class);
    }


}
