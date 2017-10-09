package com.seuic.app.store.net.okhttp;

import com.seuic.app.store.utils.CharsetUtils;
import com.seuic.app.store.utils.Loger;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class OkhttpLoger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String str) {
        try {
            Loger.i("okhttp: " + CharsetUtils.unicode2Utf8(str));
        } catch (Exception e) {
            Loger.e("okhttp: OkhttpLogger error :" + e.getMessage());
            Loger.i("okhttp: " + str);
        }
    }
}
