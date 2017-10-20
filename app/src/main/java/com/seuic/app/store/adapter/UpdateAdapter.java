package com.seuic.app.store.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.glide.GlideAppManager;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.utils.MuTextViewClickUtils;
import com.seuic.app.store.view.MultifunctionalTextView;

import java.util.List;

/**
 * Created on 2017/9/26.
 *
 * @author dpuntu
 */

public class UpdateAdapter extends BaseRecycleViewAdapter<UpdateAdapter.UpdateViewHolder, RecommendReceive> {

    public UpdateAdapter(List<RecycleObject> mRecycleObjectList) {
        super(mRecycleObjectList, UpdateAdapter.UpdateViewHolder.class, R.layout.activity_update_recycle_item);
    }

    @Override
    protected void loadRecycleData(final UpdateViewHolder holder, final RecommendReceive recommendReceive) {
        DownloadManager.getInstance().add2OkhttpDownloaderMap(recommendReceive); //添加
        GlideAppManager.loadImage(recommendReceive.getAppIconName(),
                                  holder.imageView,
                                  R.mipmap.default_icon,
                                  AppStoreUtils.AppStoreImageType.ICON);
        holder.updateText.setTextState(MultifunctionalTextView.TextViewState.UPDATE);
        holder.updateText.bindDownloadTask(recommendReceive.getAppVersionId());
        holder.updateText.setTextOnClickListener(new MultifunctionalTextView.TextOnClickListener() {
            @Override
            public void onTextClick(View view, int typeState) {
                MuTextViewClickUtils.clickChangeState(recommendReceive, typeState);
            }
        });
        holder.appVersion.setText(AppStoreApplication.getApp().getString(R.string.app_version_size,
                                                                         recommendReceive.getAppVersion(),
                                                                         recommendReceive.getAppSize()));
        holder.appTitle.setText(recommendReceive.getAppName());
        holder.hiddenDesc.setText(recommendReceive.getAppVersionDesc());
        holder.appDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.hiddenDesc.getVisibility() == View.GONE) {
                    holder.hiddenDesc.setVisibility(View.VISIBLE);
                } else if (holder.hiddenDesc.getVisibility() == View.VISIBLE) {
                    holder.hiddenDesc.setVisibility(View.GONE);
                }
            }
        });
    }

    public static class UpdateViewHolder extends BaseRecycleViewAdapter.DataViewHolder {
        ImageView imageView;
        MultifunctionalTextView updateText;
        TextView appVersion, appTitle, hiddenDesc;
        RelativeLayout appDesc;

        public UpdateViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.update_app_image);
            updateText = (MultifunctionalTextView) itemView.findViewById(R.id.update_app_text);
            appVersion = (TextView) itemView.findViewById(R.id.update_app_version);
            appTitle = (TextView) itemView.findViewById(R.id.update_app_title);
            hiddenDesc = (TextView) itemView.findViewById(R.id.update_app_hidden_desc);
            appDesc = (RelativeLayout) itemView.findViewById(R.id.update_app_desc);
        }
    }
}
