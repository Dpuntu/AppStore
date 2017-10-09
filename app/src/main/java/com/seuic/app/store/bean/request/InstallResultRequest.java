package com.seuic.app.store.bean.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 2017/9/27.
 *
 * @author dpuntu
 */

public class InstallResultRequest implements Serializable {
    @SerializedName("app_package_name")
    String appPackageName;
    @SerializedName("app_version_id")
    String appVersionId;

    public InstallResultRequest(String appPackageName, String appVersionId) {
        this.appPackageName = appPackageName;
        this.appVersionId = appVersionId;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getAppVersionId() {
        return appVersionId;
    }

    public void setAppVersionId(String appVersionId) {
        this.appVersionId = appVersionId;
    }
}
