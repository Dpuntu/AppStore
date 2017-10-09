package com.fmx.dpuntu.download.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class DownLoadPoolManager {
    private int maxPoolSize = 1;
    private long keepAliveTime = 30;
    private static DownLoadPoolManager sPoolManager;
    private ThreadPoolExecutor mPoolExecutor;

    public static DownLoadPoolManager getInstance() {
        if (sPoolManager == null) {
            sPoolManager = new DownLoadPoolManager();
        }
        return sPoolManager;
    }

    private DownLoadPoolManager() {
        int corePoolSize = 1;
        if (maxPoolSize <= corePoolSize) {
            maxPoolSize = corePoolSize;
        }
        //we custom the threadpool.
        BlockingQueue<Runnable> workers = new LinkedBlockingQueue<>();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        mPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, //is 3 in avd.
                maxPoolSize, //which is unuseless
                keepAliveTime,
                TimeUnit.MINUTES,
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
     * 线程空闲退出时间，单位分钟
     *
     * @param minute
     *         时间
     */
    public void setKeepAliveTime(int minute) {
        keepAliveTime = minute;
    }

}
