package com.seuic.app.store.ui.fragment;

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
import com.seuic.app.store.adapter.BaseRecycleViewAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.ui.contact.FragmentBaseContent;
import com.seuic.app.store.utils.AppStoreUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 2017/10/10.
 *
 * @author dpuntu
 */

public abstract class BaseFragment<T extends BaseRecycleViewAdapter, V extends FragmentBaseContent.Presenter> extends Fragment implements FragmentBaseContent.View {
    private Unbinder mUnbinder;
    protected V mPresenter;
    protected T mAdapter;
    @BindView(R.id.fragment_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.fragment_error_text)
    TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_layout, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mRefreshLayout.setColorSchemeResources(AppStoreUtils.refreshColors);
        mRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        initFragment(rootView);
        return rootView;
    }

    protected abstract void initFragment(View view);

    protected abstract void onRefreshRecycle(final List<RecycleObject> recycleObjectList);

    protected abstract void isRefreshing();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener
            = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refresh();
        }
    };


    private void refresh() {
        mRefreshLayout.setRefreshing(true);
        isRefreshing();
    }

    @Override
    public void removeRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    protected void showRecycle(boolean recycleShow) {
        mTextView.setVisibility(recycleShow ? View.GONE : View.VISIBLE);
        mRefreshLayout.setVisibility(recycleShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoadError(String errorMsg) {
        removeRefresh();
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAdapter != null) {
                    mAdapter = null;
                    mRecyclerView.setAdapter(null);
                }
                showRecycle(false);
                mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRecycle(true);
                        refresh();
                    }
                });
            }
        }, 300);
    }

    @Override
    public void refreshRecycleView(final List<RecycleObject> recycleObjectList) {
        removeRefresh();
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefreshRecycle(recycleObjectList);
            }
        }, 300);
    }
}
