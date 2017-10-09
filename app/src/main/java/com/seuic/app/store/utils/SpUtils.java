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

    private static final String SP_NAME = "com.seuic.app.store";

    // 0 关 1 开
    public static final String SP_SWITCH_AUTO = "auto";

    public static final String SP_AD_MSG = "ad_msg";

    public static final String SP_NET = "net_type";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private SpUtils() {
        if (mPreferences == null) {
            mPreferences = AppStoreApplication.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
        }
    }

    public static SpUtils getInstance() {
        return mSpUtils;
    }

    public SharedPreferences.Editor getEditor() {
        return mEditor;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }
}
