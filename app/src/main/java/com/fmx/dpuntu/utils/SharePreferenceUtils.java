package com.fmx.dpuntu.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created on 2017/7/27.
 *
 * @author dpuntu
 */

public class SharePreferenceUtils {
    private static SharePreferenceUtils mSharePreferenceUtils;
    private Context context;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String NAME = "com.fmx.dpuntu.share";

    private SharePreferenceUtils(Context context) {
        this.context = context;
        mSharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static SharePreferenceUtils getInstance(Context context) {
        if (mSharePreferenceUtils == null) {
            mSharePreferenceUtils = new SharePreferenceUtils(context);
        }
        return mSharePreferenceUtils;
    }

}
