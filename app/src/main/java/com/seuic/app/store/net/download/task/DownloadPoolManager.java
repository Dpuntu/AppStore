package com.seuic.app.store.net.download.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class DownloadPoolManager {
    private static DownloadPoolManager sPoolManager;
    private ThreadPoolExecutor mPoolExecutor;
    private int maxPoolSize = 30;
    private long keepAliveTime = 500;

    public static DownloadPoolManager getInstance() {
        if (sPoolManager == null) {
            synchronized (DownloadPoolManager.class) {
                sPoolManager = new DownloadPoolManager();
            }
        }
        return sPoolManager;
    }

    private DownloadPoolManager() {
        int corePoolSize = 3;
        if (maxPoolSize <= corePoolSize) {
            maxPoolSize = corePoolSize;
        }
        BlockingQueue<Runnable> workers = new LinkedBlockingQueue<>();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        mPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                workers,
                Executors.defaultThreadFactory(),
                handler
        );
    }

    public void setMaxPoolSize(int size) {
        this.maxPoolSize = size;
    }

    /**
     * 往线程池中添加任务
     *
     * @param runnable
     *         被添加的任务
     */
    public void execute(Runnable runnable) {
        if (runnable != null) {
            mPoolExecutor.execute(runnable);
        }
    }

    /**
     * 从线程池中移除任务
     *
     * @param runnable
     *         被移除的任务
     */
    public void remove(Runnable runnable) {
        if (runnable != null) {
            mPoolExecutor.remove(runnable);
        }
    }

    /**
     * 线程空闲退出时间，单位毫秒
     *
     * @param minute
     *         时间
     */
    public void setKeepAliveTime(int minute) {
        keepAliveTime = minute;
    }

    /**
     * 不再接受新任务
     */
    public void shutdown() {
        if (mPoolExecutor != null) {
            mPoolExecutor.shutdown();
        }
    }

    /**
     * 立刻停止所有任务
     */
    public void shutdownAll() {
        if (mPoolExecutor != null) {
            mPoolExecutor.shutdownNow();
        }
    }
}
