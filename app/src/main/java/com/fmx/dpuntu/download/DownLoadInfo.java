package com.fmx.dpuntu.download;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class DownLoadInfo {

    private long appSize;
    private String appName;
    private String appVersion;
    private String downLoadUrl;
    private long downloadSize;
    private String filePath;
    private DownLoadListener downLoadListener;
    private DownloadState downloadState;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
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

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public DownLoadListener getDownLoadListener() {
        return downLoadListener;
    }

    public void setDownLoadListener(DownLoadListener downLoadListener) {
        this.downLoadListener = downLoadListener;
    }

    public DownloadState getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(DownloadState downloadState) {
        this.downloadState = downloadState;
    }

    public Build Builder() {
        return new Build();
    }

    public static class Build {
        private long appSize;
        private String appName;
        private String appVersion;
        private String downLoadUrl;
        private long downloadSize;
        private String filePath;
        private DownLoadListener downLoadListener;
        private DownloadState downloadState;

        public String getFilePath() {
            return filePath;
        }

        public Build setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public long getAppSize() {
            return appSize;
        }

        public Build setAppSize(long appSize) {
            this.appSize = appSize;
            return this;
        }

        public String getAppName() {
            return appName;
        }

        public Build setAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public Build setAppVersion(String appVersion) {
            this.appVersion = appVersion;
            return this;
        }

        public String getDownLoadUrl() {
            return downLoadUrl;
        }

        public Build setDownLoadUrl(String downLoadUrl) {
            this.downLoadUrl = downLoadUrl;
            return this;
        }

        public long getDownloadSize() {
            return downloadSize;
        }

        public Build setDownloadSize(long downloadSize) {
            this.downloadSize = downloadSize;
            return this;
        }

        public DownLoadListener getDownLoadListener() {
            return downLoadListener;
        }

        public Build setDownLoadListener(DownLoadListener downLoadListener) {
            this.downLoadListener = downLoadListener;
            return this;
        }

        public DownloadState getDownloadState() {
            return downloadState;
        }

        public Build setDownloadState(DownloadState downloadState) {
            this.downloadState = downloadState;
            return this;
        }

        public DownLoadInfo build() {
            DownLoadInfo mDownLoadInfo = new DownLoadInfo();
            mDownLoadInfo.setAppName(getAppName());
            mDownLoadInfo.setAppSize(getAppSize());
            mDownLoadInfo.setAppVersion(getAppVersion());
            mDownLoadInfo.setDownLoadListener(getDownLoadListener());
            mDownLoadInfo.setDownloadSize(getDownloadSize());
            mDownLoadInfo.setDownloadState(getDownloadState());
            mDownLoadInfo.setDownLoadUrl(getDownLoadUrl());
            mDownLoadInfo.setFilePath(getFilePath());
            return mDownLoadInfo;
        }
    }
}
