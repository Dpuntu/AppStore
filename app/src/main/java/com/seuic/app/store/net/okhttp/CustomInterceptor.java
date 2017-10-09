package com.seuic.app.store.net.okhttp;


import com.seuic.app.store.utils.HttpHeadUtils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 2017/9/21.
 *
 * @author dpuntu
 */

public class CustomInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        HashMap<String, String> headMap = HttpHeadUtils.getHeadMap();
        Request request = chain.request()
                .newBuilder()
                .addHeader(HttpHeadUtils.HEAD_TIME, headMap.get(HttpHeadUtils.HEAD_TIME))
                .addHeader(HttpHeadUtils.HEAD_PRS, headMap.get(HttpHeadUtils.HEAD_PRS))
                .addHeader(HttpHeadUtils.HEAD_CHANNEL, headMap.get(HttpHeadUtils.HEAD_CHANNEL))
                .addHeader(HttpHeadUtils.HEAD_RANDOM, headMap.get(HttpHeadUtils.HEAD_RANDOM))
                .addHeader(HttpHeadUtils.HEAD_SIGN, headMap.get(HttpHeadUtils.HEAD_SIGN))
                .addHeader(HttpHeadUtils.HEAD_SN, headMap.get(HttpHeadUtils.HEAD_SN))
                /**
                 * okhttp bug异常，请求过程中偶尔会出现IOEx
                 * */
                .addHeader(HttpHeadUtils.HEAD_CONNECTION, headMap.get(HttpHeadUtils.HEAD_CONNECTION))
                .build();

        return chain.proceed(request);
    }
}
