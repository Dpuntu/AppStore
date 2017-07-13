package com.fmx.dpuntu.utils;

import android.util.Log;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class Loger {
    private static final String TAG = "com.fmx.mvp";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}
