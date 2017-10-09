package com.seuic.app.store.net.download;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public interface DownloadSubject {
    /**
     * 注册观察者
     */
    void registerObserver(DownloadObserver observer);

    /**
     * 移除观察者
     */
    void removeObserver(DownloadObserver observer);

    /**
     * 通知观察者
     */
    void notifyObservers(DownloadBean bean);
}

