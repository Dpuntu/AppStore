package com.seuic.app.store.utils;

import android.util.Log;

/**
 * Created on 2017/8/10.
 *
 * @author dpuntu
 */

public class Loger {
    private final static boolean DEUBG = true;

    private final static String TAG = "com.seuic.app.store";

    public static void i(String msg) {
        if (DEUBG) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (DEUBG) {
            Log.d(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (DEUBG) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (DEUBG) {
            Log.e(TAG, msg);
        }
        FileUtils.writeLog(msg);
    }
}
