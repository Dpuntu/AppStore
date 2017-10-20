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

public class TypeAppsAdapter extends BaseRecycleViewAdapter<TypeAppsAdapter.InstallDataViewHolder, RecommendReceive> {

    public TypeAppsAdapter(List<RecycleObject> mRecycleObjectList) {
        super(mRecycleObjectList, TypeAppsAdapter.InstallDataViewHolder.class, R.layout.activity_install_item);
    }

    @Override
    protected void loadRecycleData(final InstallDataViewHolder holder, final RecommendReceive recommendReceive) {
        DownloadManager.getInstance().add2OkhttpDownloaderMap(recommendReceive); //添加
        GlideAppManager.loadImage(recommendReceive.getAppIconName(),
                                  holder.appImage,
                                  R.mipmap.default_icon,
                                  AppStoreUtils.AppStoreImageType.ICON);
        holder.titleText.setText(recommendReceive.getAppName());
        holder.summaryText.setText(AppStoreApplication.getApp().getString(R.string.app_size_version,
                                                                          recommendReceive.getAppSize(),
                                                                          recommendReceive.getAppVersion()));
        holder.unInstall.setTextState(MultifunctionalTextView.TextViewState.NORMAL);
        holder.unInstall.bindDownloadTask(recommendReceive.getAppVersionId());
        holder.unInstall.setTextOnClickListener(new MultifunctionalTextView.TextOnClickListener() {
            @Override
            public void onTextClick(View view, int typeState) {
                mTextOnClickListener.onTextClick(holder.unInstall, recommendReceive, holder.unInstall.getTextState());
            }
        });
    }

    public static class InstallDataViewHolder extends BaseRecycleViewAdapter.DataViewHolder {
        TextView titleText, summaryText;
        MultifunctionalTextView unInstall;
        ImageView appImage;

        public InstallDataViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.install_app_item_title);
            summaryText = (TextView) itemView.findViewById(R.id.install_app_item_summary);
            appImage = (ImageView) itemView.findViewById(R.id.install_app_item_image);
            unInstall = (MultifunctionalTextView) itemView.findViewById(R.id.install_app_item_uninstall);
        }
    }

    private TextOnClickListener mTextOnClickListener;

    public void setTextOnClickListener(TextOnClickListener mTextOnClickListener) {
        this.mTextOnClickListener = mTextOnClickListener;
    }

    public interface TextOnClickListener {
        void onTextClick(MultifunctionalTextView view, RecommendReceive recommendReceive, int textState);
    }


}
