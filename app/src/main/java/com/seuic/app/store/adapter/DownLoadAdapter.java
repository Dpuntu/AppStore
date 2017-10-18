package com.seuic.app.store.adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.bean.DownloadingBean;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.glide.GlideAppManager;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.greendao.RecommendReceiveTable;
import com.seuic.app.store.net.download.DownloadBean;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.net.download.DownloadObserver;
import com.seuic.app.store.net.download.DownloadState;
import com.seuic.app.store.utils.AndroidUtils;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.view.MultifunctionalTextView;

import java.util.List;

/**
 * Created on 2017/9/24.
 *
 * @author dpuntu
 */

public class DownLoadAdapter extends BaseRecycleViewAdapter<DownLoadAdapter.DownLoadDataViewHolder, DownloadingBean> {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public DownLoadAdapter(List<RecycleObject> mRecycleObjectList) {
        super(mRecycleObjectList, R.layout.activity_download_recycle_item);
    }

    @Override
    protected DownLoadDataViewHolder createDataViewHolder(View view) {
        return new DownLoadDataViewHolder(view);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadRecycleData(final DownLoadDataViewHolder downLoadDataViewHolder, final DownloadingBean downloadingBean) {
        GlideAppManager.loadImage(downloadingBean.getAppIconName(),
                                  downLoadDataViewHolder.mAppIcon,
                                  R.mipmap.default_icon,
                                  AppStoreUtils.AppStoreImageType.ICON);
        downLoadDataViewHolder.mTitleText.setText(downloadingBean.getAppName());
        downLoadDataViewHolder.mProgressBar.setMax(100);
        downLoadDataViewHolder.mProgressText.setText(AndroidUtils.formatSpeed(0F));
        downLoadDataViewHolder.mSizeText.setText(AndroidUtils.formatDataSize(downloadingBean.getLoadedLength())
                                                         + "/" + AndroidUtils.formatDataSize(downloadingBean.getTotalSize()));
        downLoadDataViewHolder.mProgressBar.setProgress(setProgressBarNum(downloadingBean.getLoadedLength(), downloadingBean.getTotalSize()));
        downLoadDataViewHolder.mDeleteText.setTextState(MultifunctionalTextView.TextViewState.CANCEL);
        if (downloadingBean.getLoadedLength() == downloadingBean.getTotalSize()) {
            downLoadDataViewHolder.mPauseText.setTextState(MultifunctionalTextView.TextViewState.INSTALL_FINISH);
        } else {
            downLoadDataViewHolder.mPauseText.setTextState(MultifunctionalTextView.TextViewState.LOADING_PAUSE);
        }

        downLoadDataViewHolder.mPauseText.bindDownloadTask(downloadingBean.getTaskId());
        DownloadManager.getInstance().registerObserver(downloadingBean.getTaskId(), new DownloadObserver() {
            @Override
            public void update(final DownloadBean bean) {
                if (bean.getLoadState() == DownloadState.STATE_LOADING) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            downLoadDataViewHolder.mProgressText.setText(AndroidUtils.formatSpeed(bean.getDownloadSpeed()));
                            downLoadDataViewHolder.mProgressBar.setProgress(downLoadDataViewHolder.mPauseText.getProgress());
                            downLoadDataViewHolder.mSizeText.setText(AndroidUtils.formatDataSize(bean.getLoadedLength())
                                                                             + "/" + AndroidUtils.formatDataSize(bean.getTotalSize()));
                        }
                    });
                }
            }
        });
        downLoadDataViewHolder.mDeleteText.setTextOnClickListener(new MultifunctionalTextView.TextOnClickListener() {
            @Override
            public void onTextClick(View view, int typeState) {
                if (mOnDeleteTextClickListener != null && typeState == MultifunctionalTextView.TextViewState.CANCEL) {
                    mOnDeleteTextClickListener.onDeleteTextClick(downLoadDataViewHolder.mDeleteText, downloadingBean.getTaskId());
                }
            }
        });

        downLoadDataViewHolder.mPauseText.setTextOnClickListener(new MultifunctionalTextView.TextOnClickListener() {
            @Override
            public void onTextClick(View view, int typeState) {
                if (mOnPauseTextClickListener != null) {
                    RecommendReceiveTable recommendReceiveTable = GreenDaoManager.getInstance().queryRecommendReceive(downloadingBean.getTaskId());
                    RecommendReceive recommendReceive;
                    recommendReceive = GreenDaoManager.getInstance().table2RecommendReceive(recommendReceiveTable);
                    mOnPauseTextClickListener.onPauseTextClick(downLoadDataViewHolder.mPauseText,
                                                               recommendReceive,
                                                               typeState);
                }
            }
        });
    }

    class DownLoadDataViewHolder extends BaseRecycleViewAdapter.DataViewHolder {
        ImageView mAppIcon;
        MultifunctionalTextView mDeleteText, mPauseText;
        TextView mTitleText, mProgressText, mSizeText;
        ProgressBar mProgressBar;

        DownLoadDataViewHolder(View itemView) {
            super(itemView);
            mAppIcon = (ImageView) itemView.findViewById(R.id.download_recycle_image);
            mDeleteText = (MultifunctionalTextView) itemView.findViewById(R.id.download_recycle_delete);
            mPauseText = (MultifunctionalTextView) itemView.findViewById(R.id.download_recycle_pause);
            mTitleText = (TextView) itemView.findViewById(R.id.download_recycle_title);
            mProgressText = (TextView) itemView.findViewById(R.id.download_recycle_progress_num);
            mSizeText = (TextView) itemView.findViewById(R.id.download_recycle_size);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.download_recycle_progress);
        }
    }

    private int setProgressBarNum(long loadLength, long totalSize) {
        if (totalSize <= 0) {
            return 0;
        }
        return (int) ((loadLength * 1f / totalSize * 1f) * 100);
    }

    private OnDeleteTextClickListener mOnDeleteTextClickListener;
    private OnPauseTextClickListener mOnPauseTextClickListener;


    public void setOnDeleteTextClickListener(OnDeleteTextClickListener mOnDeleteTextClickListener) {
        this.mOnDeleteTextClickListener = mOnDeleteTextClickListener;
    }

    public void setOnPauseTextClickListener(OnPauseTextClickListener mOnPauseTextClickListener) {
        this.mOnPauseTextClickListener = mOnPauseTextClickListener;
    }

    public interface OnDeleteTextClickListener {
        void onDeleteTextClick(MultifunctionalTextView view, String taskId);
    }

    public interface OnPauseTextClickListener {
        void onPauseTextClick(MultifunctionalTextView view, RecommendReceive recommendReceive, int typeState);
    }
}
