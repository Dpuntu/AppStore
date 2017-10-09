package com.seuic.app.store.bean;

import java.io.Serializable;

/**
 * Created on 2017/9/23.
 *
 * @author dpuntu
 */

public class DownloadingBean implements Serializable {
    private String appName;

    private String taskId;

    private long loadedLength;

    private long totalSize;

    private String appIconName;

    private String packageName;

    public DownloadingBean(String appName, String taskId, long loadedLength, long totalSize, String appIconName, String packageName) {
        this.appName = appName;
        this.taskId = taskId;
        this.loadedLength = loadedLength;
        this.totalSize = totalSize;
        this.appIconName = appIconName;
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getLoadedLength() {
        return loadedLength;
    }

    public void setLoadedLength(long loadedLength) {
        this.loadedLength = loadedLength;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public String getAppIconName() {
        return appIconName;
    }

    public void setAppIconName(String appIconName) {
        this.appIconName = appIconName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
