package com.seuic.app.store.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.seuic.app.store.R;
import com.seuic.app.store.adapter.BaseTitleRecycleViewAdapter;
import com.seuic.app.store.adapter.SearchAdapter;
import com.seuic.app.store.adapter.SearchHistoryAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleSearchBean;
import com.seuic.app.store.ui.contact.SearchContact;
import com.seuic.app.store.ui.presenter.SearchPresenter;
import com.seuic.app.store.utils.Loger;
import com.seuic.app.store.view.SearchBar;
import com.seuic.app.store.view.recycleview.RecyclerViewUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */

public class SearchActivity extends HomeBaseActivity implements SearchContact.View, SearchBar.SearchBarTextWatcher {
    @BindView(R.id.search_history_recycle)
    RecyclerView mHistoryRecyclerView;
    @BindView(R.id.search_recycle)
    RecyclerView mSearchRecyclerView;
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private SearchPresenter mSearchPresenter;
    private SearchAdapter mSearchAdapter;
    private String searchBarText = null;

    @Override
    protected void eventHandler() {
        mSearchPresenter = new SearchPresenter(this);
        mSearchRecyclerView.setVisibility(View.GONE);
        mHistoryRecyclerView.setVisibility(View.GONE);
    }

    @Override
    protected SearchBar.SearchBarTextWatcher textWatcher() {
        return this;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected boolean isSearchBarFocusable() {
        return true;
    }

    @Override
    protected boolean isRightLayoutShow() {
        return false;
    }

    @Override
    protected SearchBar.OnSearchClickListener homeSearch() {
        return new SearchBar.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, CharSequence text, int searchCode) {
                switch (searchCode) {
                    case SearchBar.SEARCH_ERROR:
                        Loger.e("错误提示:" + text);
                        break;
                    case SearchBar.SEARCH_SUCCESS:
                        mSearchRecyclerView.setVisibility(View.VISIBLE);
                        mHistoryRecyclerView.setVisibility(View.GONE);
                        mSearchPresenter.searchApps(text.toString());
                        break;
                }
            }
        };
    }

    @Override
    public void searchBarTextChange(String searchBarText) {
        if (searchBarText != null && !searchBarText.isEmpty()) {
            this.searchBarText = searchBarText;
            mSearchPresenter.queryHistorySearch(searchBarText);
            mHistoryRecyclerView.setVisibility(View.VISIBLE);
            mSearchRecyclerView.setVisibility(View.GONE);
        } else {
            mHistoryRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateHistoryRecycleView(List<RecycleObject> recycleObjects) {
        if (mSearchHistoryAdapter == null) {
            createSearchHistoryAdapter(recycleObjects);
        } else {
            mSearchHistoryAdapter.refreshAdapter(recycleObjects);
        }
    }

    @Override
    public void updateSearchRecycleView(List<RecycleObject> recycleObjects) {
        if (mSearchAdapter == null) {
            createSearchAdapter(recycleObjects);
        } else {
            mSearchAdapter.refreshAdapter(recycleObjects);
        }
    }

    private void createSearchAdapter(List<RecycleObject> recycleObjects) {
        mSearchAdapter = new SearchAdapter(recycleObjects);
        RecyclerViewUtils.createVerticalRecyclerView(this,mSearchRecyclerView,true,true);
        mSearchRecyclerView.setAdapter(mSearchAdapter);
    }

    private void createSearchHistoryAdapter(List<RecycleObject> recycleObjects) {
        mSearchHistoryAdapter = new SearchHistoryAdapter(recycleObjects);
        RecyclerViewUtils.createVerticalRecyclerView(this,mHistoryRecyclerView,true,true);
        mHistoryRecyclerView.setAdapter(mSearchHistoryAdapter);

        mSearchHistoryAdapter.setOnItemClickListener(new BaseTitleRecycleViewAdapter.OnItemClickListener<RecycleSearchBean>() {
            @Override
            public void onItemClick(View view, RecycleSearchBean recycleSearchBean) {
                mSearchPresenter.searchApps(recycleSearchBean.getAppName());
                mSearchBar.setText(recycleSearchBean.getAppName());
                mSearchRecyclerView.setVisibility(View.VISIBLE);
                mHistoryRecyclerView.setVisibility(View.GONE);
            }
        });

        mSearchHistoryAdapter.setOnDeleteClickListener(new SearchHistoryAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(View view, RecycleSearchBean recycleSearchBean) {
                if (searchBarText != null && !searchBarText.isEmpty()) {
                    mSearchPresenter.queryHistorySearch(searchBarText);
                    mHistoryRecyclerView.setVisibility(View.VISIBLE);
                    mSearchRecyclerView.setVisibility(View.GONE);
                } else {
                    mHistoryRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }
}
