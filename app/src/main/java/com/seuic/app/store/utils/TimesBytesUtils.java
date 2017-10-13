package com.seuic.app.store.utils;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.net.TrafficStats;
import android.support.v4.util.SimpleArrayMap;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.bean.AppInfo;
import com.seuic.app.store.greendao.DataUsageTable;
import com.seuic.app.store.greendao.GreenDaoManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.seuic.app.store.AppStoreApplication.getApp;

/**
 * Created on 2017/10/13.
 *
 * @author dpuntu
 *         流量统计
 */

public class TimesBytesUtils {

    private static SimpleArrayMap<String, String> sAppTimeArrayMap;

    /**
     * 返回设备是否支持流量统计
     */
    public static boolean isSupport() {
        return TrafficStats.UNSUPPORTED >= 0;
    }

    /**
     * 获取某个应用的流量
     *
     * @param permissions 应用权限
     * @param uid         应用进程号
     * @return long[]
     * long[0] 下载的流量byte
     * long[1] 上传的流量byte
     */
    private static long[] getAppBytes(String[] permissions, int uid) {
        if (permissions == null || permissions.length <= 0) {
            return null;
        }
        boolean isInternetApp = false;
        for (String permission : permissions) {
            if (permission.equals("android.permission.INTERNET")) {
                isInternetApp = true;
                break;
            }
        }

        if (!isInternetApp) {
            return null;
        }

        // 下载的流量byte ,上传的流量byte
        return new long[]{TrafficStats.getUidRxBytes(uid), TrafficStats.getUidTxBytes(uid)};
    }

    /**
     * 获取234G时的流量
     *
     * @return long
     * 上传的流量byte
     */
    private static long getMobileUploadBytes() {
        return TrafficStats.getMobileTxBytes();
    }

    /**
     * 获取234G时的流量
     *
     * @return long
     * 下载的流量byte
     */
    private static long getMobileDownloadBytes() {
        return TrafficStats.getMobileRxBytes();
    }


    /**
     * 获取所有的流量
     *
     * @return long
     * 上传的流量byte
     */
    private static long getTotalUploadBytes() {
        return TrafficStats.getTotalTxBytes();
    }

    /**
     * 获取所有的流量
     *
     * @return long
     * 下载的流量byte
     */
    private static long getTotalDownloadBytes() {
        return TrafficStats.getTotalRxBytes();
    }

    /**
     * 返回1个月的时长
     *
     * @return SimpleArrayMap<String, String>
     * key 包名
     * value 时长
     */
    private static SimpleArrayMap<String, String> appAMonthTimes() {
        return appMonthTimes(1);
    }

    /**
     * 返回多久间隔的运行时长
     *
     * @param month 月数
     * @return SimpleArrayMap<String, String>
     * key 包名
     * value 时长
     */
    private static SimpleArrayMap<String, String> appMonthTimes(int month) {
        SimpleArrayMap<String, String> appTimesMap = new SimpleArrayMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, -month);
        long statTime = calendar.getTimeInMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) getApp().getSystemService(Context.USAGE_STATS_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY, statTime, endTime);
            for (UsageStats usageStats : queryUsageStats) {
                String timeMessage = "最后一次运行时间:" + formatTime(usageStats.getLastTimeUsed(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                        + "\r\n运行总时长:" + formatTime(usageStats.getTotalTimeInForeground(), new SimpleDateFormat("HH时mm分ss秒"));
                appTimesMap.put(usageStats.getPackageName(), timeMessage);
            }
            return appTimesMap;
        }
        return null;
    }

    /**
     * 格式化时间
     */
    private static String formatTime(long time, SimpleDateFormat format) {
        return format.format(new Date(time));
    }

    /**
     * APP的运行时长
     */
    public static String appRunTime(String packageName) {
        if (sAppTimeArrayMap == null || sAppTimeArrayMap.size() <= 0) {
            sAppTimeArrayMap = appAMonthTimes();
        }
        if (sAppTimeArrayMap != null && sAppTimeArrayMap.containsKey(packageName)) {
            return sAppTimeArrayMap.get(packageName);
        }
        return null;
    }

    /**
     * 存储本次开机的流量统计
     */
    public static void updateOnceBytes() {
        List<AppInfo> appInfos = AppStoreApplication.getApp().getAppInfos();
        for (AppInfo info : appInfos) {
            long[] bytes = TimesBytesUtils.getAppBytes(info.getPermissions(), info.getUid());
            if (bytes == null) {
                continue;
            }
            GreenDaoManager.getInstance().insertDataUsageTableDao(info.getPackageName(),
                    bytes[0], bytes[1], BytesType.ONCE);
        }
        SpUtils.getInstance().putLong(SpUtils.MOBILE_RX_BYTES_ONCE,
                TimesBytesUtils.getMobileDownloadBytes());
        SpUtils.getInstance().putLong(SpUtils.MOBILE_TX_BYTES_ONCE,
                TimesBytesUtils.getMobileUploadBytes());
        SpUtils.getInstance().putLong(SpUtils.TOTAL_RX_BYTES_ONCE,
                TimesBytesUtils.getTotalDownloadBytes());
        SpUtils.getInstance().putLong(SpUtils.TOTAL_TX_BYTES_ONCE,
                TimesBytesUtils.getTotalUploadBytes());
    }

    /**
     * 清理本次开机的流量统计
     */
    public static void cleanOnceBytes() {
        List<AppInfo> appInfos = AppStoreApplication.getApp().getAppInfos();
        for (AppInfo info : appInfos) {
            long[] bytes = TimesBytesUtils.getAppBytes(info.getPermissions(), info.getUid());
            if (bytes == null) {
                continue;
            }
            GreenDaoManager.getInstance().removeDataUsageTableDao(info.getPackageName(), BytesType.ONCE);
        }
        SpUtils.getInstance().putLong(SpUtils.MOBILE_RX_BYTES_ONCE, 0);
        SpUtils.getInstance().putLong(SpUtils.MOBILE_TX_BYTES_ONCE, 0);
        SpUtils.getInstance().putLong(SpUtils.TOTAL_RX_BYTES_ONCE, 0);
        SpUtils.getInstance().putLong(SpUtils.TOTAL_TX_BYTES_ONCE, 0);
    }

    /**
     * 根据APP包名返回APP的流量
     *
     * @param packageName 包名
     * @return String[]
     * String[0] 下载的流量byte
     * String[1] 上传的流量byte
     */
    public static String[] getAppDataUsage(String packageName) {
        DataUsageTable mDataUsageTableFinal = GreenDaoManager.getInstance().queryDataUsageTable(packageName, BytesType.FINAL);
        DataUsageTable mDataUsageTableOnce = GreenDaoManager.getInstance().queryDataUsageTable(packageName, BytesType.ONCE);

        if (mDataUsageTableFinal == null && mDataUsageTableOnce == null) {
            return null;
        }

        if (mDataUsageTableFinal != null && mDataUsageTableOnce != null) {
            return new String[]{AndroidUtils.formatDataSize(mDataUsageTableFinal.getUidRxBytes() + mDataUsageTableOnce.getUidRxBytes()),
                    AndroidUtils.formatDataSize(mDataUsageTableFinal.getUidTxBytes() + mDataUsageTableOnce.getUidTxBytes())};
        }

        if (mDataUsageTableOnce != null) {
            return new String[]{AndroidUtils.formatDataSize(mDataUsageTableOnce.getUidRxBytes()),
                    AndroidUtils.formatDataSize(mDataUsageTableOnce.getUidTxBytes())};
        } else {
            return new String[]{AndroidUtils.formatDataSize(mDataUsageTableFinal.getUidRxBytes()),
                    AndroidUtils.formatDataSize(mDataUsageTableFinal.getUidTxBytes())};
        }
    }

    /**
     * 返回所有数据连接上传流量
     */
    public static String getUploadDataUsage4G() {
        return AndroidUtils.formatDataSize(
                SpUtils.getInstance().getLong(SpUtils.MOBILE_TX_BYTES, 0)
                        + SpUtils.getInstance().getLong(SpUtils.MOBILE_TX_BYTES_ONCE, 0));
    }

    /**
     * 返回所有数据连接下载流量
     */
    public static String getDownloadDataUsage4G() {
        return AndroidUtils.formatDataSize(
                SpUtils.getInstance().getLong(SpUtils.MOBILE_RX_BYTES, 0)
                        + SpUtils.getInstance().getLong(SpUtils.MOBILE_RX_BYTES_ONCE, 0));

    }

    /**
     * 返回所有无线网络上传流量
     */
    public static String getUploadDataUsageWifi() {
        return AndroidUtils.formatDataSize(
                SpUtils.getInstance().getLong(SpUtils.TOTAL_TX_BYTES, 0)
                        + SpUtils.getInstance().getLong(SpUtils.TOTAL_TX_BYTES_ONCE, 0)
                        - SpUtils.getInstance().getLong(SpUtils.MOBILE_TX_BYTES, 0)
                        + SpUtils.getInstance().getLong(SpUtils.MOBILE_TX_BYTES_ONCE, 0));

    }

    /**
     * 返回所有无线网络下载流量
     */
    public static String getDownloadDataUsagewifi() {
        return AndroidUtils.formatDataSize(
                SpUtils.getInstance().getLong(SpUtils.TOTAL_RX_BYTES, 0)
                        + SpUtils.getInstance().getLong(SpUtils.TOTAL_RX_BYTES_ONCE, 0)
                        - SpUtils.getInstance().getLong(SpUtils.MOBILE_RX_BYTES, 0)
                        + SpUtils.getInstance().getLong(SpUtils.MOBILE_RX_BYTES_ONCE, 0));
    }

    /**
     * 返回所有的上传流量
     */
    public static String getUploadDataUsage() {
        return AndroidUtils.formatDataSize(
                SpUtils.getInstance().getLong(SpUtils.TOTAL_TX_BYTES, 0)
                        + SpUtils.getInstance().getLong(SpUtils.TOTAL_TX_BYTES_ONCE, 0));
    }

    /**
     * 返回所有的下载流量
     */
    public static String getDownloadDataUsage() {
        return AndroidUtils.formatDataSize(
                SpUtils.getInstance().getLong(SpUtils.TOTAL_RX_BYTES, 0)
                        + SpUtils.getInstance().getLong(SpUtils.TOTAL_RX_BYTES_ONCE, 0));
    }

    public static class BytesType {

        public static final int ONCE = 0;

        public static final int FINAL = 1;
    }
}
