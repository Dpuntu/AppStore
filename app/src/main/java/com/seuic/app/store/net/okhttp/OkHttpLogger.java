package com.seuic.app.store.net.okhttp;

import com.seuic.app.store.utils.CharsetUtils;
import com.seuic.app.store.utils.Logger;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class OkHttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String str) {
        try {
            Logger.i("okhttp: " + CharsetUtils.unicode2Utf8(str));
        } catch (Exception e) {
            Logger.e("okhttp: OkhttpLogger error :" + e.getMessage());
            Logger.i("okhttp: " + str);
        }
    }
}
