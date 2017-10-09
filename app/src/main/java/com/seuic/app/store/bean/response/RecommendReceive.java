package com.seuic.app.store.bean.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 2017/9/21.
 *
 * @author dpuntu
 */

public class RecommendReceive implements Serializable {
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
    @SerializedName("app_desc")
    String appDesc;
    @SerializedName("app_md5")
    String MD5;
    @SerializedName("download_url")
    String downloadName;
    @SerializedName("app_icon")
    String appIconName;

    public RecommendReceive(String appName, String packageName,
                            String appSize, String appVersion,
                            String appVersionId, String appDesc, String MD5,
                            String downloadName, String appIconName) {
        this.appName = appName;
        this.packageName = packageName;
        this.appSize = appSize;
        this.appVersion = appVersion;
        this.appVersionId = appVersionId;
        this.appDesc = appDesc;
        this.MD5 = MD5;
        this.downloadName = downloadName;
        this.appIconName = appIconName;
    }

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
}
