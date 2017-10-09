package com.seuic.app.store.ui.presenter;

import android.support.v4.util.SimpleArrayMap;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.bean.DownloadingBean;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleTitleMoreBean;
import com.seuic.app.store.bean.RecycleViewType;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.greendao.DownloadTaskTable;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.greendao.RecommendReceiveTable;
import com.seuic.app.store.net.download.DownloadBean;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.net.download.task.OkhttpDownloader;
import com.seuic.app.store.ui.contact.DownLoadContact;
import com.seuic.app.store.utils.Loger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 */

public class DownLoadPresenter implements DownLoadContact.Presenter {
    private DownLoadContact.View mView;

    public DownLoadPresenter(DownLoadContact.View view) {
        mView = view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void queryDownLoadData(boolean isRefresh) {
        List<RecycleObject> recycleObjects = new ArrayList<>();
        int downLoadCount = DownloadManager.getInstance().getDownloaderTaskCount();
        if (downLoadCount > 0) { // 进入下载页面后，先取下载管理中的数据，有的话就加载
            initDownloadCount(recycleObjects, downLoadCount);
            SimpleArrayMap<String, OkhttpDownloader> okhttpDownloaderMap = DownloadManager.getInstance().getDownloadingMap();
            SimpleArrayMap<String, RecommendReceive> recommendReceiveMap = DownloadManager.getInstance().getRecommendReceiveMap();
            for (int i = 0; i < okhttpDownloaderMap.size(); i++) {
                DownloadBean downloadBean = okhttpDownloaderMap.get(okhttpDownloaderMap.keyAt(i)).getDownloadBean();
                RecommendReceive recommendReceive = recommendReceiveMap.get(okhttpDownloaderMap.keyAt(i));
                recycleObjects.add(new RecycleObject(RecycleViewType.RECYCEL_DATA,
                                                     new DownloadingBean(recommendReceive.getAppName(),
                                                                         downloadBean.getTaskId(),
                                                                         downloadBean.getLoadedLength(),
                                                                         downloadBean.getTotalSize(),
                                                                         recommendReceive.getAppIconName(),
                                                                         recommendReceive.getPackageName())));
            }
        } else { // 进入下载页面后，下载管理中没有数据，就取数据库中的数据，并通过addDownloadingMap将数据添加到下载管理列表中
            List<DownloadTaskTable> downloadTaskTables = GreenDaoManager.getInstance().queryDownloadTaskTable();
            if (downloadTaskTables == null) {
                initDownloadCount(recycleObjects, 0);
            } else {
                initDownloadCount(recycleObjects, downloadTaskTables.size());
                for (DownloadTaskTable downloadTaskTable : downloadTaskTables) {
                    RecommendReceiveTable recommendReceiveTable = GreenDaoManager.getInstance().queryRecommendReceive(downloadTaskTable.getTaskId());
                    if (recommendReceiveTable != null) {
                        DownloadingBean mDownloadingBean = new DownloadingBean(recommendReceiveTable.getAppName(),
                                                                               downloadTaskTable.getTaskId(),
                                                                               downloadTaskTable.getLoadedLength(),
                                                                               downloadTaskTable.getTotalSize(),
                                                                               recommendReceiveTable.getAppIconName(),
                                                                               recommendReceiveTable.getPackageName());
                        recycleObjects.add(new RecycleObject(RecycleViewType.RECYCEL_DATA, mDownloadingBean));
                        DownloadManager.getInstance().addDownloadingMap(mDownloadingBean);
                    } else {
                        Loger.e("recommendReceiveTable is null , " + downloadTaskTable.getDownloadUrl());
                    }
                }
            }
        }
        // 是否刷新
        if (isRefresh) {
            mView.refreshRecycleView(recycleObjects);
        } else {
            mView.updateRecycleView(recycleObjects);
        }
    }

    @SuppressWarnings("unchecked")
    private void initDownloadCount(List<RecycleObject> recycleObjects, int count) {
        recycleObjects.add(new RecycleObject(RecycleViewType.RECYCEL_TITLE,
                                             new RecycleTitleMoreBean(
                                                     AppStoreApplication.getApp()
                                                             .getString(R.string.download_count, count),
                                                     false, "")));
    }
}
