package com.seuic.app.store.net.download;

import com.seuic.app.store.utils.HttpHeadUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class DownloadBean implements DownloadSubject, Serializable {
    /**
     * 下载任务ID，默认为下载地址 MD5 后的值
     */
    private String taskId;
    /**
     * 下载地址
     */
    private String downloadUrl;
    /**
     * 文件保存路径
     */
    private String savePath;
    /**
     * 文件保存文件名
     */
    private String fileName;
    /**
     * 已下载长度
     */
    private long loadedLength;
    /**
     * 下载速度,单位M/S
     */
    private float downloadSpeed;
    /**
     * 文件总长度
     */
    private long totalSize;
    /**
     * 文件当前下载状态
     */
    private DownloadState loadState;
    /**
     * 文件下载进度监听
     */
    private DownloadListener mDownloadListener;
    /**
     * 请求头配置
     */
    private Map<String, String> headMap;

    private List<DownloadObserver> mObservers = new ArrayList<>();

    public DownloadBean() {
    }

    public DownloadBean(Builder mBuilder) {
        setTaskId(mBuilder.taskId);
        setDownloadUrl(mBuilder.downloadUrl);
        setSavePath(mBuilder.savePath);
        setFileName(mBuilder.fileName);
        setLoadedLength(mBuilder.loadedLength);
        setTotalSize(mBuilder.totalSize);
        setLoadState(mBuilder.loadState);
        setDownloadListener(mBuilder.mDownloadListener);
        if (mBuilder.headMap == null) {
            setHeadMap(HttpHeadUtils.getHeadMap());
        } else {
            setHeadMap(mBuilder.headMap);
        }
    }

    @Override
    public void registerObserver(DownloadObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void removeObserver(DownloadObserver observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyObservers(DownloadBean bean) {
        for (DownloadObserver observer : mObservers) {
            observer.update(bean);
        }
    }

    public float getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(float downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public Map<String, String> getHeadMap() {
        return headMap;
    }

    public void setHeadMap(Map<String, String> headMap) {
        this.headMap = headMap;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public DownloadState getLoadState() {
        return loadState;
    }

    public void setLoadState(DownloadState loadState) {
        this.loadState = loadState;
    }

    public DownloadListener getDownloadListener() {
        return mDownloadListener;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    @Override
    public String toString() {
        return "TaskId = " + getTaskId() + "\r\n"
                + "DownloadUrl = " + getDownloadUrl() + "\r\n"
                + "SavePath = " + getSavePath() + "\r\n"
                + "FileName = " + getFileName() + "\r\n"
                + "LoadedLength = " + getLoadedLength() + "\r\n"
                + "TotalSize = " + getTotalSize();
    }

    public static class Builder {
        private String taskId;
        private String downloadUrl;
        private String savePath;
        private String fileName;
        private long loadedLength;
        private long totalSize;
        private DownloadState loadState;
        private DownloadListener mDownloadListener;
        private Map<String, String> headMap;

        public Builder taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public Builder headMap(Map<String, String> headMap) {
            this.headMap = headMap;
            return this;
        }

        public Builder savePath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder loadedLength(long loadedLength) {
            this.loadedLength = loadedLength;
            return this;
        }

        public Builder totalSize(long totalSize) {
            this.totalSize = totalSize;
            return this;
        }

        public Builder loadState(DownloadState loadState) {
            this.loadState = loadState;
            return this;
        }

        public Builder downloadListener(DownloadListener downloadListener) {
            mDownloadListener = downloadListener;
            return this;
        }

        public DownloadBean build() {
            return new DownloadBean(this);
        }
    }
}
