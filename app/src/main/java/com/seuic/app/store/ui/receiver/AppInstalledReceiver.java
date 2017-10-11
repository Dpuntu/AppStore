package com.seuic.app.store.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.bean.AppInfo;
import com.seuic.app.store.utils.AppsUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created on 2017/9/30.
 *
 * @author dpuntu
 */

public class AppInstalledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            AppStoreApplication.getApp().setAppInfos(AppsUtils.getUserAppInfos(AppStoreApplication.getApp()));
        }
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
            List<AppInfo> appInfos = AppStoreApplication.getApp().getAppInfos();
            for (AppInfo appInfo : appInfos) {
                if (("package:" + appInfo.getPackageName()).equals(packageName)) {
                    appInfos.remove(appInfo);
                    AppStoreApplication.getApp().setAppInfos(appInfos);
                    EventBus.getDefault().post(appInfos);
                    break;
                }
            }
        }
    }
}
