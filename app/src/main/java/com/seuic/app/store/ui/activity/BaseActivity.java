package com.seuic.app.store.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.view.RedPointView;
import com.seuic.app.store.view.SearchBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */

public abstract class BaseActivity extends AbsStatusBarActivity {
    private Unbinder mUnbinder;
    private View homeLayout;
    private View normalLayout;
    protected RelativeLayout normalLeft;
    protected TextView normalTitle, normalLeftTitle, normalRightTitle;
    protected ImageView normalImage;
    protected FrameLayout mRightLayout;
    protected ImageView homeDownImage;
    protected SearchBar mSearchBar;
    protected RedPointView mRedPointView;
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abs);
        FrameLayout headLayout = (FrameLayout) findViewById(R.id.abs_head);
        FrameLayout bodyLayout = (FrameLayout) findViewById(R.id.abs_body);
        View headView = LayoutInflater.from(BaseActivity.this).inflate(R.layout.actionbar_appstore, headLayout, false);
        headLayout.addView(headView);
        View bodyView = LayoutInflater.from(BaseActivity.this).inflate(initLayout(), bodyLayout, false);
        bodyLayout.addView(bodyView);
        mUnbinder = ButterKnife.bind(this, bodyView);
        initHeadView(headView);
    }

    private void initHeadView(View headView) {
        headView.setBackgroundResource(setHeadBackgroundColor());
        homeLayout = headView.findViewById(R.id.home_appstore);
        normalLayout = headView.findViewById(R.id.normal_appstore);
        // 主页
        homeDownImage = (ImageView) homeLayout.findViewById(R.id.actionbar_home_down_image);
        mSearchBar = (SearchBar) homeLayout.findViewById(R.id.actionbar_home_search);
        mRedPointView = (RedPointView) homeLayout.findViewById(R.id.actionbar_home_down_num);
        mRightLayout = (FrameLayout) homeLayout.findViewById(R.id.actionbar_home_down);
        // 其他
        normalTitle = (TextView) normalLayout.findViewById(R.id.actionbar_title);
        normalLeftTitle = (TextView) normalLayout.findViewById(R.id.actionbar_left_title);
        normalRightTitle = (TextView) normalLayout.findViewById(R.id.actionbar_right_title);
        normalImage = (ImageView) normalLayout.findViewById(R.id.actionbar_left_image);
        normalLeft = (RelativeLayout) normalLayout.findViewById(R.id.actionbar_left);
        initTitle();
        homeLayout.setVisibility(isHome() ? View.VISIBLE : View.GONE);
        normalLayout.setVisibility(isHome() ? View.GONE : View.VISIBLE);
        eventHandler();
    }

    protected abstract void initTitle();

    protected abstract void eventHandler();

    protected abstract int initLayout();

    protected abstract boolean isHome();

    protected abstract int setHeadBackgroundColor();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
