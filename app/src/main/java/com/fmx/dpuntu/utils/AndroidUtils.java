package com.fmx.dpuntu.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class AndroidUtils {

    public static ArrayList<AppInfo> getAppInfos(Context context) {
        ArrayList<AppInfo> packages = new ArrayList<>();
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfos = manager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for (PackageInfo packageInfo : packageInfos) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                ApplicationInfo mApplicationInfo = packageInfo.applicationInfo;
                AppInfo appBean = new AppInfo();
                appBean.setPackageName(mApplicationInfo.packageName);
                appBean.setAppIcon(mApplicationInfo.loadIcon(manager));
                appBean.setAppName(mApplicationInfo.loadLabel(manager).toString());
                appBean.setInsatll(appIsInstalled(manager, mApplicationInfo.packageName));
                appBean.setAppVersion(packageInfo.versionName);
                appBean.setUserApp(appIsUserd(mApplicationInfo));

                Loger.d("packageName = " + mApplicationInfo.packageName);
                Loger.d("versionName = " + packageInfo.versionName);
                Loger.d("appName = " + mApplicationInfo.loadLabel(manager).toString());

                packages.add(appBean);
            }
        }
        return packages;
    }

    private static boolean appIsInstalled(PackageManager manager, String packageName) {
        try {
            manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    //判断应用程序是否是用户程序
    private static boolean appIsUserd(ApplicationInfo info) {
        //原来是系统应用，用户手动升级
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //用户自己安装的应用程序
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }

    public static String decrypt(String encUrl, String key) {
        if (encUrl == null || key == null) {
            return null;
        }
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            byte[] urlBytes = cipher
                    .doFinal(StringUtils.convertToBytes(encUrl));
            String url = new String(urlBytes);
            return url;
        } catch (Exception e) {
            Loger.e(e.toString());
            return null;
        }
    }


    public static long exchangeSize(String size) {
        int index = size.indexOf("MB");
        size = size.substring(0, index);
        Loger.i("exchangeSize  " + size);
        Double fileSize = Double.parseDouble(size);
        Loger.d("size = " + fileSize * 1024 * 1024);
        return (long) (fileSize * 1024 * 1024);
    }

    public static String fileName(String appName, String appVersion) {
        return appName + "_" + appVersion + ".apk";
    }
}
