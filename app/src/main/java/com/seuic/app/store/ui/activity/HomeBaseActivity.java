package com.seuic.app.store.ui.activity;

import android.view.View;

import com.seuic.app.store.R;
import com.seuic.app.store.view.RedPointView;
import com.seuic.app.store.view.SearchBar;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 *         <p>
 *         待搜索框的父类
 */

public abstract class HomeBaseActivity extends BaseActivity {
    @Override
    protected void initTitle() {
        homeDownImage.setOnClickListener(homeDownImageClick());
        mSearchBar.setOnClickListener(searchClick());
        mSearchBar.setOnSearchClickListener(homeSearch());
        mSearchBar.addSearchBarTextWatcher(textWatcher());
        mSearchBar.setFocusable(isSearchBarFocusable());
        mRightLayout.setVisibility(isRightLayoutShow() ? View.VISIBLE : View.GONE);
    }

    protected SearchBar.SearchBarTextWatcher textWatcher() {
        return null;
    }


    protected void resetRedPoint(final RedPointView.RedPointType type, final String contentText) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mRedPointView.setTypeText(type, contentText);
            }
        });
    }

    protected abstract boolean isSearchBarFocusable();

    protected abstract boolean isRightLayoutShow();

    protected View.OnClickListener homeDownImageClick() {
        return null;
    }

    protected View.OnClickListener searchClick() {
        return null;
    }

    protected SearchBar.OnSearchClickListener homeSearch() {
        return null;
    }

    protected void reSetSearchBar() {
        mSearchBar.setText(mSearchBar.getText());
    }

    protected boolean isHome() {
        return true;
    }

    protected int setHeadBackgroundColor() {
        return R.color.appStoreColor;
    }
}
