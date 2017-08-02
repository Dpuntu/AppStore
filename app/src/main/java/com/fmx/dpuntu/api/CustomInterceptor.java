package com.fmx.dpuntu.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class CustomInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
//        okhttp3.Response response = chain.proceed(request);
//        String body = response.body().string();
//        String networkResponse = response.networkResponse().toString();
//        Loger.d("okhttp , networkResponse = " + networkResponse);
//        Loger.d("okhttp , body = " + body);
        return chain.proceed(request);
    }
}
