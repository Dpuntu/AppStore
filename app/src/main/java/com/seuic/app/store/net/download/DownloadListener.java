package com.seuic.app.store.net.download;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public interface DownloadListener {
    void onPause(DownloadBean bean);

    void onProgress(DownloadBean bean);

    void onFinished(DownloadBean bean);

    void onError(DownloadBean bean, int errCode, String errMsg);
}
