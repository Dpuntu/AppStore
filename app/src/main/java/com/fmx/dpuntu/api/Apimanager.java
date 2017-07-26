package com.fmx.dpuntu.api;

import com.fmx.dpuntu.utils.Loger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class ApiManager {
    private static ApiManager mApimanager;
    private static final String url = "https://ota.seuic.info/api/";

    public static ApiManager getApiManager() {
        if (mApimanager == null) {
            mApimanager = new ApiManager();
        }
        return mApimanager;
    }

    public ApiService getApiService() {
        HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoger());
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .build();

        client = client.newBuilder()
                .addNetworkInterceptor(mHttpLoggingInterceptor)
//                .addInterceptor(new CustomInterceptor())
                .build();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService mApiService = retrofit.create(ApiService.class);

        return mApiService;
    }

    public class HttpLoger implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            Loger.d("Okhttp: " + message);
        }
    }

}
