package com.seuic.app.store;

import android.app.Application;
import android.content.Intent;

import com.seuic.app.store.bean.AppInfo;
import com.seuic.app.store.ui.service.CacheCheckService;
import com.seuic.app.store.utils.AppsUtils;
import com.seuic.app.store.utils.ExceptionCrashUtils;
import com.seuic.app.store.utils.FileUtils;
import com.seuic.app.store.utils.NetworkUtils;
import com.seuic.app.store.utils.SpUtils;

import java.util.List;

/**
 * Created on 2017/9/15.
 *
 * @author dpuntu
 */

public class AppStoreApplication extends Application {
    // UE
    // http://www.pmdaniu.com/rp/view?id=AyAHZlQ5VWYDMQQ7AChVBQ
    // UI
    // https://www.chainco.cn/run/GGBrkno4KgfUqdgE
    // 接口
    // http://192.168.10.177:9010/

    private static AppStoreApplication mApp;
    private List<AppInfo> mAppInfos;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        ExceptionCrashUtils.getInstance(mApp);
        FileUtils.initFileLoger(mApp);
        // 由于之后的每次加载可能都会做读取本地软件做版本比较，所以直接存储一个List，免得以后每次读取for循环
        mAppInfos = AppsUtils.getUserAppInfos(mApp);
        // 判断网络环境
        SpUtils.getInstance()
                .getEditor()
                .putInt(SpUtils.SP_NET, NetworkUtils.getNetWorkType(mApp))
                .commit();
        //磁盘缓存清理
        startService(new Intent(this, CacheCheckService.class));
    }

    public List<AppInfo> getAppInfos() {
        if (mAppInfos == null || mAppInfos.size() <= 0) {
            mAppInfos = AppsUtils.getUserAppInfos(mApp);
        }
        return mAppInfos;
    }

    // 卸载软件之后，需要重新读取并配置一次
    public void setAppInfos(List<AppInfo> mAppInfos) {
        if (this.mAppInfos != null && this.mAppInfos.size() > 0) {
            this.mAppInfos.clear();
        }
        this.mAppInfos = mAppInfos;
    }

    public static AppStoreApplication getApp() {
        return mApp;
    }

}
