package com.seuic.app.store.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

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
import com.seuic.app.store.utils.MuTextViewClickUtils;
import com.seuic.app.store.view.BannerView;
import com.seuic.app.store.view.MultifunctionalTextView;
import com.seuic.app.store.view.recycleview.RecyclerViewUtils;

import java.util.List;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */

public class RecommendFragment extends BaseFragment<RecommendAdapter, RecommendPresenter> implements RecommendContent.View {
    private boolean isRefresh = false;
    private View mRecycleHead;

    @Override
    protected void initFragment(View view) {
        mPresenter = new RecommendPresenter(this);
        mPresenter.isRefresh(false);
        mPresenter.getAdvertisements();
        mPresenter.getRecommendApps();
    }

    @Override
    protected void isRefreshing() {
        mPresenter.isRefresh(true);
        mPresenter.getAdvertisements();
    }

    @Override
    public void updateBannerView(String count, boolean isHidden, long intervalTime, List<AdvertisementsReceive.AdReceiveDetails> mDetailses) {
        mRecycleHead = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recommend_head, null, false);
        BannerView mBannerView = (BannerView) mRecycleHead.findViewById(R.id.recommend_banner);
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
        mPresenter.getRecommendApps();
    }

    @Override
    public void updateRecycleView(List<RecycleObject> recycleObjectList) {
        RecyclerViewUtils.createVerticalRecyclerView(getActivity(), mRecyclerView, !isRefresh, true);
        mAdapter = new RecommendAdapter(recycleObjectList);
        mAdapter.addHeadView(mRecycleHead);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setTextOnClickListener(new RecommendAdapter.TextOnClickListener() {
            @Override
            public void onTextClick(final MultifunctionalTextView textView, RecommendReceive recommendReceive, int textState) {
                MuTextViewClickUtils.clickChangeState(recommendReceive, textState);
            }
        });
        mAdapter.setOnItemClickListener(new BaseTitleRecycleViewAdapter.OnItemClickListener<RecommendReceive>() {
            @Override
            public void onItemClick(View view, RecommendReceive recommendReceive) {
                Intent intent = new Intent();
                intent.putExtra(AppDetailActivity.APP_DETAIL, recommendReceive);
                intent.setClass(getActivity(), AppDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRefreshRecycle(List<RecycleObject> recycleObjectList) {
        if (mAdapter != null) {
            mAdapter.setTextOnClickListener(null);
            mAdapter.setOnItemClickListener(null);
            mAdapter = null;
            mRecyclerView.setAdapter(null);
            isRefresh = true;
        }
        updateRecycleView(recycleObjectList);
    }
}
