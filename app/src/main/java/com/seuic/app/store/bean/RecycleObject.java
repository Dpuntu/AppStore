package com.seuic.app.store.bean;

import java.io.Serializable;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class RecycleObject<T> implements Serializable {
    private int type;
    private T t;

    public RecycleObject() {
    }

    public RecycleObject(int type, T t) {
        this.type = type;
        this.t = t;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getObject() {
        return t;
    }

    public void setObject(T t) {
        this.t = t;
    }
}
