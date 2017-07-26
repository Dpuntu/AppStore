package com.fmx.dpuntu.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.fmx.dpuntu.ui.RVPIndicator;

import java.util.List;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private List<Fragment> fragmentList;

    HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public PagerAdapter getViewPagerAdapter() {
        fragmentList = view.initFragments();
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(((HomeActivity) view).getSupportFragmentManager());
        return mFragmentAdapter;
    }

    @Override
    public void initPageChangeListener(RVPIndicator mRVPIndicator,ViewPager pager) {
        pager.setCurrentItem(0);
        mRVPIndicator.setOnPageChangeListener(new RVPIndicator.PageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class FragmentAdapter extends FragmentStatePagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
