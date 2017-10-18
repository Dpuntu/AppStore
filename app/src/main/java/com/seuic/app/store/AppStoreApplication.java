package com.seuic.app.store;

import android.app.Application;
import android.content.Intent;

import com.seuic.app.store.cloudservice.TerminalManager;
import com.seuic.app.store.ui.service.CacheCheckService;
import com.seuic.app.store.ui.service.DataUsageService;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.utils.AppsUtils;
import com.seuic.app.store.utils.ExceptionCrashUtils;
import com.seuic.app.store.utils.FileUtils;
import com.seuic.app.store.utils.Loger;
import com.seuic.app.store.utils.NetworkUtils;
import com.seuic.app.store.utils.SpUtils;

/**
 * Created on 2017/9/15.
 *
 * @author dpuntu
 */

public class AppStoreApplication extends Application {
    private static AppStoreApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = AppStoreApplication.this;
        FileUtils.initFileLoger(mApp);
        // 启动前先检查设备的SN号
        TerminalManager.getInstance().checkTerminal();
        ExceptionCrashUtils.getInstance(mApp);
        // 初始化本地APP列表
        AppsUtils.getAppInfos();
        // 判断网络环境
        SpUtils.getInstance()
                .putInt(SpUtils.SP_NET, NetworkUtils.getNetWorkType(mApp));
        //磁盘缓存清理并初始化下载任务队列
        startService(new Intent(this, CacheCheckService.class));
        //检查开启次数，并开始统计流量
        addAppStoreTimes();
    }

    private void addAppStoreTimes() {
        int appTime = SpUtils.getInstance().getInt(SpUtils.SP_APP_TIME, 1);
        Loger.i("这是第" + appTime + "次启动" + AppStoreUtils.getAppPackageName());
        if (appTime <= 1) {
            startService(new Intent(this, DataUsageService.class));
        }
        SpUtils.getInstance().putInt(SpUtils.SP_APP_TIME, appTime + 1);
    }

    public static AppStoreApplication getApp() {
        return mApp;
    }
}
