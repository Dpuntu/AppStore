package com.seuic.app.store.ui.presenter;

import android.support.v4.util.SimpleArrayMap;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.adapter.BaseRecycleViewAdapter;
import com.seuic.app.store.bean.DownloadingBean;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleTitleMoreBean;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.net.download.DownloadBean;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.net.download.task.OkHttpDownloader;
import com.seuic.app.store.ui.contact.DownLoadContact;

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
        if (downLoadCount > 0) {
            initDownloadCount(recycleObjects, downLoadCount);
            SimpleArrayMap<String, OkHttpDownloader> mIsDownLoadingMap = DownloadManager.getInstance().getIsDownLoadingMap();
            for (int i = 0; i < mIsDownLoadingMap.size(); i++) {
                DownloadBean downloadBean = mIsDownLoadingMap.get(mIsDownLoadingMap.keyAt(i)).getDownloadBean();
                RecommendReceive recommendReceive = GreenDaoManager.getInstance().table2RecommendReceive(
                        GreenDaoManager.getInstance().queryRecommendReceive(mIsDownLoadingMap.keyAt(i)));
                recycleObjects.add(new RecycleObject(BaseRecycleViewAdapter.RecycleViewType.RECYCLE_DATA,
                                                     new DownloadingBean(recommendReceive.getAppName(),
                                                                         downloadBean.getTaskId(),
                                                                         downloadBean.getLoadedLength(),
                                                                         downloadBean.getTotalSize(),
                                                                         recommendReceive.getAppIconName(),
                                                                         recommendReceive.getPackageName())));
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
        recycleObjects.add(new RecycleObject(BaseRecycleViewAdapter.RecycleViewType.RECYCLE_TITLE,
                                             new RecycleTitleMoreBean(
                                                     AppStoreApplication.getApp()
                                                             .getString(R.string.download_count, count),
                                                     false, "")));
    }
}
