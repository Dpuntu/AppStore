package com.seuic.app.store.utils;

import com.bumptech.glide.load.engine.GlideException;
import com.google.gson.Gson;
import com.seuic.app.store.bean.response.ResponseData;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created on 2017/9/25.
 *
 * @author dpuntu
 */

public class RxUtils {
    /**
     * rx对异常的处理
     */
    public static <T> Observable<ResponseData<T>> onErrorInterceptor(Observable<ResponseData<T>> observable) {
        return observable.onErrorReturn(new Function<Throwable, ResponseData<T>>() {
            @Override
            public ResponseData<T> apply(@NonNull Throwable throwable) throws Exception {
                ResponseData<T> responseData = new ResponseData<>();
                responseData.setResult(false);
                if (throwable instanceof UnknownHostException) {
                    responseData.setMsg("网络异常 , 请检查网络是否正常连接或请求地址是否正确!");
                } else if (throwable instanceof HttpException) {
                    int code = ((HttpException) throwable).response().code();
                    if (code == 403) {
                        responseData.setMsg("403 , 请开发者检查请求头是否配置正确!");
                    } else if (code == 404) {
                        responseData.setMsg("404 , 未知请求, 请检查请求地址!");
                    } else if (code == 200) {
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        try {
                            String resBody = responseBody.string();
                            Gson gson = new Gson();
                            ResponseData receiveData = gson.fromJson(resBody, ResponseData.class);
                            responseData.setResult(true);
                            responseData.setMsg(receiveData.getMsg());
                        } catch (IOException e) {
                            responseData.setMsg("请求异常 , 请稍候再尝试!");
                        }
                    } else {
                        responseData.setMsg(code + " , 未知错误!");
                    }
                } else if (throwable instanceof SocketTimeoutException) {
                    responseData.setMsg("连接超时 , 请检查网络是否可以正常访问网络!");
                } else if (throwable instanceof GlideException) {
                    responseData.setMsg("网络异常 , 请检查网络是否正常连接!");
                } else {
                    responseData.setMsg(throwable.getMessage());
                }
                return responseData;
            }
        });
    }

    /**
     * Rx返回结果处理
     */
    public static abstract class ResponseObserver<T> implements Observer<ResponseData<T>> {
        private Disposable mDisposable;
        private String observeName;

        public ResponseObserver(String observeName) {
            this.observeName = observeName;
        }

        @Override
        public void onSubscribe(@NonNull Disposable disposable) {
            mDisposable = disposable;
        }

        @Override
        public void onNext(@NonNull ResponseData<T> data) {
            if (data.isResult() && data.getData() != null) {
                Loger.e(observeName + " , " + data.getMsg());
                onSuccess(data.getData());
            } else {
                onError(observeName + " , " + data.getMsg());
            }
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            onError(android.util.Log.getStackTraceString(throwable));
            onComplete();
        }

        @Override
        public void onComplete() {
            Loger.d(observeName + " onComplete !");
            if (mDisposable != null) {
                mDisposable.dispose();
            }
        }

        public abstract void onSuccess(T t);

        public abstract void onError(String errorMsg);
    }
}
