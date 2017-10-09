package com.fmx.dpuntu.component;

import com.fmx.dpuntu.api.ApiManager;
import com.fmx.dpuntu.api.ApiService;
import com.fmx.dpuntu.api.CustomInterceptor;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 2017/8/2.
 *
 * @author dpuntu
 */
@Module
public class ApiModule {
    private ApiService mApiService;

    @Provides
    public ApiService provideApiService() {
        HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor(new ApiManager.HttpLogger());
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .build();

        client = client.newBuilder()
                .addInterceptor(new CustomInterceptor())
                .addNetworkInterceptor(mHttpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(ApiManager.url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiService = retrofit.create(ApiService.class);

        return mApiService;
    }
}
