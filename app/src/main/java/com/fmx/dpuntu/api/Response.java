package com.fmx.dpuntu.api;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class Response<T> implements Serializable {
    @SerializedName("result")
    private String result;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
