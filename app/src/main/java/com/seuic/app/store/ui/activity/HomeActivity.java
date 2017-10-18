package com.seuic.app.store.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.seuic.app.store.R;
import com.seuic.app.store.listener.DownloadCountListener;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.ui.contact.HomeContact;
import com.seuic.app.store.ui.presenter.HomePresenter;
import com.seuic.app.store.utils.ToastUtils;
import com.seuic.app.store.view.RedPointView;
import com.seuic.app.store.view.ViewPagerIndicator;

import butterknife.BindView;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */
public class HomeActivity extends HomeBaseActivity implements HomeContact.View, DownloadCountListener {
    private HomePresenter mPresenter;

    @BindView(R.id.home_viewpager_head)
    ViewPagerIndicator mPagerHeadSelector;
    @BindView(R.id.home_viewpager)
    ViewPager mViewPager;

    private long backTime = 0;
    private static final long TIME_SPACE = 2000;

    @Override
    protected void eventHandler() {
        mPresenter = new HomePresenter(HomeActivity.this);
        mPresenter.initHomeFragments();
        mViewPager.setAdapter(mPresenter.getPagerAdapter());
        mPagerHeadSelector.setViewPager(mViewPager, 0);
        mPagerHeadSelector.addOnPageChangeListener(mOnPageChangeListener);
        DownloadManager.getInstance().setDownloadCountListener(this);
        updateResetRedPoint(mPresenter.checkDownloadCount());
    }

    private void updateResetRedPoint(int count) {
        if (count <= 0) {
            resetRedPoint(RedPointView.RedPointType.TYPE_GONE, null);
        } else {
            resetRedPoint(RedPointView.RedPointType.TYPE_NUM, count + "");
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    public FragmentManager getViewSupportFragmentManager() {
        return getSupportFragmentManager();
    }

    private ViewPagerIndicator.OnPageChangeListener mOnPageChangeListener = new ViewPagerIndicator.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected boolean isSearchBarFocusable() {
        return false;
    }

    @Override
    protected boolean isRightLayoutShow() {
        return true;
    }

    @Override
    protected View.OnClickListener homeDownImageClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DownLoadActivity.class));
            }
        };
    }

    @Override
    protected View.OnClickListener searchClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onDownloadCountChange(int count) {
        updateResetRedPoint(count);
    }

    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - backTime >= TIME_SPACE) {
            ToastUtils.showToast("再按一次退出");
            backTime = nowTime;
        } else {
            System.exit(0);
        }
    }
}
