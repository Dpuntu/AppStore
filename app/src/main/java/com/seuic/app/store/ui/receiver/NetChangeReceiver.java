package com.seuic.app.store.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.utils.NetworkUtils;
import com.seuic.app.store.utils.SpUtils;


/**
 * Created on 2017/8/18.
 *
 * @author dpuntu
 *         <p>
 *         监听网络变化
 */

public class NetChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            SpUtils.getInstance()
                    .putInt(SpUtils.SP_NET, NetworkUtils.getNetWorkType(AppStoreApplication.getApp()));
        }
    }
}
