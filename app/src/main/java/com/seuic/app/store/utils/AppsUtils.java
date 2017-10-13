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

import java.util.ArrayList;
import java.util.List;


/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class AppsUtils {

    /**
     * 获得机器中所有的APP信息
     *
     * @param context
     *
     * @return List app列表
     */
    public static ArrayList<AppInfo> getAllAppInfos(Context context) {
        ArrayList<AppInfo> packages = new ArrayList<>();
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfos = manager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : packageInfos) {
            ApplicationInfo mApplicationInfo = packageInfo.applicationInfo;
            AppInfo appBean = new AppInfo();
            appBean.setPackageName(mApplicationInfo.packageName);
            appBean.setAppIcon(mApplicationInfo.loadIcon(manager));
            appBean.setAppName(mApplicationInfo.loadLabel(manager).toString());
            appBean.setInsatll(appIsInstalled(manager, mApplicationInfo.packageName));
            appBean.setAppVersion(packageInfo.versionName);
            appBean.setUserApp(appIsUserd(mApplicationInfo));
            appBean.setPermissions(packageInfo.requestedPermissions);
            appBean.setUid(mApplicationInfo.uid);
            packages.add(appBean);
        }
        return packages;
    }

    /**
     * 获得机器中所有三方APP信息
     *
     * @param context
     *
     * @return List app列表
     */
    public static ArrayList<AppInfo> getUserAppInfos(Context context) {
        ArrayList<AppInfo> packages = new ArrayList<>();
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfos = manager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : packageInfos) {
            ApplicationInfo mApplicationInfo = packageInfo.applicationInfo;
            if (appIsUserd(mApplicationInfo)) {
                AppInfo appBean = new AppInfo();
                appBean.setPackageName(mApplicationInfo.packageName);
                appBean.setAppIcon(mApplicationInfo.loadIcon(manager));
                appBean.setAppName(mApplicationInfo.loadLabel(manager).toString());
                appBean.setInsatll(appIsInstalled(manager, mApplicationInfo.packageName));
                appBean.setAppVersion(packageInfo.versionName);
                appBean.setUserApp(appIsUserd(mApplicationInfo));
                appBean.setPermissions(packageInfo.requestedPermissions);
                appBean.setUid(mApplicationInfo.uid);
                packages.add(appBean);
            }
        }
        return packages;
    }

    /**
     * 获得机器中所有三方APP的包名和版本信息
     *
     * @param context
     *
     * @return List app列表
     */
    public static List<AppVersionRequest> getAppVersionRequests(Context context) {
        List<AppVersionRequest> appVersionRequests = new ArrayList<>();
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfos = manager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for (PackageInfo packageInfo : packageInfos) {
            ApplicationInfo mApplicationInfo = packageInfo.applicationInfo;
            if (appIsUserd(mApplicationInfo)) {
                appVersionRequests.add(new AppVersionRequest(mApplicationInfo.packageName, packageInfo.versionName));
            }
        }
        return appVersionRequests;
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

}
