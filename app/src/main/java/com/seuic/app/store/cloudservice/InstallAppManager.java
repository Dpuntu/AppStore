package com.seuic.app.store.cloudservice;

import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.greendao.DownloadTaskTable;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.net.ApiManager;
import com.seuic.app.store.net.download.DownloadBean;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.net.download.DownloadState;
import com.seuic.app.store.utils.FileUtils;
import com.seuic.app.store.utils.Logger;
import com.seuic.app.store.utils.RxUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.seuic.app.store.net.download.DownloadState.STATE_INSTALL_FAIL;

/**
 * Created on 2017/9/26.
 *
 * @author dpuntu
 */

public class InstallAppManager {
    /**
     * 安装安全锁
     */
    private boolean isInstallLock = false;
    /**
     * 待安装的APP列表
     * <p>
     * 每次取出一个APP进行安装，完成后继续下一个
     */
    private Map<String, RecommendReceive> mRecommendReceiveMap = new HashMap<>();

    private static InstallAppManager mInstallAppManager = new InstallAppManager();

    public static InstallAppManager getInstance() {
        return mInstallAppManager;
    }

    /**
     * 添加待安装的APP任务，如果是第一个，直接进行安装，后面添加了，由于安全锁，只能添加到任务表，不可直接安装
     *
     * @param taskId
     *         任务ID
     * @param recommendReceive
     *         任务参数
     */
    public void addRecommendReceiveMap(String taskId, RecommendReceive recommendReceive) {
        if (!mRecommendReceiveMap.containsKey(taskId)) {
            mRecommendReceiveMap.put(taskId, recommendReceive);
        }
        if (!isInstallLock) {
            isInstallLock = true;
            bindCloudService4Install(taskId);
        }
    }

    /**
     * 绑定CloudService，通过CloudService接口实现静默安装，由于CloudService每次只能安装一个APP，所以才有任务表和安全锁的诞生
     *
     * @param taskId
     *         任务ID
     */
    private void bindCloudService4Install(final String taskId) {
        if (!CloudServiceManager.getInstance().isConnected()) {
            CloudServiceManager.getInstance().bindCloudService(
                    new CloudServiceManager.BindCloudListener() {
                        @Override
                        public void bindFail(String msg) {
                            CloudServiceManager.getInstance().unBindCloudService();
                            installAppEnd(false, taskId, msg);
                        }

                        @Override
                        public void bindSuccess(CloudServiceManager serviceManager) {
                            installApp(serviceManager, taskId);
                        }
                    });
        } else {
            installApp(CloudServiceManager.getInstance(), taskId);
        }
    }

    /**
     * 安装APP的具体实现方法
     *
     * @param cloudServiceManager
     *         CloudServiceManager对象，从bindService中获取
     * @param taskId
     *         任务ID
     */
    private void installApp(CloudServiceManager cloudServiceManager, final String taskId) {
        Logger.e("正在安装 " + taskId);
        DownloadTaskTable downloadTaskTable = GreenDaoManager.getInstance().queryDownloadTask(taskId);
        cloudServiceManager.installApp(
                downloadTaskTable.getSavePath() + downloadTaskTable.getFileName(),
                new CloudServiceManager.InstallListener() {
                    @Override
                    public void onInstallSuccess() {
                        installAppEnd(true, taskId, "");
                    }

                    @Override
                    public void onInstallError(String errorMsg) {
                        installAppEnd(false, taskId, errorMsg);
                    }
                });
    }

    /**
     * 更新安装状态
     *
     * @param mDownloadBean
     *         下载任务的参数
     * @param taskId
     *         任务ID
     */
    private void updateInstallState(String taskId, DownloadBean mDownloadBean) {
        DownloadManager.getInstance().notifyDownloadUpdate(taskId);
        FileUtils.deleteFile(mDownloadBean.getSavePath() + mDownloadBean.getFileName());
        mRecommendReceiveMap.remove(taskId);
        installNext();
    }

    /**
     * 安装下一个APP
     * <p>
     * 如果任务表中还可以取到任务，说明需要继续安装任务
     * 如果任务表中还可以取不到任务，但是下载任务列表中存在任务，则打开安装锁，方便下次进入时，直接进入安装
     * 如果任务表中还可以取不到任务并且下载任务列表中也不存在任务，则解绑CloudService，并打开安装锁，方便下次进入时，直接进入安装
     */
    private void installNext() {
        if (mRecommendReceiveMap.size() > 0) {
            Iterator mRecommendReceiveIterator = mRecommendReceiveMap.entrySet().iterator();
            bindCloudService4Install(((Map.Entry) mRecommendReceiveIterator.next()).getKey().toString());
        } else if (DownloadManager.getInstance().getDownloaderTaskCount() > 0) {
            isInstallLock = false;
        } else {
            isInstallLock = false;
            CloudServiceManager.getInstance().unBindCloudService();
        }
    }

    /**
     * 安装结果反馈
     *
     * @param isSuccess
     *         成功或者失败
     * @param taskId
     *         任务ID
     * @param errorMsg
     *         错误信息提示
     */
    private void installAppEnd(boolean isSuccess, String taskId, String errorMsg) {
        DownloadBean mDownloadBean = DownloadManager.getInstance().getDownloadBean(taskId);
        if (isSuccess) {
            Logger.e("安装成功");
            mDownloadBean.setLoadState(DownloadState.STATE_INSTALL_SUCCESS);
            installResult(mRecommendReceiveMap.get(taskId).getPackageName(), taskId);
        } else {
            Logger.e("安装失败: " + errorMsg);
            mDownloadBean.setLoadState(STATE_INSTALL_FAIL);
        }
        updateInstallState(taskId, mDownloadBean);
    }

    /**
     * 反馈服务器安装任务的完成情况
     *
     * @param packageName
     *         安装的任务APP的包名
     * @param taskId
     *         任务ID
     */
    private void installResult(String packageName, String taskId) {
        RxUtils.onErrorInterceptor(
                ApiManager.getInstance()
                        .getService()
                        .installResult(packageName, taskId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new InstallResponseObserver("installResult , taskId = " + taskId + " , packageName = " + packageName));
    }

    private class InstallResponseObserver extends RxUtils.ResponseObserver<String> {
        InstallResponseObserver(String observeName) {
            super(observeName);
        }

        @Override
        public void onSuccess(String str) {
            Logger.e("已反馈 : " + str);
        }

        @Override
        public void onError(boolean isResult, String errorMsg) {
            if (isResult) {
                Logger.e("已反馈");
            } else {
                Logger.e("反馈失败 : " + errorMsg);
            }
        }
    }
}
