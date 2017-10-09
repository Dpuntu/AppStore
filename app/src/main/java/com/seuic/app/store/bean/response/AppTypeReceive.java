package com.seuic.app.store.bean.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017/9/22.
 *
 * @author dpuntu
 */

public class AppTypeReceive implements Serializable {
    @SerializedName("app_type")
    String appType;
    @SerializedName("app_type_id")
    String appTypeId;
    @SerializedName("apps")
    List<RecommendReceive> apps;

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(String appTypeId) {
        this.appTypeId = appTypeId;
    }

    public List<RecommendReceive> getApps() {
        return apps;
    }

    public void setApps(List<RecommendReceive> apps) {
        this.apps = apps;
    }
}
