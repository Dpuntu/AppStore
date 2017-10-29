package com.seuic.app.store.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.seuic.app.store.adapter.AssortmentAdapter;
import com.seuic.app.store.adapter.BaseRecycleViewAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.ui.activity.AppDetailActivity;
import com.seuic.app.store.ui.activity.AppTypeActivity;
import com.seuic.app.store.ui.contact.AssortmentContent;
import com.seuic.app.store.ui.presenter.AssortmentPresenter;
import com.seuic.app.store.view.recycleview.RecyclerViewUtils;

import java.util.List;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 */

public class AssortmentFragment extends BaseFragment<AssortmentAdapter, AssortmentPresenter> implements AssortmentContent.View {
    public static final String APPS_TYPE = "apps_type";
    public static final String APPS_TYPE_ID = "apps_type_id";

    @Override
    protected void initFragment(View view) {
        mPresenter = new AssortmentPresenter(this);
        mPresenter.getAppTypes(false);
    }

    @Override
    protected void isRefreshing() {
        mPresenter.getAppTypes(true);
    }

    @Override
    public void updateRecycleView(List<RecycleObject> recycleObjectList) {
        RecyclerViewUtils.createVerticalRecyclerView(getActivity(), mRecyclerView, true, false);
        mAdapter = new AssortmentAdapter(recycleObjectList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnAssortmentItemClickListener(new AssortmentAdapter.OnAssortmentItemClickListener() {
            @Override
            public void onItemClick(View view, RecommendReceive recommendReceive) {
                // 应用详情
                Intent intent = new Intent();
                intent.putExtra(AppDetailActivity.APP_DETAIL, recommendReceive);
                intent.setClass(getActivity(), AppDetailActivity.class);
                startActivity(intent);
            }
        });
        mAdapter.setOnMoreClickListener(new BaseRecycleViewAdapter.OnMoreClickListener() {
            @Override
            public void onMoreClick(View view, String assortmentId, String typeTitle) {
                // 更多
                Intent intent = new Intent();
                intent.setClass(getActivity(), AppTypeActivity.class);
                intent.putExtra(APPS_TYPE_ID, assortmentId);
                intent.putExtra(APPS_TYPE, typeTitle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRefreshRecycle(List<RecycleObject> recycleObjectList) {
        if (mAdapter != null) {
            mAdapter.refreshAdapter(recycleObjectList);
        } else {
            updateRecycleView(recycleObjectList);
        }
    }
}
