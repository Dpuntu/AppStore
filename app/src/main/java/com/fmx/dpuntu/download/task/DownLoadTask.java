package com.fmx.dpuntu.download.task;

import android.util.Log;

import com.fmx.dpuntu.download.DownLoadInfo;
import com.fmx.dpuntu.download.DownloadState;
import com.fmx.dpuntu.utils.Loger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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
        String fileName = info.getAppName() + "_" + info.getAppVersion() + ".apk";
        File file = new File(info.getFilePath() + fileName);
        if (file.exists() && file.length() != info.getAppSize()) {
            if (file.delete()) {
                info.setDownloadSize(0);
                downLoader.notifyDownloadUpdate(info);
            } else {
                error(-1);
            }
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
                writeFile(in, file);
            }
        } catch (IOException e) {
            error(-3);
            e.printStackTrace();
            return;
        }
    }

    private void writeFile(InputStream in, File file) {
        if (in != null) {
            Loger.d("!= null");
            RandomAccessFile raf = null;
            BufferedInputStream buffer;
            byte[] buf = new byte[BUFFER_SIZE];
            try {
                raf = new RandomAccessFile(file, "rw");
                raf.seek(info.getDownloadSize());
                buffer = new BufferedInputStream(in);
                int len;
                long completed = info.getDownloadSize();
                Loger.d("completed = " + completed);
                while (info.getDownloadSize() < info.getAppSize()
                        && info.getAppSize() > 0
                        && (len = buffer.read(buf, 0, BUFFER_SIZE)) != -1) {
                    if (info.getDownloadState() == DownloadState.STATE_DOWNLOADING) {
                        Loger.d("len = " + len);
                        raf.write(buf, 0, len);
                        completed += len;
                        info.setDownloadSize(completed);
                        downLoader.notifyDownloadUpdate(info);
                    }
                }
            } catch (FileNotFoundException e) {
                error(-4);
                e.printStackTrace();
                return;
            } catch (IOException e) {
                error(-5);
                e.printStackTrace();
                return;
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
