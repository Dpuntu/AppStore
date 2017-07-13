package com.fmx.dpuntu.download;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public interface DownLoadListener {
    void onProgress(DownLoadInfo info);

    void onStart(DownLoadInfo info);

    void onPause(DownLoadInfo info);

    void onStop(DownLoadInfo info);

    void onError(DownLoadInfo info);
}
