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
import com.seuic.app.store.adapter.BaseTitleRecycleViewAdapter;
import com.seuic.app.store.adapter.RecommendAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.AdvertisementsReceive;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.ui.activity.AppDetailActivity;
import com.seuic.app.store.ui.activity.WebViewActivity;
import com.seuic.app.store.ui.contact.RecommendContent;
import com.seuic.app.store.ui.presenter.RecommendPresenter;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.utils.MuTextViewClickUtils;
import com.seuic.app.store.view.BannerView;
import com.seuic.app.store.view.MultifunctionalTextView;
import com.seuic.app.store.view.recycleview.RecyclerViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */

public class RecommendFragment extends Fragment implements RecommendContent.View {
    private Unbinder mUnbinder;
    private RecommendPresenter mRecommendPresenter;
    private boolean isRefresh = false;
    @BindView(R.id.recommend_banner)
    BannerView mBannerView;
    @BindView(R.id.recommend_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.recommend_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recommend_refresh_text)
    TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        mRecommendPresenter = new RecommendPresenter(this);
        mUnbinder = ButterKnife.bind(this, rootView);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        mRefreshLayout.setColorSchemeResources(AppStoreUtils.refreshColors);
        mRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRecommendPresenter.isRefresh(false);
        mRecommendPresenter.getAdvertisements();
        mRecommendPresenter.getRecommendApps();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void updateBannerView(String count, boolean isHidden, long intervalTime, List<AdvertisementsReceive.AdReceiveDetails> mDetailses) {
        mBannerView.setVisibility(isHidden ? View.GONE : View.VISIBLE);
        mBannerView.setViewUrlList(mDetailses, Integer.parseInt(count), 0);
        mBannerView.setLoopTime(intervalTime);
        mBannerView.isAutoLoop(true);
        mBannerView.setOnClickListener(new BannerView.OnClickListener() {
            @Override
            public void onClick(View view, String url, boolean isAddHeader, int position) {
                if (url != null && !url.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra(WebViewActivity.WEB_URL, url);
                    intent.putExtra(WebViewActivity.WEB_HEAD, isAddHeader);
                    intent.setClass(getActivity(), WebViewActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void refreshRecycleView(List<RecycleObject> recycleObjectList) {
        // 推荐界面刷新后，先清除历史，否则会下载错位
        if (recommendAdapter != null) {
            recommendAdapter.setTextOnClickListener(null);
            recommendAdapter.setOnItemClickListener(null);
            recommendAdapter = null;
            mRecyclerView.setAdapter(null);
            isRefresh = true;
            System.gc();
        }
        updateRecycleView(recycleObjectList);
    }

    private RecommendAdapter recommendAdapter;

    @Override
    public void updateRecycleView(List<RecycleObject> recycleObjectList) {
        recommendAdapter = new RecommendAdapter(recycleObjectList);
        RecyclerViewUtils.createVerticalRecyclerView(getActivity(), mRecyclerView, !isRefresh, true);
        mRecyclerView.setAdapter(recommendAdapter);
        recommendAdapter.setTextOnClickListener(new RecommendAdapter.TextOnClickListener() {
            @Override
            public void onTextClick(final MultifunctionalTextView textView, RecommendReceive recommendReceive, int textState) {
                MuTextViewClickUtils.clickChangeState(recommendReceive, textState);
            }
        });
        recommendAdapter.setOnItemClickListener(new BaseTitleRecycleViewAdapter.OnItemClickListener<RecommendReceive>() {
            @Override
            public void onItemClick(View view, RecommendReceive recommendReceive) {
                Intent intent = new Intent();
                intent.putExtra(AppDetailActivity.APP_DETAIL, recommendReceive);
                intent.setClass(getActivity(), AppDetailActivity.class);
                startActivity(intent);
            }
        });

        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }


    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            refreshView();
            System.gc();
        }
    };


    @Override
    public void onLoadError(String errorMsg) {
        if (recommendAdapter != null) {
            recommendAdapter = null;
            mRecyclerView.setAdapter(null);
        }
        showRecycle(false);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecycle(true);
                refreshView();
            }
        });
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    private void refreshView() {
        mRecommendPresenter.isRefresh(true);
        mRecommendPresenter.getAdvertisements();
        mRecommendPresenter.getRecommendApps();
    }

    private void showRecycle(boolean recycleShow) {
        mTextView.setVisibility(recycleShow ? View.GONE : View.VISIBLE);
        mRefreshLayout.setVisibility(recycleShow ? View.VISIBLE : View.GONE);
    }
}
