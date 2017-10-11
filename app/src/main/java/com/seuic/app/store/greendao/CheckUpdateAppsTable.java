package com.seuic.app.store.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created on 2017/9/23.
 *
 * @author dpuntu
 */
@Entity
public class CheckUpdateAppsTable {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String appVersionId;
    private String appName;
    private String packageName;
    private String appSize;
    private String appVersion;
    private String appVersionDesc;
    private String appDesc;
    private String MD5;
    private String downloadName;
    private String appIconName;
    @Generated(hash = 1811817647)
    public CheckUpdateAppsTable(Long id, String appVersionId, String appName,
            String packageName, String appSize, String appVersion,
            String appVersionDesc, String appDesc, String MD5, String downloadName,
            String appIconName) {
        this.id = id;
        this.appVersionId = appVersionId;
        this.appName = appName;
        this.packageName = packageName;
        this.appSize = appSize;
        this.appVersion = appVersion;
        this.appVersionDesc = appVersionDesc;
        this.appDesc = appDesc;
        this.MD5 = MD5;
        this.downloadName = downloadName;
        this.appIconName = appIconName;
    }
    @Generated(hash = 2089223772)
    public CheckUpdateAppsTable() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAppVersionId() {
        return this.appVersionId;
    }
    public void setAppVersionId(String appVersionId) {
        this.appVersionId = appVersionId;
    }
    public String getAppName() {
        return this.appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getPackageName() {
        return this.packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getAppSize() {
        return this.appSize;
    }
    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }
    public String getAppVersion() {
        return this.appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    public String getAppVersionDesc() {
        return this.appVersionDesc;
    }
    public void setAppVersionDesc(String appVersionDesc) {
        this.appVersionDesc = appVersionDesc;
    }
    public String getAppDesc() {
        return this.appDesc;
    }
    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }
    public String getMD5() {
        return this.MD5;
    }
    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }
    public String getDownloadName() {
        return this.downloadName;
    }
    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }
    public String getAppIconName() {
        return this.appIconName;
    }
    public void setAppIconName(String appIconName) {
        this.appIconName = appIconName;
    }


}
