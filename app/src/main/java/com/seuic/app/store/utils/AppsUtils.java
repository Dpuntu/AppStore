package com.seuic.app.store.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.bean.AppInfo;
import com.seuic.app.store.bean.request.AppVersionRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class AppsUtils {
    private static List<AppInfo> mAppInfos;

    /**
     * 获得机器中所有的APP信息
     *
     * @param context
     *
     * @return List app列表
     */
    private static List<AppInfo> getAllAppInfos(Context context) {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfos = manager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : packageInfos) {
            ApplicationInfo mApplicationInfo = packageInfo.applicationInfo;
            AppInfo appInfo = new AppInfo();
            appInfo.setPackageName(mApplicationInfo.packageName);
            appInfo.setAppIcon(mApplicationInfo.loadIcon(manager));
            appInfo.setAppName(mApplicationInfo.loadLabel(manager).toString());
            appInfo.setInstall(appIsInstalled(manager, mApplicationInfo.packageName));
            appInfo.setAppVersion(packageInfo.versionName);
            appInfo.setUserApp(true);
            appInfo.setPermissions(packageInfo.requestedPermissions);
            appInfo.setUid(mApplicationInfo.uid);
            appInfos.add(appInfo);
        }
        return appInfos;
    }

    /**
     * 获得机器中所有三方APP信息
     *
     * @param context
     *
     * @return List app列表
     */
    private static List<AppInfo> getUserAppInfos(Context context) {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfos = manager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : packageInfos) {
            ApplicationInfo mApplicationInfo = packageInfo.applicationInfo;
            if (appIsUserd(mApplicationInfo)) {
                AppInfo appInfo = new AppInfo();
                appInfo.setPackageName(mApplicationInfo.packageName);
                appInfo.setAppIcon(mApplicationInfo.loadIcon(manager));
                appInfo.setAppName(mApplicationInfo.loadLabel(manager).toString());
                appInfo.setInstall(appIsInstalled(manager, mApplicationInfo.packageName));
                appInfo.setAppVersion(packageInfo.versionName);
                appInfo.setUserApp(true);
                appInfo.setPermissions(packageInfo.requestedPermissions);
                appInfo.setUid(mApplicationInfo.uid);
                appInfos.add(appInfo);
            }
        }
        return appInfos;
    }

    /**
     * 获得APP信息
     *
     * @param context
     *
     * @return app信息
     */
    private static AppInfo getUserAppInfo(Context context, String packageName) {
        AppInfo appInfo = new AppInfo();
        PackageManager manager = context.getPackageManager();
        PackageInfo packageInfo;
        ApplicationInfo mApplicationInfo;
        try {
            packageInfo = manager.getPackageInfo(packageName, 0);
            mApplicationInfo = packageInfo.applicationInfo;
            appInfo.setPackageName(mApplicationInfo.packageName);
            appInfo.setAppIcon(mApplicationInfo.loadIcon(manager));
            appInfo.setAppName(mApplicationInfo.loadLabel(manager).toString());
            appInfo.setInstall(appIsInstalled(manager, mApplicationInfo.packageName));
            appInfo.setAppVersion(packageInfo.versionName);
            appInfo.setUserApp(true);
            appInfo.setPermissions(packageInfo.requestedPermissions);
            appInfo.setUid(mApplicationInfo.uid);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    public static List<AppInfo> getAppInfos() {
        if (mAppInfos == null || mAppInfos.size() <= 0) {
            mAppInfos = getUserAppInfos(AppStoreApplication.getApp());
        }
        return mAppInfos;
    }

    public static void setAppInfos(List<AppInfo> appInfos) {
        if (mAppInfos != null && mAppInfos.size() > 0) {
            mAppInfos.clear();
        }
        mAppInfos = appInfos;
    }

    public static void addAppInfo(AppInfo appInfo) {
        if (mAppInfos != null && mAppInfos.size() > 0) {
            mAppInfos.add(appInfo);
        }
    }

    public static void addAppInfo(String packageName) {
        addAppInfo(getUserAppInfo(AppStoreApplication.getApp(), packageName));
    }

    public static void removeAppInfo(String packageName) {
        if (mAppInfos != null && mAppInfos.size() > 0) {
            AppInfo info = null;
            for (AppInfo appInfo : mAppInfos) {
                if ((appInfo.getPackageName()).equals(packageName)) {
                    info = appInfo;
                }
            }
            mAppInfos.remove(info);
        }
    }

    /**
     * 获得机器中所有三方APP的包名和版本信息
     *
     * @return List app列表
     */
    public static List<AppVersionRequest> getRequestApps() {
        List<AppVersionRequest> requestApps = new ArrayList<>();
        List<AppInfo> appInfos = getAppInfos();
        for (AppInfo info : appInfos) {
            requestApps.add(new AppVersionRequest(info.getPackageName(), info.getAppVersion()));
        }
        return requestApps;
    }

    /**
     * 判断APP是否安装了
     *
     * @param manager
     *         PackageManager
     * @param packageName
     *         包名
     *
     * @return boolean
     */
    public static boolean appIsInstalled(PackageManager manager, String packageName) {
        try {
            manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断APP是否为三方的
     *
     * @param info
     *
     * @return boolean
     */
    private static boolean appIsUserd(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 卸载应用程序
     */
    public static void unInstallApp(String packageName) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        AppStoreApplication.getApp().startActivity(intent);
    }

    /**
     * 安装应用程序
     */
    public static void installApp(File file) {
        if (file.exists()) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            AppStoreApplication.getApp().startActivity(intent);
        } else {
            ToastUtils.showToast("文件不存在");
        }
    }

    public static int openApp(String packageName) {
        if (packageName.equals(AppStoreUtils.getAppPackageName())) {
            return -2;
        }
        try {
            Intent intent = AppStoreApplication.getApp().getPackageManager().getLaunchIntentForPackage(packageName);
            AppStoreApplication.getApp().startActivity(intent);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 判断包名所对应的应用是否安装在SD卡上
     *
     * @param packageName
     *         应用包名
     */
    public static boolean isInstallOnSDCard(String packageName) {
        PackageManager mPackageManager = AppStoreApplication.getApp().getPackageManager();
        ApplicationInfo appInfo;
        try {
            appInfo = mPackageManager.getApplicationInfo(packageName, 0);
            if ((appInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
