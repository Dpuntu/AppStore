package com.seuic.app.store.net.download;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public enum DownloadState {
    /**
     * 新任务建立
     */
    STATE_NORMAL,
    /**
     * 新任务建立
     */
    STATE_NEWTASK,
    /**
     * 任务下载中
     */
    STATE_LOADING,
    /**
     * 任务暂停
     */
    STATE_PAUSE,
    /**
     * 更新
     */
    STATE_UPDATE,
    /**
     * 任务完成
     */
    STATE_FINISH,
    /**
     * 任务下载错误
     */
    STATE_ERROR,
    /**
     * 安装完成
     */
    STATE_INSTALL_SUCCESS,
    /**
     * 安装失败
     */
    STATE_INSTALL_FAIL,

}
