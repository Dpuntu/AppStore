package com.seuic.app.store.bean;

import java.io.Serializable;

/**
 * Created on 2017/9/26.
 *
 * @author dpuntu
 */

public class RecycleSearchBean implements Serializable {
    private String appName;
    private boolean isShowDelete;
    private String appTime;

    public RecycleSearchBean(String appName, boolean isShowDelete, String appTime) {
        this.appName = appName;
        this.isShowDelete = isShowDelete;
        this.appTime = appTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isShowDelete() {
        return isShowDelete;
    }

    public void setShowDelete(boolean showDelete) {
        isShowDelete = showDelete;
    }

    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(String appTime) {
        this.appTime = appTime;
    }
}
