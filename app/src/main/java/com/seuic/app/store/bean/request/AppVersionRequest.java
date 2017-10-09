package com.seuic.app.store.bean.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 2017/9/22.
 *
 * @author dpuntu
 */

public class AppVersionRequest implements Serializable {
    @SerializedName("app_package_name")
    String appPackageName;
    @SerializedName("current_version")
    String currentVersion;

    public AppVersionRequest(String appPackageName, String currentVersion) {
        this.appPackageName = appPackageName;
        this.currentVersion = currentVersion;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }
}
