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

    public boolean putBoolean(String key, boolean bool) {
        return mPreferences.edit().putBoolean(key, bool).commit();
    }

    public boolean putInt(String key, int in) {
        return mPreferences.edit().putInt(key, in).commit();
    }

    public boolean putString(String key, String str) {
        return mPreferences.edit().putString(key, str).commit();
    }

    public boolean putLong(String key, long lo) {
        return mPreferences.edit().putLong(key, lo).commit();
    }

    public boolean getBoolean(String key, boolean de) {
        return mPreferences.getBoolean(key, de);
    }

    public int getInt(String key, int de) {
        return mPreferences.getInt(key, de);
    }

    public String getStr(String key, String de) {
        return mPreferences.getString(key, de);
    }

    public long getLong(String key, long de) {
        return mPreferences.getLong(key, de);
    }

}
