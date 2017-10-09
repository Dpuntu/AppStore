package com.seuic.app.store.bean.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class ResponseData<T> implements Serializable {
    @SerializedName("result")
    private boolean result;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private T data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
