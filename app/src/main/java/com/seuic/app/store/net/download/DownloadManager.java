package com.seuic.app.store.net.download;

import android.support.v4.util.SimpleArrayMap;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.bean.AppInfo;
import com.seuic.app.store.bean.DownloadingBean;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.greendao.DownloadTaskTable;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.greendao.RecommendReceiveTable;
import com.seuic.app.store.install.InstallAppManager;
import com.seuic.app.store.listener.DownloadCountListener;
import com.seuic.app.store.listener.UpdateCountListener;
import com.seuic.app.store.net.download.task.DownloadPoolManager;
import com.seuic.app.store.net.download.task.DownloadTask;
import com.seuic.app.store.net.download.task.OkhttpDownloader;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.utils.FileUtils;
import com.seuic.app.store.utils.HttpHeadUtils;
import com.seuic.app.store.utils.Loger;
import com.seuic.app.store.utils.Md5Utils;

import java.io.File;
import java.util.List;

import okhttp3.OkHttpClient;

import static com.seuic.app.store.net.download.DownloadState.STATE_INSTALL_FAIL;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 *         <p>
 *         多页面同步下载:任意位置获取需要任务Id对应的任务,并对其进行数据更新的监听
 *         <p>
 *         多任务管理
 */

public class DownloadManager {
    /**
     * 正在下载任务集合
     */
    private SimpleArrayMap<String, OkhttpDownloader> mIsDownLoadingMap = new SimpleArrayMap<>();
    /**
     * 下载任务集合
     */
    private SimpleArrayMap<String, OkhttpDownloader> mDownloaderMap = new SimpleArrayMap<>();
    /**
     * 下载的APP详情
     */
    private SimpleArrayMap<String, RecommendReceive> mRecommendReceiveMap = new SimpleArrayMap<>();

    private static DownloadManager mDownloadManager = new DownloadManager();

    private String errorMsg = "error";
    private int errorCode = 1;

    public static DownloadManager getInstance() {
        return mDownloadManager;
    }

    public void setError(int errorCode, String errorMsg) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getDownloaderTaskCount() {
        if (mIsDownLoadingMap.size() > 0) {
            return mIsDownLoadingMap.size();
        } else {
            return 0;
        }
    }

    public void setDownLoadState(String taskId, DownloadState state) {
        if (mDownloaderMap.containsKey(taskId)) {
            if (state == DownloadState.STATE_NORMAL) {
                mDownloaderMap.get(taskId).getDownloadBean().setLoadedLength(0);
            }
            mDownloaderMap.get(taskId).getDownloadBean().setLoadState(state);
        } else {
            Loger.e("DownloaderMap don't have this id = " + taskId);
        }
    }

    public SimpleArrayMap<String, OkhttpDownloader> getDownloadingMap() {
        return mIsDownLoadingMap;
    }

    public SimpleArrayMap<String, RecommendReceive> getRecommendReceiveMap() {
        return mRecommendReceiveMap;
    }

    /**
     * 添加所有可见可能被安装的APP到mDownloaderMap，mRecommendReceiveMap，并且不会被移除，除非软件退出后重启
     * <p>
     * 所有的下载必须经过这一步，否则无法下载
     */
    public void add2OkhttpDownloaderMap(RecommendReceive recommendReceive) {
        /**
         * 如果mDownloadingMap中有任务，不需要再添加
         * */
        if (mIsDownLoadingMap.containsKey(recommendReceive.getAppVersionId())
                || (mDownloaderMap.containsKey(recommendReceive.getAppVersionId())
                && mRecommendReceiveMap.containsKey(recommendReceive.getAppVersionId()))) {
            return;
        }

        mDownloaderMap.remove(recommendReceive.getAppVersionId());
        mRecommendReceiveMap.remove(recommendReceive.getAppVersionId());

        DownloadState downloadState = DownloadState.STATE_NORMAL;
        List<AppInfo> appInfos = AppStoreApplication.getApp().getAppInfos();
        for (AppInfo info : appInfos) {
            if (recommendReceive.getPackageName().equals(info.getPackageName())) {
                if (!recommendReceive.getAppVersion().equals(info.getAppVersion())) {
                    RecommendReceiveTable recommendReceiveTable = GreenDaoManager.getInstance().queryRecommendReceiveByPackageName(info.getPackageName());
                    DownloadTaskTable downloadTaskTable = GreenDaoManager.getInstance().queryDownloadTask(recommendReceive.getAppVersionId());
                    if (recommendReceiveTable != null && downloadTaskTable != null) {
                        downloadState = DownloadState.STATE_PAUSE;
                    } else {
                        downloadState = DownloadState.STATE_UPDATE;
                    }
                } else {
                    downloadState = DownloadState.STATE_INSTALL_SUCCESS;
                }
            } else {
                RecommendReceiveTable recommendReceiveTable = GreenDaoManager.getInstance().queryRecommendReceiveByPackageName(info.getPackageName());
                DownloadTaskTable downloadTaskTable = GreenDaoManager.getInstance().queryDownloadTask(recommendReceive.getAppVersionId());
                if (recommendReceiveTable != null && downloadTaskTable != null) {
                    downloadState = DownloadState.STATE_PAUSE;
                }
            }
        }

        DownloadBean mDownloadBean = new DownloadBean.Builder()
                .downloadUrl(AppStoreUtils.getDownloadUrl(recommendReceive.getDownloadName(), recommendReceive.getPackageName()))
                .fileName(recommendReceive.getAppName() + "_" + recommendReceive.getAppVersion() + "_" + recommendReceive.getDownloadName())
                .loadState(downloadState)
                .savePath(FileUtils.getDownloadPath(AppStoreApplication.getApp()))
                .taskId(recommendReceive.getAppVersionId())
                .headMap(HttpHeadUtils.getHeadMap())
                .build();

        OkhttpDownloader mOkhttpDownloader = new OkhttpDownloader.Builder()
                .addDownloadBean(mDownloadBean)
                .client(new OkHttpClient())
                .keepAliveTime(30)
                .maxTask(50)
                .build();

        mDownloaderMap.put(mOkhttpDownloader.getDownloadBean().getTaskId(), mOkhttpDownloader);
        mRecommendReceiveMap.put(mOkhttpDownloader.getDownloadBean().getTaskId(), recommendReceive);
    }


    public boolean iSAppStoreUpdate(RecommendReceive recommendReceive) {
        if (mIsDownLoadingMap.containsKey(recommendReceive.getAppVersionId())) {
            DownloadState downloadState = mIsDownLoadingMap.get(recommendReceive.getAppVersionId()).getDownloadBean().getLoadState();
            if (downloadState == DownloadState.STATE_FINISH
                    || downloadState == DownloadState.STATE_LOADING) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 查找任务表mIsDownLoadingMap中是否存在任务
     * <p>
     * 没有的话从可能下载的mDownloaderMap表中导入下载数据
     * <p>
     * 开始下载
     */
    public void start(String taskId) {
        Loger.e("开始任务 " + taskId);
        if (mIsDownLoadingMap.containsKey(taskId)) {
            startSingleTask(mIsDownLoadingMap.get(taskId));
        } else {
            mIsDownLoadingMap.put(taskId, mDownloaderMap.get(taskId));
            startSingleTask(mIsDownLoadingMap.get(taskId));
        }
    }

    /**
     * 初始化的时候，从数据库读到数据
     */
    public void checkDownloadingMap() {
        List<DownloadTaskTable> downloadTaskTables = GreenDaoManager.getInstance().queryDownloadTaskTable();
        if (downloadTaskTables == null) {
            return;
        }
        for (DownloadTaskTable downloadTaskTable : downloadTaskTables) {
            RecommendReceiveTable recommendReceiveTable = GreenDaoManager.getInstance().queryRecommendReceive(downloadTaskTable.getTaskId());
            if (recommendReceiveTable == null) {
                continue;
            }
            if (recommendReceiveTable.getPackageName().equals(AppStoreUtils.getAppPackageName())) {
                GreenDaoManager.getInstance().removeDownloadTaskTable(recommendReceiveTable.getAppVersionId());
                GreenDaoManager.getInstance().removeRecommendReceiveTable(recommendReceiveTable.getAppVersionId());
                continue;
            }
            DownloadingBean mDownloadingBean = new DownloadingBean(recommendReceiveTable.getAppName(),
                                                                   downloadTaskTable.getTaskId(),
                                                                   downloadTaskTable.getLoadedLength(),
                                                                   downloadTaskTable.getTotalSize(),
                                                                   recommendReceiveTable.getAppIconName(),
                                                                   recommendReceiveTable.getPackageName());
            addDownloadingMap(mDownloadingBean);
        }
    }

    /**
     * 添加到下载队列中
     */
    private void addDownloadingMap(DownloadingBean downloadingBean) {
        if (mDownloaderMap.containsKey(downloadingBean.getTaskId())) {
            mIsDownLoadingMap.put(downloadingBean.getTaskId(), mDownloaderMap.get(downloadingBean.getTaskId()));
        } else {
            RecommendReceiveTable recommendReceiveTable = GreenDaoManager.getInstance().queryRecommendReceive(downloadingBean.getTaskId());
            if (recommendReceiveTable == null) {
                GreenDaoManager.getInstance().removeDownloadTaskTable(downloadingBean.getTaskId());
                return;
            }
            add2OkhttpDownloaderMap(GreenDaoManager.getInstance().table2RecommendReceive(recommendReceiveTable));
            mIsDownLoadingMap.put(downloadingBean.getTaskId(), mDownloaderMap.get(downloadingBean.getTaskId()));
        }
        if (mDownloadCountListener != null) {
            mDownloadCountListener.onDownloadCountChange(mIsDownLoadingMap.size());
        }
    }

    public void startAll() {
        if (mIsDownLoadingMap.size() > 0) {
            for (int i = 0; i < mIsDownLoadingMap.size(); i++) {
                startSingleTask(mIsDownLoadingMap.get(mIsDownLoadingMap.keyAt(i)));
            }
        } else {
            Loger.e("startAll don't have any task");
        }
    }

    private void startSingleTask(OkhttpDownloader mOkhttpDownloader) {
        DownloadBean bean = mOkhttpDownloader.getDownloadBean();
        String taskId = bean.getTaskId();
        DownloadState state = bean.getLoadState();

        if (mDownloadCountListener != null) {
            mDownloadCountListener.onDownloadCountChange(mIsDownLoadingMap.size());
        }

        if (state == DownloadState.STATE_NORMAL
                || state == DownloadState.STATE_PAUSE
                || state == DownloadState.STATE_NEWTASK
                || state == DownloadState.STATE_ERROR
                || state == DownloadState.STATE_UPDATE
                || state == DownloadState.STATE_INSTALL_FAIL) {
            DownloadTask downloadTask = mOkhttpDownloader.getDownloadTask();
            downloadTask.setDownloadTask(bean, mOkhttpDownloader.getClient());
            mIsDownLoadingMap.get(taskId).setDownloadTask(downloadTask);
            DownloadPoolManager.getInstance().execute(downloadTask);
            /**
             * 将任务插入到数据库
             * */
            GreenDaoManager.getInstance().insertRecommendReceiveTableDao(mRecommendReceiveMap.get(taskId));
            GreenDaoManager.getInstance().insertDownloadTaskTable(bean);
            Loger.d("enqueue download task into thread pool!");
        } else {
            Loger.e("The state of current task is " + bean.getLoadState() + ",  can't be downloaded!");
        }
    }

    /**
     * 根据 taskId 从下载列表中获取下载对象
     *
     * @param taskId
     *         任务ID
     *
     * @return taskId 对应的 DownloadBean
     */
    public DownloadBean getDownloadBean(String taskId) {
        if (mDownloaderMap.containsKey(taskId)) {
            return mDownloaderMap.get(taskId).getDownloadBean();
        } else {
            return null;
        }
    }

    /**
     * 暂停下载任务
     *
     * @param taskId
     *         被暂停的下载任务 ID
     */
    public void pause(String taskId) {
        DownloadBean bean = getDownloadBean(taskId);
        if (bean != null) {
            bean.setLoadState(DownloadState.STATE_PAUSE);
            notifyDownloadUpdate(bean.getTaskId());
        }
    }

    /**
     * 将所有的任务都暂停
     */
    public void pauseAll() {
        for (int i = 0; i < mIsDownLoadingMap.size(); i++) {
            pause(mIsDownLoadingMap.keyAt(i));
        }
    }

    /**
     * 移除一项正在下载的任务
     *
     * @param taskId
     *         任务ID
     */
    public void removeLoadingTask(String taskId) {
        if (!mIsDownLoadingMap.containsKey(taskId)) {
            Loger.e("removeLoadingTask don't have " + taskId + " task");
        } else {
            removeTask(taskId);
        }
    }

    /**
     * 数据库移除，loading移除，线程池移除，文件删除，刷新界面
     */
    private void removeTask(String taskId) {
        DownloadBean mDownloadBean = getDownloadBean(taskId);
        mDownloadBean.setLoadState(DownloadState.STATE_INSTALL_FAIL);
        notifyDownloadUpdate(taskId);
    }

    /**
     * 通知所有的监听器更新
     *
     * @param taskId
     *         下载对象ID
     */

    public void notifyDownloadUpdate(String taskId) {
        if (!mIsDownLoadingMap.containsKey(taskId)) {
            Loger.e("notifyDownloadUpdate don't have " + taskId + " task");
        } else {
            DownloadBean mDownloadBean = mIsDownLoadingMap.get(taskId).getDownloadBean();
            // AppStoreNotificationManager.getInstance().showDownloadNotification(mRecommendReceiveMap.get(taskId), mDownloadBean);
            mDownloadBean.notifyObservers(mDownloadBean);
            switch (mDownloadBean.getLoadState()) {
                case STATE_INSTALL_SUCCESS:
                    GreenDaoManager.getInstance().removeCheckUpdateAppsTable(taskId);
                    if (mUpdateCountListener != null) {
                        mUpdateCountListener.onUpdateCountChange();
                    }
                case STATE_INSTALL_FAIL:
                    GreenDaoManager.getInstance().removeDownloadTaskTable(taskId);
                    GreenDaoManager.getInstance().removeRecommendReceiveTable(taskId);
                    if (mIsDownLoadingMap.containsKey(taskId)) {
                        DownloadPoolManager.getInstance().remove(mIsDownLoadingMap.get(taskId).getDownloadTask());
                        mIsDownLoadingMap.remove(taskId);
                    }
                    FileUtils.deleteFile(mDownloadBean.getSavePath() + mDownloadBean.getFileName());
                    break;
                case STATE_FINISH:
                    if (Md5Utils.getMd5ByFile(new File(mDownloadBean.getSavePath() + "/" + mDownloadBean.getFileName()))
                            .equals(mRecommendReceiveMap.get(taskId).getMD5())) {
                        InstallAppManager.getInstance().addRecommendReceiveMap(taskId, mRecommendReceiveMap.get(taskId));
                    } else {
                        mDownloadBean.setLoadState(STATE_INSTALL_FAIL);
                        notifyDownloadUpdate(taskId);
                    }
                    break;
            }
            if (mDownloadCountListener != null) {
                mDownloadCountListener.onDownloadCountChange(mIsDownLoadingMap.size());
            }
        }
    }


    /**
     * 注册监听器
     *
     * @param taskId
     *         下载对象ID
     */
    public void registerObserver(String taskId, DownloadObserver observer) {
        if (!mDownloaderMap.containsKey(taskId)) {
            Loger.e("registerObserver don't have " + taskId + " task");
        } else {
            mDownloaderMap.get(taskId).getDownloadBean().registerObserver(observer);
        }
    }

    /**
     * 移除监听器
     *
     * @param taskId
     *         下载对象ID
     */
    public void removeObserver(String taskId, DownloadObserver observer) {
        if (!mDownloaderMap.containsKey(taskId)) {
            Loger.e("removeObserver don't have " + taskId + " task");
        } else {
            mDownloaderMap.get(taskId).getDownloadBean().removeObserver(observer);
        }
    }

    private DownloadCountListener mDownloadCountListener;
    private UpdateCountListener mUpdateCountListener;

    /**
     * 当前下载任务的数量
     */
    public void setDownloadCountListener(DownloadCountListener mDownloadCountListener) {
        this.mDownloadCountListener = mDownloadCountListener;
    }

    /**
     * 当前更新任务的数量
     */
    public void setUpdateCountListener(UpdateCountListener mUpdateCountListener) {
        this.mUpdateCountListener = mUpdateCountListener;
    }
}
