package com.fmx.dpuntu.download.task;

import com.fmx.dpuntu.download.DownLoadInfo;
import com.fmx.dpuntu.download.DownLoadListener;
import com.fmx.dpuntu.download.DownloadState;
import com.fmx.dpuntu.utils.Loger;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class DownLoader {
    private OkHttpClient mClient;
    private int mKeepAliveTime;
    private int mMaxTaskSize;
    private static DownLoadInfo mDownLoadInfo;

    public DownLoadInfo getDownLoadInfo() {
        return mDownLoadInfo;
    }

    public void setDownLoadInfo(DownLoadInfo downLoadInfo) {
        mDownLoadInfo = downLoadInfo;
    }

    private Map<String, DownLoadInfo> downLoadBean = new HashMap<>();
    private Map<String, DownLoadInfo> downLoadingBean = new HashMap<>();

    public OkHttpClient getClient() {
        return mClient;
    }

    public void setClient(OkHttpClient client) {
        mClient = client;
    }

    public int getKeepAliveTime() {
        return mKeepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        mKeepAliveTime = keepAliveTime;
    }

    public int getMaxTaskSize() {
        return mMaxTaskSize;
    }

    public void setMaxTaskSize(int maxTaskSize) {
        mMaxTaskSize = maxTaskSize;
    }

    public void addDownloadBean(DownLoadInfo info) {
        downLoadBean.put(info.getAppName(), info);
    }

    public void start() {
        for (Map.Entry<String, DownLoadInfo> entry : downLoadBean.entrySet()) {
            DownLoadInfo info = entry.getValue();
            startTask(info);
        }
    }

    private void startTask(DownLoadInfo info) {
        Loger.d("startTask");
        DownloadState state = info.getDownloadState();
        if (state == DownloadState.STATE_DEFAULT
                || state == DownloadState.STATE_ERROR
                || state == DownloadState.STATE_PAUSE
                ) {
            info.setDownloadState(DownloadState.STATE_START);
            DownLoadTask task = new DownLoadTask(this, info, mClient);
            DownLoadPoolManager.getInstance().setKeepAliveTime(getKeepAliveTime());
            DownLoadPoolManager.getInstance().setMaxPoolSize(getMaxTaskSize());
            DownLoadPoolManager.getInstance().execute(task);
            downLoadingBean.put(info.getAppName(), info);
            notifyDownloadUpdate(info);
        }
    }

    public void notifyDownloadUpdate(DownLoadInfo info) {
        DownLoadListener listener = info.getDownLoadListener();
        switch (info.getDownloadState()) {
            case STATE_DOWNLOADING:
                if (info.getDownloadSize() == info.getAppSize()) {
                    info.setDownloadState(DownloadState.STATE_FINISH);
                    notifyDownloadUpdate(info);
                } else if (info.getDownloadSize() >= info.getAppSize()) {
                    info.setDownloadState(DownloadState.STATE_ERROR);
                    notifyDownloadUpdate(info);
                } else {
                    listener.onProgress(info);
                }
                break;
            case STATE_FINISH:
                listener.onFinish(info);
                break;
            case STATE_START:
                listener.onStart(info);
                info.setDownloadState(DownloadState.STATE_DOWNLOADING);
                notifyDownloadUpdate(info);
                break;
            case STATE_PAUSE:
                listener.onPause(info);
                break;
            case STATE_ERROR:
                listener.onError(info);
                break;
            case STATE_DEFAULT:
                break;
        }
    }


    public static class Builder {
        private OkHttpClient mClient;
        private int mKeepAliveTime;
        private int mMaxTaskSize;
        private Map<String, DownLoadInfo> downLoadBean = new HashMap<>();
        private DownLoader mDownLoader;

        public Builder() {
        }

        public Builder(DownLoader mDownLoader) {
            this.mDownLoader = mDownLoader;
        }

        public Map<String, DownLoadInfo> getDownLoadBean() {
            return downLoadBean;
        }

        public Builder setDownLoadBean(DownLoadInfo downLoadInfo) {
            if (mDownLoader != null) {
                this.downLoadBean = mDownLoader.downLoadBean;
            }
            if (downLoadInfo != null && !downLoadBean.containsKey(downLoadInfo.getAppName())) {
                this.downLoadBean.put(downLoadInfo.getAppName(), downLoadInfo);
            }
            return this;
        }

        private DownLoadInfo mDownLoadInfo;

        public OkHttpClient getClient() {
            return mClient;
        }

        public Builder setClient(OkHttpClient client) {
            mClient = client;
            return this;
        }

        public int getKeepAliveTime() {
            return mKeepAliveTime;
        }

        public Builder setKeepAliveTime(int keepAliveTime) {
            mKeepAliveTime = keepAliveTime;
            return this;
        }

        public int getMaxTaskSize() {
            return mMaxTaskSize;
        }

        public Builder setMaxTaskSize(int maxTaskSize) {
            mMaxTaskSize = maxTaskSize;
            return this;
        }

        public DownLoader build() {
            mDownLoader.downLoadBean = getDownLoadBean();
            mDownLoader.mClient = getClient();
            mDownLoader.mKeepAliveTime = getKeepAliveTime();
            mDownLoader.mMaxTaskSize = getMaxTaskSize();
            return mDownLoader;
        }
    }
}
