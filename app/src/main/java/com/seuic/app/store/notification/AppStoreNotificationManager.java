package com.seuic.app.store.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.util.SimpleArrayMap;
import android.widget.RemoteViews;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.net.download.DownloadBean;
import com.seuic.app.store.net.download.DownloadState;
import com.seuic.app.store.ui.activity.AppDetailActivity;


/**
 * Created on 2017/10/9.
 *
 * @author dpuntu
 */

public class AppStoreNotificationManager {
    private static AppStoreNotificationManager mAppStoreNotificationManager = new AppStoreNotificationManager();
    private SimpleArrayMap<String, Integer> mDownloadBeans = new SimpleArrayMap<>(3);
    private static int id = 0;

    private AppStoreNotificationManager() {
    }

    public static AppStoreNotificationManager getInstance() {
        return mAppStoreNotificationManager;
    }

    public void showDownloadNotification(RecommendReceive mRecommendReceive, DownloadBean mDownloadBean) {
        Notification.Builder builder = new Notification.Builder(AppStoreApplication.getApp());
        Notification notification = builder.setOngoing(true)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.app_store_icon)
                .setOnlyAlertOnce(true)
                .build();
        if (Build.VERSION.SDK_INT >= 16) {
            notification.bigContentView = createRemoteViews(mRecommendReceive, mDownloadBean);
        }
        notification.contentView = createRemoteViews(mRecommendReceive, mDownloadBean);
        NotificationManager mNotificationManager = (NotificationManager) AppStoreApplication.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        if (!mDownloadBeans.containsKey(mDownloadBean.getTaskId())) {
            id++;
            mDownloadBeans.put(mDownloadBean.getTaskId(), id % 3);
        }
        mNotificationManager.notify(id % 3, notification);
    }

    private RemoteViews createRemoteViews(RecommendReceive mRecommendReceive, DownloadBean mDownloadBean) {
        int progress;
        if (mDownloadBean.getLoadedLength() <= 0 || mDownloadBean.getTotalSize() <= 0) {
            progress = 0;
        } else {
            progress = (int) ((mDownloadBean.getLoadedLength() * 1f / mDownloadBean.getTotalSize() * 1f) * 100);
        }
        RemoteViews mRemoteViews = new RemoteViews(AppStoreApplication.getApp().getPackageName(), R.layout.download_notification);
        mRemoteViews.setTextViewText(R.id.notification_title, mRecommendReceive.getAppName());
        if (mDownloadBean.getLoadState() == DownloadState.STATE_LOADING) {
            mRemoteViews.setTextViewText(R.id.notification_num, progress + "%");
            mRemoteViews.setProgressBar(R.id.notification_progressbar, 100, progress, false);
            mRemoteViews.setTextViewText(R.id.notification_hint, "正在下载...");
        } else if (mDownloadBean.getLoadState() == DownloadState.STATE_FINISH) {
            mRemoteViews.setTextViewText(R.id.notification_hint, progress == 0 ? "正在下载..." : "下载完成, 正在安装");
            if (mDownloadBeans.containsKey(mDownloadBean.getTaskId())) {
                mDownloadBeans.remove(mDownloadBean.getTaskId());
            }
        } else if (mDownloadBean.getLoadState() == DownloadState.STATE_INSTALL_SUCCESS) {
            mRemoteViews.setTextViewText(R.id.notification_hint, "安装成功");
            cancelNotification(mDownloadBean);
        }
        mRemoteViews.setImageViewResource(R.id.notification_icon, R.mipmap.app_store_icon);
        Intent intent = new Intent(AppStoreApplication.getApp(), AppDetailActivity.class);
        intent.putExtra(AppDetailActivity.APP_DETAIL, mRecommendReceive);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(AppStoreApplication.getApp(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.notification_title, pendingIntent);
        return mRemoteViews;
    }

    private void cancelNotification(DownloadBean mDownloadBean) {
        NotificationManager mNotificationManager = (NotificationManager) AppStoreApplication.getApp().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(mDownloadBeans.get(mDownloadBean.getTaskId()));
        if (mDownloadBeans.containsKey(mDownloadBean.getTaskId())) {
            mDownloadBeans.remove(mDownloadBean.getTaskId());
        }
    }
}
