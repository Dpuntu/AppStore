package com.fmx.dpuntu.api;

import com.fmx.dpuntu.utils.Loger;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class ApiManager {
//    private static ApiManager mApimanager;
    public static final String url = "https://ota.seuic.info/api/";

//    public static ApiManager getApiManager() {
//        if (mApimanager == null) {
//            mApimanager = new ApiManager();
//        }
//        return mApimanager;
//    }

//    public ApiService getApiService() {
//        HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
//        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(3, TimeUnit.SECONDS)
//                .readTimeout(1, TimeUnit.SECONDS)
//                .build();
//
//        client = client.newBuilder()
//                .addInterceptor(new CustomInterceptor())
//                .addNetworkInterceptor(mHttpLoggingInterceptor)
//                .build();
//
//        Retrofit retrofit = new Retrofit
//                .Builder()
//                .baseUrl(url)
//                .client(client)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ApiService mApiService = retrofit.create(ApiService.class);
//
//        return mApiService;
//    }

    public static class HttpLogger implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            Loger.i("okHttp3: " + message);
        }
    }

}
