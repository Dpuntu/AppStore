package com.seuic.app.store.ui.service;

import android.app.IntentService;
import android.content.Intent;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.glide.GlideAppManager;
import com.seuic.app.store.glide.GlideParams;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.utils.FileUtils;
import com.seuic.app.store.utils.Logger;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 *         <p>
 *         自动清理缓存
 */

public class CacheCheckService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * name is cahce
     * <p>
     * Used to name the worker thread, important only for debugging.
     */
    public CacheCheckService() {
        super("cache");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //检查下载任务
        DownloadManager.getInstance().checkDownloadingMap();
        //清理磁盘
        if (FileUtils.getFolderSize(FileUtils.getCachePath(AppStoreApplication.getApp()))
                >= GlideParams.MEMORY_CACHE_SIZE * (4 / 5f)) {
            boolean isClear = GlideAppManager.clearCache();
            if (isClear) {
                Logger.d("缓存清除成功");
            } else {
                Logger.d("自动清理缓存失败,请手动清理");
            }
        }
    }
}
