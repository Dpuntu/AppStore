package com.seuic.app.store.net.download.task;

import com.seuic.app.store.net.download.DownloadBean;

import okhttp3.OkHttpClient;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class OkhttpDownloader {
    /** 指定下载客户端 */
    private OkHttpClient mClient;
    /** 最大下载任务数 */
    private int mMaxTaskSize = 0;
    /** 线程池内线程闲置销毁时间，单位分钟 */
    private int keepAliveTime = 0;
    /** 下载豆豆，key = taskId value = 下载对象 */
    private DownloadBean mDownloadBean;
    /** OkDownloader 正维持的下载 Task */
    private DownloadTask mDownloadTask;

    public OkhttpDownloader() {
    }

    public OkHttpClient getClient() {
        return mClient;
    }

    public void setClient(OkHttpClient client) {
        mClient = client;
    }

    public int getMaxTaskSize() {
        return mMaxTaskSize;
    }

    public void setMaxTaskSize(int maxTaskSize) {
        mMaxTaskSize = maxTaskSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public DownloadBean getDownloadBean() {
        return mDownloadBean;
    }

    public void setDownloadBean(DownloadBean downloadBean) {
        mDownloadBean = downloadBean;
    }

    public DownloadTask getDownloadTask() {
        return mDownloadTask;
    }

    public void setDownloadTask(DownloadTask downloadTask) {
        mDownloadTask = downloadTask;
    }

    public static class Builder {
        private int mKeepAliveTime;
        private int mMaxTaskSize;
        private OkHttpClient mClient;
        private DownloadBean mDownloadBean;
        private OkhttpDownloader mOkDownloader;

        public Builder() {
        }

        public Builder addDownloadBean(DownloadBean mDownloadBean) {
            this.mDownloadBean = mDownloadBean;
            return this;
        }

        public Builder client(OkHttpClient client) {
            this.mClient = client;
            return this;
        }

        public Builder maxTask(int maxTaskSize) {
            this.mMaxTaskSize = maxTaskSize;
            return this;
        }

        public Builder keepAliveTime(int keepMinute) {
            this.mKeepAliveTime = keepMinute;
            return this;
        }

        public OkhttpDownloader build() {
            if (mOkDownloader == null) {
                mOkDownloader = new OkhttpDownloader();
            }
            if (this.mClient == null) {
                this.mClient = new OkHttpClient();
            }
            mOkDownloader.mClient = this.mClient;
            mOkDownloader.mMaxTaskSize = this.mMaxTaskSize;
            mOkDownloader.mDownloadBean = this.mDownloadBean;
            mOkDownloader.keepAliveTime = this.mKeepAliveTime;
            mOkDownloader.mDownloadTask = new DownloadTask();
            return mOkDownloader;
        }
    }
}
