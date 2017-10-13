package com.seuic.app.store.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class AppInfo implements Serializable {
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
    //应用权限
    private String[] permissions;
    //应用进程ID
    private int uid;

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

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
