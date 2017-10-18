package com.seuic.app.store.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.glide.GlideAppManager;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.view.recycleview.CustomGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class AssortmentAdapter extends BaseRecycleViewAdapter<AssortmentAdapter.AssortmentDataViewHolder, List<RecycleObject<RecommendReceive>>> {
    public AssortmentAdapter(List<RecycleObject> mRecycleObjectList) {
        super(mRecycleObjectList, R.layout.fragment_assortment_item);
    }

    @Override
    protected AssortmentDataViewHolder createDataViewHolder(View view) {
        return new AssortmentAdapter.AssortmentDataViewHolder(view);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadRecycleData(AssortmentDataViewHolder holder, List<RecycleObject<RecommendReceive>> recycleObjects) {
        final List<RecommendReceive> recommendReceives = new ArrayList<>();
        for (RecycleObject recycleObject : recycleObjects) {
            if (recycleObject.getType() != RecycleViewType.RECYCLE_APP_TYPE) {
                continue;
            }
            recommendReceives.add((RecommendReceive) recycleObject.getObject());
            CustomGridLayoutManager mGridLayoutManager = new CustomGridLayoutManager(AppStoreApplication.getApp(), 4);
            mGridLayoutManager.setScrollEnabled(false);
            holder.mRecyclerView.setLayoutManager(mGridLayoutManager);
            AssortmentDataItemRecycleViewAdapter assortmentDataItemRecycleViewAdapter = new AssortmentDataItemRecycleViewAdapter(recommendReceives);
            holder.mRecyclerView.setAdapter(assortmentDataItemRecycleViewAdapter);
            assortmentDataItemRecycleViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    mOnAssortmentItemClickListener.onItemClick(view, recommendReceives.get(position));
                }
            });
        }
    }

    class AssortmentDataViewHolder extends BaseRecycleViewAdapter.DataViewHolder {
        RecyclerView mRecyclerView;

        AssortmentDataViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.assortment_item_recycle);
        }
    }

    private class AssortmentDataItemRecycleViewAdapter extends BaseQuickAdapter<RecommendReceive, BaseViewHolder> {
        AssortmentDataItemRecycleViewAdapter(@Nullable List<RecommendReceive> data) {
            super(R.layout.fragment_assortment_item_recycle_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final RecommendReceive recommendReceive) {
            DownloadManager.getInstance().add2OkhttpDownloaderMap(recommendReceive); //添加
            GlideAppManager.loadImage(recommendReceive.getAppIconName(),
                                      (ImageView) helper.getView(R.id.assortment_item_image),
                                      R.mipmap.default_icon,
                                      AppStoreUtils.AppStoreImageType.ICON);
            helper.setText(R.id.assortment_item_name, recommendReceive.getAppName());
        }
    }

    private OnAssortmentItemClickListener mOnAssortmentItemClickListener;

    public void setOnAssortmentItemClickListener(OnAssortmentItemClickListener mOnAssortmentItemClickListener) {
        this.mOnAssortmentItemClickListener = mOnAssortmentItemClickListener;
    }

    public interface OnAssortmentItemClickListener {
        void onItemClick(View view, RecommendReceive recommendReceive);
    }
}
