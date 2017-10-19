package com.seuic.app.store.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.seuic.app.store.utils.Logger;
import com.seuic.app.store.utils.TimesBytesUtils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/10/13.
 *
 * @author dpuntu
 *         监听流量变化并存储到数据库
 *         两处调用，Application和开机广播
 */

public class DataUsageService extends Service {
    private ScheduledExecutorService executorService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.w("DataUsageService - onCreate");
        executorService = new ScheduledThreadPoolExecutor(1);
        // 15秒记录一次数据
        executorService.scheduleWithFixedDelay(dataUsageRunnable, 3, 15, TimeUnit.SECONDS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
        // 只需要执行一次即可，没必要重写onStartCommand，防止多次调用
    }

    @Override
    public void onDestroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
        executorService = null;
        super.onDestroy();
    }

    private Runnable dataUsageRunnable = new Runnable() {
        @Override
        public void run() {
            TimesBytesUtils.updateOnceBytes();
        }
    };
}
