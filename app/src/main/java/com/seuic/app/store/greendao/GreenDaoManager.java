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
    private static DataUsageTableDao mDataUsageTableDao;

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
        mDataUsageTableDao = daoSession.getDataUsageTableDao();

        return sGreenDaoManager;
    }

    /**
     * 插入App到APP类型数据库
     *
     * @param recommendReceive
     *         网络APP的详细数据
     * @param typeName
     *         APP分类名称
     * @param typeId
     *         APP分类的ID
     */
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

    /**
     * 插入下载数据到下载数据库
     *
     * @param downloadBean
     *         下载数据
     */
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

    /**
     * 插入App到所有APP数据库
     *
     * @param recommendReceive
     *         网络APP的详细数据
     */
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

    /**
     * 插入App到更新APP数据库
     *
     * @param recommendReceive
     *         网络APP的详细数据
     */
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

    /**
     * 插入App集合到更新APP数据库
     *
     * @param recommendReceiveList
     *         网络APP的详细数据的集合
     */
    public void insertCheckUpdateAppsTableDao(List<RecommendReceive> recommendReceiveList) {
        // 添加任务前，删除之前所有的数据
        removeCheckUpdateAppsTableAll();
        for (RecommendReceive recommendReceive : recommendReceiveList) {
            insertCheckUpdateAppsTableDao(recommendReceive);
        }
    }

    /**
     * 插入搜索数据到搜索数据库
     *
     * @param appName
     *         搜索的名字
     * @param searchTime
     *         搜索的时间
     */
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

    /**
     * 插入APP的流量统计
     *
     * @param packageName
     *         app的包名
     * @param rxBytes
     *         app下载的流量数据大小，单位byte
     * @param txBytes
     *         app上传的流量数据大小，单位byte
     * @param onceTime
     *         统计类型
     *         ONCE 本次开机数据
     *         FINAL 永久数据
     */
    public void insertDataUsageTableDao(String packageName, long rxBytes, long txBytes, int onceTime) {
        DataUsageTable mDataUsageTable = queryDataUsageTable(packageName, onceTime);
        if (mDataUsageTable == null) {
            mDataUsageTable = new DataUsageTable();
        }
        mDataUsageTable.setPackageName(packageName);
        mDataUsageTable.setUidRxBytes(rxBytes);
        mDataUsageTable.setUidTxBytes(txBytes);
        mDataUsageTable.setOnceTime(onceTime);
        mDataUsageTableDao.insertOrReplaceInTx(mDataUsageTable);
    }

    /**
     * 更新下载数据到下载数据库
     *
     * @param downloadBean
     *         下载数据
     */
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

    /**
     * 更新App到所有APP数据库
     *
     * @param recommendReceive
     *         网络APP的详细数据
     */
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

    /**
     * 查询下载任务表
     *
     * @param taskId
     *         任务ID
     */
    public DownloadTaskTable queryDownloadTask(String taskId) {
        return mDownloadTaskTableDao.queryBuilder().where(DownloadTaskTableDao.Properties.TaskId.eq(taskId)).unique();
    }

    /**
     * 查询APP
     *
     * @param taskId
     *         任务ID
     */
    public RecommendReceiveTable queryRecommendReceive(String taskId) {
        return mRecommendReceiveTableDao.queryBuilder().where(RecommendReceiveTableDao.Properties.AppVersionId.eq(taskId)).unique();
    }

    /**
     * 查询APP
     *
     * @param packageName
     *         包名
     */
    public RecommendReceiveTable queryRecommendReceiveByPN(String packageName) {
        return mRecommendReceiveTableDao.queryBuilder().where(RecommendReceiveTableDao.Properties.PackageName.eq(packageName)).unique();
    }

    /**
     * 查询APP
     *
     * @param packageName
     *         包名
     */
    public RecommendReceiveTable queryRecommendReceiveByPackageName(String packageName) {
        return mRecommendReceiveTableDao.queryBuilder().where(RecommendReceiveTableDao.Properties.PackageName.eq(packageName)).unique();
    }

    /**
     * 查询更新APP
     *
     * @param taskId
     *         任务ID
     */
    public CheckUpdateAppsTable queryCheckUpdateApp(String taskId) {
        return mCheckUpdateAppsTableDao.queryBuilder().where(CheckUpdateAppsTableDao.Properties.AppVersionId.eq(taskId)).unique();
    }

    /**
     * 查询某一类的APP集合
     *
     * @param typeName
     *         分类名称
     * @param typeId
     *         分类ID
     */
    public List<TypeAppsTable> queryTypeAppsTable(String typeName, String typeId) {
        return mTypeAppsTableDao.queryBuilder()
                .where(TypeAppsTableDao.Properties.AppTypeName.eq(typeName))
                .where(TypeAppsTableDao.Properties.AppTypeId.eq(typeId))
                .list();
    }

    /**
     * 查询所有的更新App
     */
    public List<CheckUpdateAppsTable> queryCheckUpdateApps() {
        return mCheckUpdateAppsTableDao.queryBuilder().list();
    }

    /**
     * 查询所有下载任务
     */
    public List<DownloadTaskTable> queryDownloadTaskTable() {
        return mDownloadTaskTableDao.queryBuilder().list();
    }

    /**
     * 查询所有网络App
     */
    public List<RecommendReceiveTable> queryRecommendReceiveTable() {
        List<RecommendReceiveTable> recommendReceiveTables = mRecommendReceiveTableDao.queryBuilder().list();
        if (recommendReceiveTables == null || recommendReceiveTables.size() <= 0) {
            return null;
        } else {
            return recommendReceiveTables;
        }
    }

    /**
     * 查询搜索历史
     *
     * @param appName
     *         搜索关键字
     */
    public SearchHistoryTable querySearchHistory(String appName) {
        return mSearchHistoryTableDao.queryBuilder().where(SearchHistoryTableDao.Properties.AppName.eq(appName)).unique();
    }

    /**
     * 查询所有历史
     */
    public List<SearchHistoryTable> querySearchHistory() {
        return mSearchHistoryTableDao.queryBuilder().list();
    }

    /**
     * 查询某个App的某一类流量
     *
     * @param packageName
     *         app包名
     * @param onceTime
     *         统计类型
     *         ONCE 本次开机数据
     *         FINAL 永久数据
     */
    public DataUsageTable queryDataUsageTable(String packageName, int onceTime) {
        return mDataUsageTableDao.queryBuilder()
                .where(DataUsageTableDao.Properties.PackageName.eq(packageName))
                .where(DataUsageTableDao.Properties.OnceTime.eq(onceTime)).unique();
    }

    /**
     * 移除某个历史
     *
     * @param appName
     *         搜索关键字
     */
    public void removeSearchHistoryTable(String appName) {
        mSearchHistoryTableDao.delete(querySearchHistory(appName));
    }

    /**
     * 移除某个下载任务
     *
     * @param taskId
     *         任务ID
     */
    public void removeDownloadTaskTable(String taskId) {
        DownloadTaskTable downloadTaskTable = queryDownloadTask(taskId);
        if (downloadTaskTable != null) {
            mDownloadTaskTableDao.delete(downloadTaskTable);
        }
    }

    /**
     * 移除某个网络App
     *
     * @param taskId
     *         任务ID
     */
    public void removeRecommendReceiveTable(String taskId) {
        RecommendReceiveTable recommendReceiveTable = queryRecommendReceive(taskId);
        if (recommendReceiveTable != null) {
            mRecommendReceiveTableDao.delete(recommendReceiveTable);
        }
    }

    /**
     * 移除某个更新App
     *
     * @param taskId
     *         任务ID
     */
    public void removeCheckUpdateAppsTable(String taskId) {
        CheckUpdateAppsTable mCheckUpdateAppsTable = queryCheckUpdateApp(taskId);
        if (mCheckUpdateAppsTable != null) {
            mCheckUpdateAppsTableDao.delete(mCheckUpdateAppsTable);
        }
    }

    /**
     * 移除某个App的某类流量统计
     *
     * @param packageName
     *         包名
     * @param onceTime
     *         统计类型
     *         ONCE 本次开机数据
     *         FINAL 永久数据
     */
    public void removeDataUsageTableDao(String packageName, int onceTime) {
        DataUsageTable mDataUsageTable = queryDataUsageTable(packageName, onceTime);
        if (mDataUsageTable != null) {
            mDataUsageTableDao.delete(mDataUsageTable);
        }
    }

    /**
     * 移除所有的下载任务
     */
    public void removeDownloadTaskTableAll() {
        mDownloadTaskTableDao.deleteAll();
    }

    /**
     * 移除所有的网络App
     */
    public void removeRecommendReceiveTableAll() {
        mRecommendReceiveTableDao.deleteAll();
    }

    /**
     * 移除所有的更新App
     */
    public void removeCheckUpdateAppsTableAll() {
        mCheckUpdateAppsTableDao.deleteAll();
    }

    /**
     * 移除所有的分类App
     */
    public void removeTypeAppsTableAll() {
        mTypeAppsTableDao.deleteAll();
    }

    /**
     * 将网络APP读取的表信息转为RecommendReceive类型
     */
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
