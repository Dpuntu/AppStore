package com.seuic.app.store.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.glide.GlideAppManager;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.view.MultifunctionalTextView;

import java.util.List;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class RecommendAdapter extends BaseTitleRecycleViewAdapter<RecommendAdapter.RecommendDataViewHolder, RecommendReceive> {

    public RecommendAdapter(List<RecycleObject> mRecycleObjectList) {
        super(mRecycleObjectList, R.layout.fragment_recommend_item);
    }

    @Override
    protected RecommendDataViewHolder createDataViewHolder(View view) {
        return new RecommendAdapter.RecommendDataViewHolder(view);
    }

    @Override
    protected void loadRecycleData(final RecommendDataViewHolder holder, final RecommendReceive recommendReceive) {
        DownloadManager.getInstance().add2OkhttpDownloaderMap(recommendReceive);
        holder.appNameText.setText(recommendReceive.getAppName());
        holder.appVersionText.setText(
                AppStoreApplication.getApp().getString(R.string.app_size_version,
                                                       recommendReceive.getAppSize(), recommendReceive.getAppVersion()));
        holder.appDescText.setText(recommendReceive.getAppDesc());
        holder.installText.setTextState(MultifunctionalTextView.TextViewState.NORMAL);
        holder.installText.bindDownloadTask(recommendReceive.getAppVersionId());
        GlideAppManager.loadImage(recommendReceive.getAppIconName(),
                                  holder.appImage,
                                  R.mipmap.default_icon,
                                  AppStoreUtils.AppStoreImageType.ICON);
        holder.installText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextOnClickListener != null && holder.installText.getTaskId().equals(recommendReceive.getAppVersionId())) {
                    mTextOnClickListener.onTextClick(holder.installText, recommendReceive, holder.installText.getTextState());
                }
            }
        });
    }

    private TextOnClickListener mTextOnClickListener;

    public void setTextOnClickListener(TextOnClickListener mTextOnClickListener) {
        this.mTextOnClickListener = mTextOnClickListener;
    }

    public interface TextOnClickListener {
        void onTextClick(MultifunctionalTextView view, RecommendReceive recommendReceive, int textState);
    }

    class RecommendDataViewHolder extends BaseTitleRecycleViewAdapter.DataViewHolder {
        TextView appNameText, appVersionText, appDescText;
        MultifunctionalTextView installText;
        ImageView appImage;

        RecommendDataViewHolder(View itemView) {
            super(itemView);
            appNameText = (TextView) itemView.findViewById(R.id.recommend_app_item_name);
            appVersionText = (TextView) itemView.findViewById(R.id.recommend_app_item_version);
            appDescText = (TextView) itemView.findViewById(R.id.recommend_app_item_desc);
            appImage = (ImageView) itemView.findViewById(R.id.recommend_app_item_image);
            installText = (MultifunctionalTextView) itemView.findViewById(R.id.recommend_app_item_install);
        }
    }
}
