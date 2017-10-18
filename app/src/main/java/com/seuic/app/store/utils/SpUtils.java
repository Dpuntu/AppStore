package com.seuic.app.store.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.seuic.app.store.AppStoreApplication;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class SpUtils {
    private static SpUtils mSpUtils = new SpUtils();

    private SharedPreferences mPreferences;

    private static final String SP_NAME = "com.seuic.app.store";

    public static final String SP_DEVICE_SN = "device_sn";

    // 0 关 1 开
    public static final String SP_SWITCH_AUTO = "auto";

    public static final String MOBILE_RX_BYTES = "mobile_rx_bytes";

    public static final String MOBILE_TX_BYTES = "mobile_tx_bytes";

    public static final String TOTAL_RX_BYTES = "total_rx_bytes";

    public static final String TOTAL_TX_BYTES = "total_tx_bytes";

    public static final String MOBILE_RX_BYTES_ONCE = "mobile_rx_bytes_once";

    public static final String MOBILE_TX_BYTES_ONCE = "mobile_tx_bytes_once";

    public static final String TOTAL_RX_BYTES_ONCE = "total_rx_bytes_once";

    public static final String TOTAL_TX_BYTES_ONCE = "total_tx_bytes_once";

    public static final String SP_NET = "net_type";

    public static final String SP_APP_TIME = "app_time";

    private SpUtils() {
        if (mPreferences == null) {
            mPreferences = AppStoreApplication.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
    }

    public static SpUtils getInstance() {
        return mSpUtils;
    }

    /**
     * 添加boolean类型
     *
     * @param key
     *         键值
     * @param bool
     *         存储值
     */
    public boolean putBoolean(String key, boolean bool) {
        return mPreferences.edit().putBoolean(key, bool).commit();
    }

    /**
     * 添加int类型
     *
     * @param key
     *         键值
     * @param in
     *         存储值
     */
    public boolean putInt(String key, int in) {
        return mPreferences.edit().putInt(key, in).commit();
    }

    /**
     * 添加String类型
     *
     * @param key
     *         键值
     * @param str
     *         存储值
     */
    public boolean putStr(String key, String str) {
        return mPreferences.edit().putString(key, str).commit();
    }

    /**
     * 添加long类型
     *
     * @param key
     *         键值
     * @param lo
     *         存储值
     */
    public boolean putLong(String key, long lo) {
        return mPreferences.edit().putLong(key, lo).commit();
    }

    /**
     * 获取boolean类型
     *
     * @param key
     *         键值
     * @param de
     *         默认值
     */
    public boolean getBoolean(String key, boolean de) {
        return mPreferences.getBoolean(key, de);
    }

    /**
     * 获取int类型
     *
     * @param key
     *         键值
     * @param de
     *         默认值
     */
    public int getInt(String key, int de) {
        return mPreferences.getInt(key, de);
    }

    /**
     * 获取String类型
     *
     * @param key
     *         键值
     * @param de
     *         默认值
     */
    public String getStr(String key, String de) {
        return mPreferences.getString(key, de);
    }

    /**
     * 获取long类型
     *
     * @param key
     *         键值
     * @param de
     *         默认值
     */
    public long getLong(String key, long de) {
        return mPreferences.getLong(key, de);
    }

}
