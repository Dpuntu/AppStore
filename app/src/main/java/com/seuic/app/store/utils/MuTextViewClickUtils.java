package com.seuic.app.store.utils;

import android.view.View;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.net.download.DownloadState;
import com.seuic.app.store.ui.dialog.DialogManager;
import com.seuic.app.store.view.MultifunctionalTextView;

/**
 * Created on 2017/9/29.
 *
 * @author dpuntu
 */

public class MuTextViewClickUtils {

    /**
     * 多功能TextVieW的点击事件处理
     * <p>
     * 除了以下的状态，还有取消和卸载，这两个独立处理，针对相应的操作处理
     */
    public static void clickChangeState(final RecommendReceive recommendReceive, int textState) {
        switch (textState) {
            // 等待状态不可做任何操作
            case MultifunctionalTextView.TextViewState.WAIT:
                break;
            //  以下状态点击即下载开始
            case MultifunctionalTextView.TextViewState.NORMAL:
            case MultifunctionalTextView.TextViewState.UPDATE:
            case MultifunctionalTextView.TextViewState.LOADING_PAUSE:
            case MultifunctionalTextView.TextViewState.LOADING_FAIL:
            case MultifunctionalTextView.TextViewState.INSTALL_FAIL:
                NetworkUtils.NETTYPE netType = NetworkUtils.getNetType();
                switch (netType) {
                    case NONE_NET:// 没有网络
                        ToastUtils.showToast("请检查网络链接是否正常");
                        break;
                    case DATA_NET:
                        DialogManager.getInstance()
                                .showHintDialog("温馨提示",
                                                "当前使用数据连接，是否继续下载？",
                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        DialogManager.getInstance().dismissHintDialog();
                                                        DownloadManager.getInstance().start(recommendReceive.getAppVersionId());
                                                    }
                                                },
                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        DialogManager.getInstance().dismissHintDialog();
                                                    }
                                                });
                        break;
                    case WIFI_NET:
                        DownloadManager.getInstance().start(recommendReceive.getAppVersionId());
                        break;
                }
                break;
            //  正在下载状态
            case MultifunctionalTextView.TextViewState.LOADING:
                DownloadManager.getInstance().pause(recommendReceive.getAppVersionId());
                break;
            //  下载完成状态,即为安装中,无需任何操作
            case MultifunctionalTextView.TextViewState.LOADING_FINISH:
                break;
            //  下载取消状态,删除所有与之相关的数据，并回执失败
            case MultifunctionalTextView.TextViewState.INSTALL_FINISH:
                if (AppsUtils.openApp(recommendReceive.getPackageName()) != 0) {
                    boolean isUninstallApp = true;
                    for (int i = 0; i < AppStoreApplication.getApp().getAppInfos().size(); i++) {
                        if (AppStoreApplication.getApp().getAppInfos().get(i).getPackageName().equals(recommendReceive.getPackageName())) {
                            isUninstallApp = false;
                            break;
                        }
                    }
                    if (isUninstallApp) {
                        ToastUtils.showToast("软件未安装或被卸载,已开始下载此软件");
                        DownloadManager.getInstance().setDownLoadState(recommendReceive.getAppVersionId(), DownloadState.STATE_NORMAL);
                        DownloadManager.getInstance().start(recommendReceive.getAppVersionId());
                    } else {
                        ToastUtils.showToast("此类软件无法被打开");
                    }
                }
                break;
        }
    }
}
