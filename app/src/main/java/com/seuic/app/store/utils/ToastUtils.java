package com.seuic.app.store.utils;

import android.widget.Toast;

import com.seuic.app.store.AppStoreApplication;

/**
 * Created on 2017/9/30.
 *
 * @author dpuntu
 */

public class ToastUtils {

    public static void showToast(String msg) {
        Toast.makeText(AppStoreApplication.getApp(), msg, Toast.LENGTH_SHORT).show();
    }
}
