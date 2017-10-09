package com.seuic.app.store.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.adapter.AssortmentAdapter;
import com.seuic.app.store.adapter.BaseTitleRecycleViewAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.ui.activity.AppDetailActivity;
import com.seuic.app.store.ui.activity.AppTypeActivity;
import com.seuic.app.store.ui.contact.AssortmentContent;
import com.seuic.app.store.ui.presenter.AssortmentPresenter;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.view.recycleview.RecyclerViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 */

public class AssortmentFragment extends Fragment implements AssortmentContent.View {
    public static final String APPS_TYPE = "apps_type";
    public static final String APPS_TYPE_ID = "apps_type_id";
    private Unbinder mUnbinder;
    private AssortmentPresenter mAssortmentPresenter;
    @BindView(R.id.assortment_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.assortment_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.assortment_refresh_text)
    TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assortment, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mAssortmentPresenter = new AssortmentPresenter(this);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        mAssortmentPresenter.getAppTypes(false);
        mRefreshLayout.setColorSchemeResources(AppStoreUtils.refreshColors);
        mRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private AssortmentAdapter assortmentAdapter;

    @Override
    public void updateRecycleView(List<RecycleObject> recycleObjectList) {
        RecyclerViewUtils.createVerticalRecyclerView(getActivity(), mRecyclerView, true, false);
        assortmentAdapter = new AssortmentAdapter(recycleObjectList);
        mRecyclerView.setAdapter(assortmentAdapter);
        assortmentAdapter.setOnAssortmentItemClickListener(new AssortmentAdapter.OnAssortmentItemClickListener() {
            @Override
            public void onItemClick(View view, RecommendReceive recommendReceive) {
                // 应用详情
                Intent intent = new Intent();
                intent.putExtra(AppDetailActivity.APP_DETAIL, recommendReceive);
                intent.setClass(getActivity(), AppDetailActivity.class);
                startActivity(intent);
            }
        });
        assortmentAdapter.setOnMoreClickListener(new BaseTitleRecycleViewAdapter.OnMoreClickListener() {
            @Override
            public void OnMoreClick(View view, String assortmentId, String typeTitle) {
                // 更多
                Intent intent = new Intent();
                intent.setClass(getActivity(), AppTypeActivity.class);
                intent.putExtra(APPS_TYPE_ID, assortmentId);
                intent.putExtra(APPS_TYPE, typeTitle);
                startActivity(intent);
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            mAssortmentPresenter.getAppTypes(true);
            System.gc();
        }
    };

    @Override
    public void refreshRecycleView(List<RecycleObject> recycleObjectList) {
        if (assortmentAdapter != null) {
            assortmentAdapter.refreshAdapter(recycleObjectList);
        } else {
            updateRecycleView(recycleObjectList);
        }
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadError(String errorMsg) {
        if (assortmentAdapter != null) {
            assortmentAdapter = null;
            mRecyclerView.setAdapter(null);
        }
        showRecycle(false);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecycle(true);
                mAssortmentPresenter.getAppTypes(true);
            }
        });
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    private void showRecycle(boolean recycleShow) {
        mTextView.setVisibility(recycleShow ? View.GONE : View.VISIBLE);
        mRefreshLayout.setVisibility(recycleShow ? View.VISIBLE : View.GONE);
    }
}
