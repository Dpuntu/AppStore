package com.seuic.app.store.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.ScreenShotBean;
import com.seuic.app.store.bean.response.AppDetailReceive;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.glide.GlideAppManager;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.ui.dialog.DialogManager;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.utils.MuTextViewClickUtils;
import com.seuic.app.store.view.MultifunctionalTextView;
import com.seuic.app.store.view.recycleview.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/26.
 *
 * @author dpuntu
 */

public class SearchAdapter extends BaseRecycleViewAdapter<SearchAdapter.SearchViewHolder, AppDetailReceive> {
    public SearchAdapter(List<RecycleObject> mRecycleObjectList) {
        super(mRecycleObjectList, R.layout.activity_search_apps_item);
    }

    @Override
    protected SearchViewHolder createDataViewHolder(View view) {
        return new SearchViewHolder(view);
    }

    @Override
    protected void loadRecycleData(final SearchViewHolder holder, final AppDetailReceive appDetailReceive) {
        final RecommendReceive recommendReceive = new RecommendReceive(appDetailReceive.getAppName(),
                                                                       appDetailReceive.getPackageName(),
                                                                       appDetailReceive.getAppSize(),
                                                                       appDetailReceive.getAppVersion(),
                                                                       appDetailReceive.getAppVersionId(),
                                                                       appDetailReceive.getAppVersionIdDesc(),
                                                                       appDetailReceive.getAppDesc(),
                                                                       appDetailReceive.getMD5(),
                                                                       appDetailReceive.getDownloadName(),
                                                                       appDetailReceive.getAppIconName());
        DownloadManager.getInstance().add2OkhttpDownloaderMap(recommendReceive); //添加
        GlideAppManager.loadImage(appDetailReceive.getAppIconName(),
                                  holder.mImageView,
                                  R.mipmap.default_icon,
                                  AppStoreUtils.AppStoreImageType.ICON);
        loadScreenShot(appDetailReceive, holder);
        holder.mTextBtn.setTextState(MultifunctionalTextView.TextViewState.NORMAL);
        holder.mTextBtn.bindDownloadTask(appDetailReceive.getAppVersionId());
        holder.appName.setText(appDetailReceive.getAppName());
        holder.appType.setText(appDetailReceive.getAppTypes());
        holder.mTextBtn.setTextOnClickListener(new MultifunctionalTextView.TextOnClickListener() {
            @Override
            public void onTextClick(View view, int typeState) {
                MuTextViewClickUtils.clickChangeState(recommendReceive, typeState);
            }
        });
    }

    public class SearchViewHolder extends BaseRecycleViewAdapter.DataViewHolder {
        RecyclerView mGalleryRecycle;
        ImageView mImageView;
        MultifunctionalTextView mTextBtn;
        TextView appName, appType;

        SearchViewHolder(View itemView) {
            super(itemView);
            mGalleryRecycle = (RecyclerView) itemView.findViewById(R.id.search_screen_gallery);
            mImageView = (ImageView) itemView.findViewById(R.id.app_detail_image);
            mTextBtn = (MultifunctionalTextView) itemView.findViewById(R.id.app_detail_install);
            appName = (TextView) itemView.findViewById(R.id.app_detail_name);
            appType = (TextView) itemView.findViewById(R.id.app_detail_type);
        }
    }


    private void loadScreenShot(AppDetailReceive appDetailReceive, SearchViewHolder holder) {
        List<RecycleObject> screenShots = new ArrayList<>();

        checkScreenShotName(screenShots, appDetailReceive.getAppShot1());
        checkScreenShotName(screenShots, appDetailReceive.getAppShot2());
        checkScreenShotName(screenShots, appDetailReceive.getAppShot3());
        checkScreenShotName(screenShots, appDetailReceive.getAppShot4());
        checkScreenShotName(screenShots, appDetailReceive.getAppShot5());

        CustomLinearLayoutManager layoutManager =
                new CustomLinearLayoutManager(
                        AppStoreApplication.getApp(),
                        CustomLinearLayoutManager.HORIZONTAL,
                        false);
        ScreenShotAdapter screenShotAdapter = new ScreenShotAdapter(screenShots);
        holder.mGalleryRecycle.setLayoutManager(layoutManager);
        holder.mGalleryRecycle.setAdapter(screenShotAdapter);
        screenShotAdapter.setOnItemClickAllListener(new OnItemClickAllListener() {
            @Override
            public void OnItemClickAll(View view, int position, List<RecycleObject> mRecycleObjectList) {
                DialogManager.getInstance().showImageDetailDialog(position, mRecycleObjectList);
            }
        });
    }

    private void checkScreenShotName(List<RecycleObject> screenShots, String appShot) {
        if (appShot != null && !appShot.isEmpty()) {
            screenShots.add(new RecycleObject<>(RecycleViewType.RECYCLE_DATA, new ScreenShotBean(appShot)));
        }
    }
}
