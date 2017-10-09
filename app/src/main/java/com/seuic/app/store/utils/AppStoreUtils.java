package com.seuic.app.store.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class AppStoreUtils {

    /**
     * AppStore 获取图片的类型
     */
    public static class AppStoreImageType { //0表示图标1表示截图2表示广告图片
        public static final String ICON = "0";
        public static final String SCREEN = "1";
        public static final String AD = "2";
    }

    /**
     * AppStore RecycleView 刷新时拉下颜色
     */
    public static final int[] refreshColors = new int[] {R.color.searchColor, R.color.homeTitleColor, R.color.mainColor};

    /**
     * AppStore 请求头CHANNEL
     */
    public static final String CHANNEL = "appstore";

    /**
     * AppStore 请求头MD5
     */
    public static final String MD5_KEY = "pkh6pvqaw0l1ryrvnikan38iecxkdfew";


    /**
     * AppStore 请求地址
     * http://192.168.119.71:8766/apk/
     * http://192.168.10.179:8766/apk/
     */
    public static final String APPSTORE_BASE_URL = "http://192.168.10.179:8766/apk/";

    /**
     * AppStore 获得下载地址
     *
     * @param downloadName
     *         下载文件名
     * @param packageName
     *         下载的包名
     *
     * @return String 完整下载地址
     */
    // appstore/package?package_file=xxx&package_name=xxx
    public static String getDownloadUrl(String downloadName, String packageName) {
        return APPSTORE_BASE_URL + "appstore/package?package_file=" + downloadName + "&package_name=" + packageName;
    }

    /**
     * 获得包名
     *
     * @return String PackageName
     */
    public static String getAppPackageName() {
        return AppStoreApplication.getApp().getPackageName();
    }

    /**
     * 获得版本
     *
     * @return String Version
     */

    public static String getAppVersion() {
        PackageManager packageManager = AppStoreApplication.getApp().getPackageManager();
        PackageInfo packInfo = null;
        String version = null;
        try {
            packInfo = packageManager.getPackageInfo(getAppPackageName(), 0);
            version = packInfo.versionName;
            if (version == null || version.isEmpty()) {
                version = "1.0.0";
            }
        } catch (PackageManager.NameNotFoundException e) {
            Loger.e(android.util.Log.getStackTraceString(e));
            version = "1.0.0";
        }
        return version;
    }

}
