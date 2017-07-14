package com.fmx.dpuntu.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import com.fmx.dpuntu.download.DownLoadInfo;
import com.fmx.dpuntu.download.DownLoadListener;
import com.fmx.dpuntu.download.DownloadState;
import com.fmx.dpuntu.download.task.DownLoader;
import com.fmx.dpuntu.utils.AndroidUtils;
import com.fmx.dpuntu.utils.Loger;

import java.io.File;

/**
 * Created on 2017/7/14.
 *
 * @author dpuntu
 */

public class DialogPresenter implements DialogContact.Presenter {
    public static final int DOWN_PROGRESS = 0;
    public static final int DOWN_START = 1;
    public static final int DOWN_PAUSE = 2;
    public static final int DOWN_STOP = 3;
    public static final int DOWN_ERROR = 4;
    public static final int DOWN_FINISH = 5;

    private DialogActivity view;
    private Handler mHandler;

    public DialogPresenter(DialogActivity view, Handler mHandler) {
        this.view = view;
        this.mHandler = mHandler;
    }

    @Override
    public void startDownLoad() {
        String appUrl = AndroidUtils.decrypt(view.getInfo().getAppUrl(), view.getInfo().getAppHashCode().substring(0, 8));
        Loger.d("下载:" + view.getInfo().getAppName() + ", 地址:" + appUrl);
        Loger.d("下載到:" + Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
        DownLoadInfo mDownLoadInfo = new DownLoadInfo.Build()
                .setAppName(view.getInfo().getAppName())
                .setAppSize(AndroidUtils.exchangeSize(view.getInfo().getAppSize()))
                .setAppVersion(view.getInfo().getAppVersion())
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
    }

    @Override
    public void onStart() {
        view.showDefaultDialog("下载提示", "准备下载" + view.getInfo().getAppName()
                                       + "\r\n版本" + view.getInfo().getAppVersion()
                                       + "\r\n预计消耗流量" + view.getInfo().getAppSize()
                                       + "\r\n本次软件由" + view.getInfo().getAppComment() + "提供"
                                       + "\r\n是否继续下载?",
                               new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       startDownLoad();
                                   }
                               });
    }

    @Override
    public void onFinish() {
        view.showDefaultDialog("程序出错", "请返回上一页", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.finish();
            }
        });
    }

    @Override
    public void onDownLoadProgress(DownLoadInfo info) {
        view.showDownloadtDialog("正在下载" + info.getAppName() + "...", info, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.finish();
            }
        });

        Loger.i("onProgress , " + info.getDownloadSize() * 100 / info.getAppSize() + "%");
    }

    @Override
    public void onDownLoadStart(DownLoadInfo info) {
        Loger.i("onStart , " + info.getDownLoadUrl());
    }

    @Override
    public void onDownLoadFail(DownLoadInfo info) {
        view.showDefaultDialog(info.getAppName() + "下载出错",
                               info.getAppName() + "_" + info.getAppVersion() + ".apk",
                               new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       view.finish();
                                   }
                               });
        Loger.i("onError , " + info.getDownloadSize());
    }

    @Override
    public void onDownLoadFinish(final DownLoadInfo info) {
        view.showDefaultDialog(info.getAppName() + "下载完成",
                               "是否立即安装" + info.getAppName() + "_" + info.getAppVersion() + ".apk",
                               new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       installApk(info.getFilePath()
                                                          + AndroidUtils.fileName(info.getAppName(),
                                                                                  info.getAppVersion())
                                       );
                                   }
                               });
        Loger.i("onFinish , " + info.getDownloadSize());
    }

    private void installApk(String file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file),
                              "application/vnd.android.package-archive");
        view.startActivity(intent);
    }

    @Override
    public void onDownLoadPause(DownLoadInfo info) {
        Loger.i("onPause , " + info.getDownloadSize());
    }

    @Override
    public void onDownLoadStop(DownLoadInfo info) {
        Loger.i("onStop , " + info.getDownloadSize());
    }

    public DownLoadListener mListener = new DownLoadListener() {
        @Override
        public void onProgress(DownLoadInfo info) {
            mHandler.obtainMessage(DOWN_PROGRESS, info).sendToTarget();
        }

        @Override
        public void onStart(DownLoadInfo info) {
            mHandler.obtainMessage(DOWN_START, info).sendToTarget();
        }

        @Override
        public void onPause(DownLoadInfo info) {
            mHandler.obtainMessage(DOWN_PAUSE, info).sendToTarget();
        }

        @Override
        public void onStop(DownLoadInfo info) {
            mHandler.obtainMessage(DOWN_STOP, info).sendToTarget();
        }

        @Override
        public void onError(DownLoadInfo info) {
            mHandler.obtainMessage(DOWN_ERROR, info).sendToTarget();
        }

        @Override
        public void onFinish(DownLoadInfo info) {
            mHandler.obtainMessage(DOWN_FINISH, info).sendToTarget();

        }
    };
}
