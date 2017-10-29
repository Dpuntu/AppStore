package com.seuic.app.store.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.utils.AppsUtils;
import com.seuic.app.store.utils.TimesBytesUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created on 2017/9/30.
 *
 * @author dpuntu
 */

public class AppInstalledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // PackageManager Exception Package manager has died
        String packageName = intent.getDataString();
        packageName = packageName.substring(8, packageName.length());
        switch (intent.getAction()) {
            case Intent.ACTION_PACKAGE_REMOVED:
                AppsUtils.removeAppInfo(packageName);
                GreenDaoManager.getInstance().removeDataUsageTableDao(packageName, TimesBytesUtils.BytesType.FINAL);
                GreenDaoManager.getInstance().removeDataUsageTableDao(packageName, TimesBytesUtils.BytesType.ONCE);
                DownloadManager.getInstance().removeAppByPN(packageName);
                EventBus.getDefault().post(AppsUtils.getAppInfos());
                break;
            case Intent.ACTION_PACKAGE_ADDED:
                AppsUtils.addAppInfo(packageName);
                break;
            default:
                break;
        }
    }
}
