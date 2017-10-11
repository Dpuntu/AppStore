package com.seuic.app.store.greendao;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.net.download.DownloadBean;

import java.util.List;

/**
 * Created on 2017/9/23.
 *
 * @author dpuntu
 */

public class GreenDaoManager {
    private static GreenDaoManager sGreenDaoManager;
    private static String DB_NAME = "appstore.db";
    private static DaoMaster.DevOpenHelper helper;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private static DownloadTaskTableDao mDownloadTaskTableDao;
    private static RecommendReceiveTableDao mRecommendReceiveTableDao;
    private static CheckUpdateAppsTableDao mCheckUpdateAppsTableDao;
    private static SearchHistoryTableDao mSearchHistoryTableDao;
    private static TypeAppsTableDao mTypeAppsTableDao;

    private GreenDaoManager() {
    }

    public static GreenDaoManager getInstance() {
        if (sGreenDaoManager == null) {
            helper = new DaoMaster.DevOpenHelper(AppStoreApplication.getApp(), DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
            daoSession = daoMaster.newSession();
            sGreenDaoManager = new GreenDaoManager();
        }

        mDownloadTaskTableDao = daoSession.getDownloadTaskTableDao();
        mRecommendReceiveTableDao = daoSession.getRecommendReceiveTableDao();
        mCheckUpdateAppsTableDao = daoSession.getCheckUpdateAppsTableDao();
        mSearchHistoryTableDao = daoSession.getSearchHistoryTableDao();
        mTypeAppsTableDao = daoSession.getTypeAppsTableDao();

        return sGreenDaoManager;
    }

    public void insertTypeAppsTableDao(RecommendReceive recommendReceive, String typeName, String typeId) {
        TypeAppsTable mTypeAppsTable = new TypeAppsTable();
        mTypeAppsTable.setAppName(recommendReceive.getAppName());
        mTypeAppsTable.setPackageName(recommendReceive.getPackageName());
        mTypeAppsTable.setAppSize(recommendReceive.getAppSize());
        mTypeAppsTable.setAppVersion(recommendReceive.getAppVersion());
        mTypeAppsTable.setAppVersionId(recommendReceive.getAppVersionId());
        mTypeAppsTable.setAppVersionDesc(recommendReceive.getAppVersionDesc());
        mTypeAppsTable.setAppDesc(recommendReceive.getAppDesc());
        mTypeAppsTable.setMD5(recommendReceive.getMD5());
        mTypeAppsTable.setDownloadName(recommendReceive.getDownloadName());
        mTypeAppsTable.setAppIconName(recommendReceive.getAppIconName());
        mTypeAppsTable.setAppTypeId(typeId);
        mTypeAppsTable.setAppTypeName(typeName);
        mTypeAppsTableDao.insertOrReplaceInTx(mTypeAppsTable);
    }

    public void insertDownloadTaskTable(DownloadBean downloadBean) {
        removeDownloadTaskTable(downloadBean.getTaskId());
        DownloadTaskTable mDownloadTaskTable = new DownloadTaskTable();
        mDownloadTaskTable.setTaskId(downloadBean.getTaskId());
        mDownloadTaskTable.setDownloadUrl(downloadBean.getDownloadUrl());
        mDownloadTaskTable.setSavePath(downloadBean.getSavePath());
        mDownloadTaskTable.setFileName(downloadBean.getFileName());
        mDownloadTaskTable.setLoadedLength(downloadBean.getLoadedLength());
        mDownloadTaskTable.setTotalSize(downloadBean.getTotalSize());
        mDownloadTaskTableDao.insertOrReplaceInTx(mDownloadTaskTable);
    }

    public void insertRecommendReceiveTableDao(RecommendReceive recommendReceive) {
        removeRecommendReceiveTable(recommendReceive.getAppVersionId());
        RecommendReceiveTable mRecommendReceiveTable = new RecommendReceiveTable();
        mRecommendReceiveTable.setAppName(recommendReceive.getAppName());
        mRecommendReceiveTable.setPackageName(recommendReceive.getPackageName());
        mRecommendReceiveTable.setAppSize(recommendReceive.getAppSize());
        mRecommendReceiveTable.setAppVersion(recommendReceive.getAppVersion());
        mRecommendReceiveTable.setAppVersionId(recommendReceive.getAppVersionId());
        mRecommendReceiveTable.setAppVersionDesc(recommendReceive.getAppVersionDesc());
        mRecommendReceiveTable.setAppDesc(recommendReceive.getAppDesc());
        mRecommendReceiveTable.setMD5(recommendReceive.getMD5());
        mRecommendReceiveTable.setDownloadName(recommendReceive.getDownloadName());
        mRecommendReceiveTable.setAppIconName(recommendReceive.getAppIconName());
        mRecommendReceiveTableDao.insertOrReplaceInTx(mRecommendReceiveTable);
    }

    public void insertCheckUpdateAppsTableDao(RecommendReceive recommendReceive) {
        CheckUpdateAppsTable mCheckUpdateAppsTable = new CheckUpdateAppsTable();
        mCheckUpdateAppsTable.setAppName(recommendReceive.getAppName());
        mCheckUpdateAppsTable.setPackageName(recommendReceive.getPackageName());
        mCheckUpdateAppsTable.setAppSize(recommendReceive.getAppSize());
        mCheckUpdateAppsTable.setAppVersion(recommendReceive.getAppVersion());
        mCheckUpdateAppsTable.setAppVersionId(recommendReceive.getAppVersionId());
        mCheckUpdateAppsTable.setAppVersionDesc(recommendReceive.getAppVersionDesc());
        mCheckUpdateAppsTable.setAppDesc(recommendReceive.getAppDesc());
        mCheckUpdateAppsTable.setMD5(recommendReceive.getMD5());
        mCheckUpdateAppsTable.setDownloadName(recommendReceive.getDownloadName());
        mCheckUpdateAppsTable.setAppIconName(recommendReceive.getAppIconName());
        mCheckUpdateAppsTableDao.insertOrReplaceInTx(mCheckUpdateAppsTable);
    }

    public void insertCheckUpdateAppsTableDao(List<RecommendReceive> recommendReceiveList) {
        // 添加任务前，删除之前所有的数据
        removeCheckUpdateAppsTableAll();
        for (RecommendReceive recommendReceive : recommendReceiveList) {
            insertCheckUpdateAppsTableDao(recommendReceive);
        }
    }

    public void insertSearchHistoryTableDao(String appName, String searchTime) {
        SearchHistoryTable searchHistoryTable = new SearchHistoryTable();
        searchHistoryTable.setAppName(appName);
        searchHistoryTable.setSearchTime(searchTime);
        mSearchHistoryTableDao.insertOrReplaceInTx(searchHistoryTable);
        List<SearchHistoryTable> searchHistoryTables = querySearchHistory();
        if (searchHistoryTables != null && searchHistoryTables.size() > 5) {
            removeSearchHistoryTable(searchHistoryTables.get(0).getAppName());//最大值存储5个，删掉最早的记录
        }
    }

    public void updateDownloadTaskTable(DownloadBean downloadBean) {
        DownloadTaskTable rDownloadTaskTable = queryDownloadTask(downloadBean.getTaskId());
        DownloadTaskTable mDownloadTaskTable = new DownloadTaskTable();
        mDownloadTaskTable.setId(rDownloadTaskTable.getId());
        mDownloadTaskTable.setTaskId(downloadBean.getTaskId());
        mDownloadTaskTable.setDownloadUrl(downloadBean.getDownloadUrl());
        mDownloadTaskTable.setSavePath(downloadBean.getSavePath());
        mDownloadTaskTable.setFileName(downloadBean.getFileName());
        mDownloadTaskTable.setLoadedLength(downloadBean.getLoadedLength());
        mDownloadTaskTable.setTotalSize(downloadBean.getTotalSize());
        mDownloadTaskTableDao.update(mDownloadTaskTable);
    }

    public void updateRecommendReceiveTableDao(RecommendReceive recommendReceive) {
        RecommendReceiveTable rRecommendReceiveTable = queryRecommendReceive(recommendReceive.getAppVersionId());
        RecommendReceiveTable mRecommendReceiveTable = new RecommendReceiveTable();
        mRecommendReceiveTable.setId(rRecommendReceiveTable.getId());
        mRecommendReceiveTable.setAppName(recommendReceive.getAppName());
        mRecommendReceiveTable.setPackageName(recommendReceive.getPackageName());
        mRecommendReceiveTable.setAppSize(recommendReceive.getAppSize());
        mRecommendReceiveTable.setAppVersion(recommendReceive.getAppVersion());
        mRecommendReceiveTable.setAppVersionId(recommendReceive.getAppVersionId());
        mRecommendReceiveTable.setAppVersionDesc(recommendReceive.getAppVersionDesc());
        mRecommendReceiveTable.setAppDesc(recommendReceive.getAppDesc());
        mRecommendReceiveTable.setMD5(recommendReceive.getMD5());
        mRecommendReceiveTable.setDownloadName(recommendReceive.getDownloadName());
        mRecommendReceiveTable.setAppIconName(recommendReceive.getAppIconName());
        mRecommendReceiveTableDao.update(mRecommendReceiveTable);
    }

    public DownloadTaskTable queryDownloadTask(String taskId) {
        return mDownloadTaskTableDao.queryBuilder().where(DownloadTaskTableDao.Properties.TaskId.eq(taskId)).unique();
    }


    public RecommendReceiveTable queryRecommendReceive(String taskId) {
        return mRecommendReceiveTableDao.queryBuilder().where(RecommendReceiveTableDao.Properties.AppVersionId.eq(taskId)).unique();
    }

    public RecommendReceiveTable queryRecommendReceiveByPackageName(String packageName) {
        return mRecommendReceiveTableDao.queryBuilder().where(RecommendReceiveTableDao.Properties.PackageName.eq(packageName)).unique();
    }

    public CheckUpdateAppsTable queryCheckUpdateApp(String taskId) {
        return mCheckUpdateAppsTableDao.queryBuilder().where(CheckUpdateAppsTableDao.Properties.AppVersionId.eq(taskId)).unique();
    }

    public List<TypeAppsTable> queryTypeAppsTable(String typeName, String typeId) {
        return mTypeAppsTableDao.queryBuilder()
                .where(TypeAppsTableDao.Properties.AppTypeName.eq(typeName))
                .where(TypeAppsTableDao.Properties.AppTypeId.eq(typeId))
                .list();
    }

    public List<CheckUpdateAppsTable> queryCheckUpdateApps() {
        return mCheckUpdateAppsTableDao.queryBuilder().list();
    }

    public List<DownloadTaskTable> queryDownloadTaskTable() {
        return mDownloadTaskTableDao.queryBuilder().list();
    }

    public List<RecommendReceiveTable> queryRecommendReceiveTable() {
        List<RecommendReceiveTable> recommendReceiveTables = mRecommendReceiveTableDao.queryBuilder().list();
        if (recommendReceiveTables == null || recommendReceiveTables.size() <= 0) {
            return null;
        } else {
            return recommendReceiveTables;
        }
    }

    public SearchHistoryTable querySearchHistory(String appName) {
        return mSearchHistoryTableDao.queryBuilder().where(SearchHistoryTableDao.Properties.AppName.eq(appName)).unique();
    }

    public List<SearchHistoryTable> querySearchHistory() {
        return mSearchHistoryTableDao.queryBuilder().list();
    }

    public void removeSearchHistoryTable(String appName) {
        mSearchHistoryTableDao.delete(querySearchHistory(appName));
    }

    public void removeDownloadTaskTable(String taskId) {
        DownloadTaskTable downloadTaskTable = queryDownloadTask(taskId);
        if (downloadTaskTable != null) {
            mDownloadTaskTableDao.delete(downloadTaskTable);
        }
    }

    public void removeRecommendReceiveTable(String taskId) {
        RecommendReceiveTable recommendReceiveTable = queryRecommendReceive(taskId);
        if (recommendReceiveTable != null) {
            mRecommendReceiveTableDao.delete(recommendReceiveTable);
        }
    }

    public void removeCheckUpdateAppsTable(String taskId) {
        CheckUpdateAppsTable mCheckUpdateAppsTable = queryCheckUpdateApp(taskId);
        if (mCheckUpdateAppsTable != null) {
            mCheckUpdateAppsTableDao.delete(mCheckUpdateAppsTable);
        }
    }

    public void removeDownloadTaskTableAll() {
        mDownloadTaskTableDao.deleteAll();
    }

    public void removeRecommendReceiveTableAll() {
        mRecommendReceiveTableDao.deleteAll();
    }

    public void removeCheckUpdateAppsTableAll() {
        mCheckUpdateAppsTableDao.deleteAll();
    }

    public void removeTypeAppsTableAll() {
        mTypeAppsTableDao.deleteAll();
    }

    public RecommendReceive table2RecommendReceive(RecommendReceiveTable recommendReceiveTable) {
        if (recommendReceiveTable != null) {
            return new RecommendReceive(recommendReceiveTable.getAppName(),
                                        recommendReceiveTable.getPackageName(),
                                        recommendReceiveTable.getAppSize(),
                                        recommendReceiveTable.getAppVersion(),
                                        recommendReceiveTable.getAppVersionId(),
                                        recommendReceiveTable.getAppVersionDesc(),
                                        recommendReceiveTable.getAppDesc(),
                                        recommendReceiveTable.getMD5(),
                                        recommendReceiveTable.getDownloadName(),
                                        recommendReceiveTable.getAppIconName());
        } else {
            return null;
        }
    }
}
