package com.seuic.app.store.utils;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.seuic.app.store.ui.activity.SplashActivity;

/**
 * Created on 2017/8/11.
 *
 * @author dpuntu
 *         <p>
 *         程序如意外崩溃，重启服务
 */

public class ExceptionCrashUtils implements Thread.UncaughtExceptionHandler {

    static ExceptionCrashUtils mExceptionCrashUtils;

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Application application;

    private ExceptionCrashUtils(Application application) {
        this.application = application;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static synchronized ExceptionCrashUtils getInstance(Application application) {
        if (mExceptionCrashUtils == null) {
            synchronized (ExceptionCrashUtils.class) {
                if (mExceptionCrashUtils == null) {
                    mExceptionCrashUtils = new ExceptionCrashUtils(application);
                }
            }

        }
        return mExceptionCrashUtils;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Loger.e("unCaughtException , " + thread.getName()
                        + " , throwable: \r\n" + android.util.Log.getStackTraceString(throwable));
        Loger.e("AppStore异常崩溃, 正在准备重启...");
        if (!handleException(throwable) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(application, SplashActivity.class);
            PendingIntent restartIntent = PendingIntent.getService(application, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.gc();
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        return true;
    }
}
