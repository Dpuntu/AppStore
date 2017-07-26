package com.fmx.dpuntu.ui.dialog;

import com.fmx.dpuntu.download.DownLoadInfo;

/**
 * Created on 2017/7/14.
 *
 * @author dpuntu
 */

public class DialogContract {
    public interface Presenter {
        void startDownLoad();

        void onStart();

        void onFinish();

        void onDownLoadProgress(DownLoadInfo info);

        void onDownLoadStart(DownLoadInfo info);

        void onDownLoadFail(DownLoadInfo info);

        void onDownLoadFinish(DownLoadInfo info);

        void onDownLoadPause(DownLoadInfo info);

        void onDownLoadStop(DownLoadInfo info);
    }

    public interface View {
        void startDownLoad();
    }
}
