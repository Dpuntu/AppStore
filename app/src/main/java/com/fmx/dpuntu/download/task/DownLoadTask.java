package com.fmx.dpuntu.download.task;

import android.util.Log;

import com.fmx.dpuntu.download.DownLoadInfo;
import com.fmx.dpuntu.download.DownloadState;
import com.fmx.dpuntu.utils.AndroidUtils;
import com.fmx.dpuntu.utils.Loger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class DownLoadTask implements Runnable {
    private DownLoadInfo info;
    private OkHttpClient mClient;
    private DownLoader downLoader;
    private static final int BUFFER_SIZE = 1024;

    public DownLoadTask(DownLoader downLoader, DownLoadInfo info, OkHttpClient mClient) {
        this.downLoader = downLoader;
        this.info = info;
        this.mClient = mClient;
    }

    @Override
    public void run() {
        info.setDownloadState(DownloadState.STATE_START);
        String fileName = AndroidUtils.fileName(info.getAppName(), info.getAppVersion());
        File file = new File(info.getFilePath() + fileName);
        if (file.exists() && file.length() != info.getAppSize()) {
            if (file.delete()) {
                info.setDownloadSize(0);
                downLoader.notifyDownloadUpdate(info);
            } else {
                error(-1);
            }
        } else {
            downLoader.notifyDownloadUpdate(info);
        }

        File dic = new File(info.getFilePath());
        if (!dic.exists()) {
            if (dic.mkdirs()) {
                Log.i(TAG, "目录创建成功");
            }
        }
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    Log.i(TAG, "文件创建成功");
                    download(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            download(file);
        }
    }

    private void download(final File file) {
        InputStream in = null;
        long appSize = 0;
        if (mClient == null) {
            mClient = new OkHttpClient.Builder()
                    .connectTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
//                    .addInterceptor(new CustomInterceptor())
                    .build();
        }
        Request.Builder requestBuilder = new Request.Builder()
                .url(info.getDownLoadUrl());
        requestBuilder.method("GET", null);
        Request request = null;
        if (info.getAppSize() > 0) {
            Loger.d("getAppSize > 0");
            request = requestBuilder
                    //.addHeader("Range", "bytes=" + info.getAppSize() + "-" + info.getDownloadSize())
                    .build();
        }
        Call call = mClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                Loger.d("isSuccessful");
                in = response.body().byteStream();
                appSize = response.body().contentLength();
                writeFile(in, file, appSize);
            }
        } catch (IOException e) {
            error(-3);
            e.printStackTrace();
        }
    }

    private void writeFile(InputStream in, File file, long appSize) {
        if (in != null) {
            Loger.d("!= null");
            FileOutputStream fos = null;
            byte[] buf = new byte[BUFFER_SIZE];
            try {
                fos = new FileOutputStream(file, true);
                int len;
                long completed = info.getDownloadSize();
                if (appSize > 0 && info.getAppSize() != appSize) {
                    info.setAppSize(appSize);
                }
                downLoader.notifyDownloadUpdate(info);
                while (info.getDownloadSize() < info.getAppSize()
                        && info.getAppSize() > 0
                        && (len = in.read(buf)) != -1) {
                    if (info.getDownloadState() == DownloadState.STATE_DOWNLOADING) {
                        fos.write(buf, 0, len);
                        completed += len;
                        info.setDownloadSize(completed);
                        downLoader.notifyDownloadUpdate(info);
                        fos.flush();
                    }
                }
                fos.close();
            } catch (FileNotFoundException e) {
                error(-4);
                e.printStackTrace();
            } catch (IOException e) {
                error(-5);
                e.printStackTrace();
            }
        } else {
            error(-6);
        }
    }

    private void error(long error) {
        info.setDownloadState(DownloadState.STATE_ERROR);
        info.setDownloadSize(error);
        downLoader.notifyDownloadUpdate(info);
    }
}
