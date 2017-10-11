package com.seuic.app.store.bean.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 2017/9/22.
 *
 * @author dpuntu
 */

public class AppDetailReceive implements Serializable {
    @SerializedName("app_name")
    String appName;
    @SerializedName("app_package_name")
    String packageName;
    @SerializedName("app_size")
    String appSize;
    @SerializedName("app_version")
    String appVersion;
    @SerializedName("app_version_id")
    String appVersionId;
    @SerializedName("getApp_version_desc")
    String appVersionIdDesc;
    @SerializedName("app_desc")
    String appDesc;
    @SerializedName("app_md5")
    String MD5;
    @SerializedName("download_url")
    String downloadName;
    @SerializedName("app_icon")
    String appIconName;
    @SerializedName("app_shot1")
    String appShot1;
    @SerializedName("app_shot2")
    String appShot2;
    @SerializedName("app_shot3")
    String appShot3;
    @SerializedName("app_shot4")
    String appShot4;
    @SerializedName("app_shot5")
    String appShot5;
    @SerializedName("types")
    String appTypes;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppVersionId() {
        return appVersionId;
    }

    public void setAppVersionId(String appVersionId) {
        this.appVersionId = appVersionId;
    }

    public String getAppVersionIdDesc() {
        return appVersionIdDesc;
    }

    public void setAppVersionIdDesc(String appVersionIdDesc) {
        this.appVersionIdDesc = appVersionIdDesc;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    public String getAppIconName() {
        return appIconName;
    }

    public void setAppIconName(String appIconName) {
        this.appIconName = appIconName;
    }

    public String getAppShot1() {
        return appShot1;
    }

    public void setAppShot1(String appShot1) {
        this.appShot1 = appShot1;
    }

    public String getAppShot2() {
        return appShot2;
    }

    public void setAppShot2(String appShot2) {
        this.appShot2 = appShot2;
    }

    public String getAppShot3() {
        return appShot3;
    }

    public void setAppShot3(String appShot3) {
        this.appShot3 = appShot3;
    }

    public String getAppShot4() {
        return appShot4;
    }

    public void setAppShot4(String appShot4) {
        this.appShot4 = appShot4;
    }

    public String getAppShot5() {
        return appShot5;
    }

    public void setAppShot5(String appShot5) {
        this.appShot5 = appShot5;
    }

    public String getAppTypes() {
        return appTypes;
    }

    public void setAppTypes(String appTypes) {
        this.appTypes = appTypes;
    }
}
