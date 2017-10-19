package com.seuic.app.store.net.download.task;

import com.seuic.app.store.greendao.DownloadTaskTable;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.net.download.DownloadBean;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.net.download.DownloadState;
import com.seuic.app.store.utils.FileUtils;
import com.seuic.app.store.utils.HttpHeadUtils;
import com.seuic.app.store.utils.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class DownloadTask implements Runnable {
    private DownloadBean mDownloadBean;
    private OkHttpClient mClient;
    private static final int BUFFER_SIZE = 1024 * 1024;
    private File downloadFile;
    private InputStream stream = null;

    public DownloadTask() {
    }

    public void setDownloadTask(DownloadBean mDownloadBean, OkHttpClient client ) {
        this.mDownloadBean = mDownloadBean;
        oldTime = -1;
        oldLength = -1;
        mClient = client;
        FileUtils.createDownloadPath(mDownloadBean.getSavePath());
        downloadFile = new File(mDownloadBean.getSavePath(), mDownloadBean.getFileName());
        mDownloadBean.setLoadState(DownloadState.STATE_NEWTASK);
        DownloadManager.getInstance().notifyDownloadUpdate(mDownloadBean.getTaskId());
    }

    @Override
    public void run() {
        mDownloadBean.setLoadState(DownloadState.STATE_LOADING);
        DownloadManager.getInstance().notifyDownloadUpdate(mDownloadBean.getTaskId());
        initLengthAndStream();
        DownloadTaskTable downloadTaskTable = GreenDaoManager.getInstance().queryDownloadTask(mDownloadBean.getTaskId());
        if (downloadTaskTable != null) {
            if (downloadFile.exists()
                    && downloadFile.length() < downloadTaskTable.getTotalSize()
                    && mDownloadBean.getTotalSize() == downloadTaskTable.getTotalSize()) {
                Logger.e("文件存在，正在断点，可惜不支持断点");
//                mDownloadBean.setLoadedLength(downloadFile.length());
//                GreenDaoManager.getInstance().updateDownloadTaskTable(mDownloadBean);
                FileUtils.deleteFile(downloadFile);
                mDownloadBean.setLoadedLength(0);
                GreenDaoManager.getInstance().updateDownloadTaskTable(mDownloadBean);
            } else {
                Logger.e("文件存在，但是下载错误");
                FileUtils.deleteFile(downloadFile);
                mDownloadBean.setLoadedLength(0);
            }
        } else {
            if (downloadFile.exists()) {
                FileUtils.deleteFile(downloadFile);
            }
            mDownloadBean.setLoadedLength(0);
            GreenDaoManager.getInstance().insertDownloadTaskTable(mDownloadBean);
        }
        startTask();
    }

    private void startTask() {
        if (stream != null) {
            RandomAccessFile raf = null;
            BufferedInputStream buffer = new BufferedInputStream(stream);
            byte[] buf = new byte[BUFFER_SIZE];
            try {
                raf = new RandomAccessFile(downloadFile, "rwd");
                int len;
                long completed = mDownloadBean.getLoadedLength();
                // skipBytesFromStream(stream, completed); 无法断点
                raf.seek(completed);
                while (mDownloadBean.getLoadedLength() < mDownloadBean.getTotalSize() &&
                        mDownloadBean.getTotalSize() > 0 && (len = buffer.read(buf, 0, BUFFER_SIZE)) != -1
                        && mDownloadBean.getLoadState() == DownloadState.STATE_LOADING) {
                    raf.write(buf, 0, len);
                    completed += len;
                    mDownloadBean.setLoadedLength(completed);
                    updateDownLoadSpeed(mDownloadBean);
                    DownloadManager.getInstance().notifyDownloadUpdate(mDownloadBean.getTaskId());
                    GreenDaoManager.getInstance().updateDownloadTaskTable(mDownloadBean);
                }
            } catch (Exception e) {
                Logger.e(android.util.Log.getStackTraceString(e));
                if (e instanceof SocketTimeoutException) {// 连接超时
                    sendErrorState(-1, "网络连接超时");
                } else if (e instanceof SSLException) {
                    sendErrorState(-2, "请求错误,可能是网络断开连接");
                } else {
                    sendErrorState(-5, "未知错误" + e.getMessage());
                }
            } finally {
                try {
                    if (raf != null) {
                        raf.close();
                    }
                    buffer.close();
                    stream.close();
                } catch (IOException e) {
                    Logger.e(android.util.Log.getStackTraceString(e));
                    sendErrorState(-5, "未知错误");
                }
                if (downloadFile.length() == mDownloadBean.getTotalSize()
                        && mDownloadBean.getLoadedLength() == mDownloadBean.getTotalSize()
                        && mDownloadBean.getLoadState() == DownloadState.STATE_LOADING) {
                    mDownloadBean.setDownloadSpeed(0);
                    mDownloadBean.setLoadState(DownloadState.STATE_FINISH);
                    //更新进度
                    DownloadManager.getInstance().notifyDownloadUpdate(mDownloadBean.getTaskId());
                    Logger.d("download task is over: \r\n" + mDownloadBean.toString());
                }
            }
        } else {
            Logger.e("download inputStream is null: \r\n" + mDownloadBean.toString());
            sendErrorState(-3, "下载信息为空值,请检查下载信息");
        }
    }

    /**
     * 每隔一秒测量一次下载速度
     */
    private long oldTime = -1, oldLength = -1;

    private void updateDownLoadSpeed(DownloadBean mDownloadBean) {
        //至少一秒更新一次，速度更新不需要太频繁
        if ((System.currentTimeMillis() - oldTime) < 1000) {
            return;
        }
        if (oldTime > 0) {
            if ((mDownloadBean.getTotalSize() - mDownloadBean.getLoadedLength())
                    < mDownloadBean.getDownloadSpeed() / 2f) {
                mDownloadBean.setDownloadSpeed(0);
            } else {
                float speed = (mDownloadBean.getLoadedLength() - oldLength)
                        / ((System.currentTimeMillis() - oldTime) / 1000F);
                mDownloadBean.setDownloadSpeed(speed);
            }
        }
        oldTime = System.currentTimeMillis();
        oldLength = mDownloadBean.getLoadedLength();
    }

    /**
     * 初始化下载数据
     */
    private void initLengthAndStream() {
        if (mClient == null) {
            mClient = new OkHttpClient.Builder()
                    //连接超时为10秒
                    .connectTimeout(10, TimeUnit.MILLISECONDS)
                    //读取不设置超时
                    .readTimeout(0, TimeUnit.MILLISECONDS)
                    .build();
        }
        Request request = new Request.Builder()
                .get()
                .url(mDownloadBean.getDownloadUrl())
                .build();

        if (mDownloadBean.getHeadMap() != null) {
            request = request.newBuilder()
                    .addHeader(HttpHeadUtils.HEAD_CHANNEL, mDownloadBean.getHeadMap().get(HttpHeadUtils.HEAD_CHANNEL))
                    .addHeader(HttpHeadUtils.HEAD_PRS, mDownloadBean.getHeadMap().get(HttpHeadUtils.HEAD_PRS))
                    .addHeader(HttpHeadUtils.HEAD_RANDOM, mDownloadBean.getHeadMap().get(HttpHeadUtils.HEAD_RANDOM))
                    .addHeader(HttpHeadUtils.HEAD_SIGN, mDownloadBean.getHeadMap().get(HttpHeadUtils.HEAD_SIGN))
                    .addHeader(HttpHeadUtils.HEAD_SN, mDownloadBean.getHeadMap().get(HttpHeadUtils.HEAD_SN))
                    .addHeader(HttpHeadUtils.HEAD_TIME, mDownloadBean.getHeadMap().get(HttpHeadUtils.HEAD_TIME))
                    .build();
        }
        Call call = mClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                String contentLength = response.header("Content-Length");
                mDownloadBean.setTotalSize(Long.parseLong(contentLength));
                if (response.isSuccessful()) {
                    stream = response.body().byteStream();
                }
            }
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                sendErrorState(-1, "网络连接超时");
            } else if (e instanceof SSLException) {
                sendErrorState(-2, "请求错误,可能是网络断开连接");
            } else {
                sendErrorState(-5, "未知错误" + e.getMessage());
            }
        }
    }

    /**
     * 由于服务器现在不支持Range请求头，所以无法做到网络断点，只能本地操作
     * <p>
     * 跳过数据流中起始的n个字节
     * <p>
     * 看似是断点，其实没达到省流量的效果，欺骗用户而已
     */
    private long skipBytesFromStream(InputStream inputStream, long n) {
        long remaining = n;
        byte[] skipBuffer = new byte[BUFFER_SIZE];
        int nr = 0;
        if (n <= 0) {
            return 0;
        }
        while (remaining > 0) {
            try {
                nr = inputStream.read(skipBuffer, 0, (int) Math.min(BUFFER_SIZE, remaining));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nr < 0) {
                break;
            }
            remaining -= nr;
        }
        return n - remaining;
    }

    /**
     * 返回错误提示
     *
     * @param errorCode
     *         错误码
     *         <p>
     *         -1 网络连接超时
     *         <p>
     *         -2 请求错误,可能是网络断开连接
     *         <p>
     *         -3 下载信息为空值,请检查下载信息
     *         <p>
     *         -4 原下载文件删除异常,可手动删除
     *         <p>
     *         -5 未知错误
     * @param errorMessage
     *         错误信息
     */
    private void sendErrorState(int errorCode, String errorMessage) {
        DownloadManager.getInstance().setError(errorCode, errorMessage);
        mDownloadBean.setLoadState(DownloadState.STATE_ERROR);
        DownloadManager.getInstance().notifyDownloadUpdate(mDownloadBean.getTaskId());
    }
}
