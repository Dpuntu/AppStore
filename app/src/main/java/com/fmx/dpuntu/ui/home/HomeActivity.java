package com.fmx.dpuntu.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.fmx.dpuntu.mvp.R;
import com.fmx.dpuntu.ui.AbsActivity;
import com.fmx.dpuntu.ui.RVPIndicator;
import com.fmx.dpuntu.ui.fragment.help.HelpFragment;
import com.fmx.dpuntu.ui.fragment.loaclapp.LocalAppFragment;
import com.fmx.dpuntu.ui.fragment.netapp.NetAppFragment;
import com.fmx.dpuntu.utils.LAYOUT;
import com.fmx.dpuntu.utils.StatusBarCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */
@LAYOUT(contentView = R.layout.activity_home)
public class HomeActivity extends AbsActivity implements HomeContract.View {
    @BindView(R.id.home_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.home_indicator)
    RVPIndicator mRVPIndicator;

    private List<String> mDatas;
    private HomePresenter mHomePresenter;

    @Override
    public void initDatas() {
        StatusBarCompat.setWindowStatusBarColor(this, R.color.colorPrimary); // mRVPIndicator背景色
        mHomePresenter = new HomePresenter(HomeActivity.this);
        mViewPager.setAdapter(mHomePresenter.getViewPagerAdapter());
        mHomePresenter.initPageChangeListener(mRVPIndicator, mViewPager);
        mDatas = Arrays.asList(getResources().getStringArray(R.array.home_tabs));
        mRVPIndicator.setTabItemTitles(mDatas);
        mRVPIndicator.setViewPager(mViewPager, 0);
    }

    @Override
    protected int initLayout(boolean isLayoutFail) {
        return R.layout.activity_home;
    }

    @Override
    public List<Fragment> initFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new NetAppFragment());
        fragments.add(new LocalAppFragment());
        fragments.add(new HelpFragment());
        return fragments;
    }
}
