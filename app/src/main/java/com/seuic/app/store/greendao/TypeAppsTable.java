package com.seuic.app.store.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created on 2017/9/26.
 *
 * @author dpuntu
 *         <p>
 *         只做一次性处理，退出时数据必须全部删除
 */
@Entity
public class TypeAppsTable {
    @Id(autoincrement = true)
    private Long id;
    private String appVersionId;
    private String appName;
    private String packageName;
    private String appSize;
    private String appVersion;
    private String appDesc;
    private String MD5;
    private String downloadName;
    private String appIconName;
    private String appTypeName;
    private String appTypeId;

    @Generated(hash = 850909634)
    public TypeAppsTable(Long id, String appVersionId, String appName,
                         String packageName, String appSize, String appVersion, String appDesc,
                         String MD5, String downloadName, String appIconName, String appTypeName,
                         String appTypeId) {
        this.id = id;
        this.appVersionId = appVersionId;
        this.appName = appName;
        this.packageName = packageName;
        this.appSize = appSize;
        this.appVersion = appVersion;
        this.appDesc = appDesc;
        this.MD5 = MD5;
        this.downloadName = downloadName;
        this.appIconName = appIconName;
        this.appTypeName = appTypeName;
        this.appTypeId = appTypeId;
    }

    @Generated(hash = 491325036)
    public TypeAppsTable() {
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

    public String getAppTypeName() {
        return this.appTypeName;
    }

    public void setAppTypeName(String appTypeName) {
        this.appTypeName = appTypeName;
    }

    public String getAppTypeId() {
        return this.appTypeId;
    }

    public void setAppTypeId(String appTypeId) {
        this.appTypeId = appTypeId;
    }

}
