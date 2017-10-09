package com.seuic.app.store.bean;

import java.io.Serializable;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class RecycleTitleBean implements Serializable {
    private String title;

    public RecycleTitleBean(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
