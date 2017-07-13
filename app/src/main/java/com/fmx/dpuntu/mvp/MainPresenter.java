package com.fmx.dpuntu.mvp;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fmx.dpuntu.api.Apimanager;
import com.fmx.dpuntu.api.AppListResponse;
import com.fmx.dpuntu.api.Response;
import com.fmx.dpuntu.download.DownLoadInfo;
import com.fmx.dpuntu.download.DownLoadListener;
import com.fmx.dpuntu.download.DownloadState;
import com.fmx.dpuntu.download.task.DownLoader;
import com.fmx.dpuntu.utils.AndroidUtils;
import com.fmx.dpuntu.utils.AppInfo;
import com.fmx.dpuntu.utils.AppRecyclerViewAdapter;
import com.fmx.dpuntu.utils.Loger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class MainPresenter implements MainContact.Presenter {
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;
    private static final int DOWNLOAD = 2;

    private MainActivity view;
    private ArrayList<AppInfo> appInfos;
    private RecyclerView.LayoutManager mLayoutManager;

    public MainPresenter(MainContact.View view) {
        this.view = (MainActivity) view;
    }

    public void initView() {
        appInfos = getAppsList();
        if (appInfos.size() > 0) {
            mLayoutManager = new LinearLayoutManager(view);
            view.getRecyclerView().setLayoutManager(mLayoutManager);
            AppRecyclerViewAdapter mAppRecyclerViewAdapter = new AppRecyclerViewAdapter(view, appInfos);
            view.getRecyclerView().setAdapter(mAppRecyclerViewAdapter);
        }
    }

    private AppRecyclerViewAdapter.RecyclerViewClickListener mRecyclerViewClickListener = new AppRecyclerViewAdapter.RecyclerViewClickListener() {
        @Override
        public void onClick(View v, AppListResponse.DownloadAppInfo info) {
            mHandler.obtainMessage(DOWNLOAD, info).sendToTarget();
        }
    };

    public void fromNetToList() {
        view.getDownLoadAppsList();
    }

    @Override
    public ArrayList<AppInfo> getAppsList() {
        return view.getAppsList();
    }


    @Override
    public void getDownLoadAppsList() {
        Loger.i("--- getDownLoadAppsList ---");
        Apimanager.getApiManager()
                .getApiService()
                .appList("709BW_ZH_82", "866830020046093", "dj_w790", "1.6.4", "1", "20")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Response<AppListResponse>>() {
                    @Override
                    public boolean test(@NonNull Response<AppListResponse> response) throws Exception {
                        Loger.d("result = " + response.getResult());
                        if (response.getResult().equals("true")) {
                            return true;
                        }
                        return false;
                    }
                })
                .subscribe(new Observer<Response<AppListResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<AppListResponse> response) {
                        List<AppListResponse.DownloadAppInfo> appinfos = response.getData().getRows();
                        mHandler.obtainMessage(SUCCESS, appinfos).sendToTarget();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Loger.i("--- onError ---");
                        mHandler.obtainMessage(FAIL, e).sendToTarget();
                    }

                    @Override
                    public void onComplete() {
                        Loger.i("--- onComplete ---");
                    }
                });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    List<AppListResponse.DownloadAppInfo> appInfos = (List<AppListResponse.DownloadAppInfo>) msg.obj;
                    if (appInfos.size() > 0) {
                        mLayoutManager = new LinearLayoutManager(view);
                        view.getRecyclerView().setLayoutManager(mLayoutManager);
                        view.getRecyclerView().addItemDecoration(new RecyclerViewDecoration(view, RecyclerViewDecoration.VERTICAL_LIST));
                        AppRecyclerViewAdapter mAppRecyclerViewAdapter = new AppRecyclerViewAdapter(view, appInfos);
                        mAppRecyclerViewAdapter.serRecyclerViewClickListener(mRecyclerViewClickListener);
                        view.getRecyclerView().setAdapter(mAppRecyclerViewAdapter);
                    }
                    break;
                case FAIL:
                    Throwable e = (Throwable) msg.obj;
                    e.printStackTrace();
                    break;
                case DOWNLOAD:
                    AppListResponse.DownloadAppInfo info = (AppListResponse.DownloadAppInfo) msg.obj;
                    String appUrl = AndroidUtils.decrypt(info.getAppUrl(), info.getAppHashCode().substring(0, 8));
                    Loger.d("下载:" + info.getAppName() + ", 地址:" + appUrl);
                    Loger.d("下載到:" + Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);

                    DownLoadInfo mDownLoadInfo = new DownLoadInfo.Build()
                            .setAppName(info.getAppName())
                            .setAppSize(AndroidUtils.exchangeSize(info.getAppSize()))
                            .setAppVersion(info.getAppVersion())
                            .setDownloadSize(0)
                            .setDownLoadUrl(appUrl)
                            .setDownloadState(DownloadState.STATE_DEFAULT)
                            .setFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator)
                            .setDownLoadListener(mListener)
                            .build();

                    DownLoader mDownLoader = new DownLoader();
                    new DownLoader.Builder(mDownLoader)
                            .setKeepAliveTime(10)
                            .setMaxTaskSize(3)
                            .setDownLoadBean(mDownLoadInfo)
                            .build();
                    mDownLoader.start();
                    break;
            }
        }
    };

    public DownLoadListener mListener = new DownLoadListener() {
        @Override
        public void onProgress(DownLoadInfo info) {
            Loger.i("onProgress , " + info.getDownloadSize() + "/" + info.getAppSize());
            Loger.i("onProgress , " + info.getDownloadSize() * 100 / info.getAppSize() + "%");
        }

        @Override
        public void onStart(DownLoadInfo info) {
            Loger.i("onStart , " + info.getDownLoadUrl());
        }

        @Override
        public void onPause(DownLoadInfo info) {
            Loger.i("onPause , " + info.getDownloadSize());
        }

        @Override
        public void onStop(DownLoadInfo info) {
            Loger.i("onStop , " + info.getDownloadSize());
        }

        @Override
        public void onError(DownLoadInfo info) {
            Loger.i("onError , " + info.getDownloadSize());
        }
    };
}
