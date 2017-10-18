package com.seuic.app.store.utils;

import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.net.download.DownloadState;
import com.seuic.app.store.view.MultifunctionalTextView;

import static com.seuic.app.store.utils.AppsUtils.getAppInfos;

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
                AppStoreUtils.checkDownloadNetType(new AppStoreUtils.OnDownloadListener() {
                    @Override
                    public void onOkDownloadClick() {
                        DownloadManager.getInstance().start(recommendReceive.getAppVersionId());
                    }

                    @Override
                    public void onCancelDownloadClick() {
                        Loger.e("拒绝下载");
                    }
                });
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
                int openError = AppsUtils.openApp(recommendReceive.getPackageName());
                if (openError == 0) {
                    return;
                }
                if (openError == -1) {
                    boolean isUninstallApp = true;
                    for (int i = 0; i < getAppInfos().size(); i++) {
                        if (AppsUtils.getAppInfos().get(i).getPackageName().equals(recommendReceive.getPackageName())) {
                            isUninstallApp = false;
                            break;
                        }
                    }
                    if (isUninstallApp) {
                        ToastUtils.showToast("软件未安装或已卸载,开始下载此软件");
                        DownloadManager.getInstance().setDownLoadState(recommendReceive.getAppVersionId(), DownloadState.STATE_NORMAL);
                        DownloadManager.getInstance().start(recommendReceive.getAppVersionId());
                    } else {
                        ToastUtils.showToast("此类软件无法被打开");
                    }
                } else if (openError == -2) {
                    ToastUtils.showToast("应用市场已经打开了");
                }
                break;
        }
    }
}
