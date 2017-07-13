package com.fmx.dpuntu.utils;

import android.graphics.drawable.Drawable;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class AppInfo {
    //图标
    private Drawable appIcon;
    //应用名称
    private String appName;
    //应用版本号
    private String appVersion;
    //应用包名
    private String packageName;
    //是否是用户app
    private boolean isUserApp;
    //是否安装
    private boolean isInsatll;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setUserApp(boolean userApp) {
        isUserApp = userApp;
    }

    public boolean isInsatll() {
        return isInsatll;
    }

    public void setInsatll(boolean insatll) {
        isInsatll = insatll;
    }
}
