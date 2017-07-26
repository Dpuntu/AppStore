package com.fmx.dpuntu.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.fmx.dpuntu.ui.RVPIndicator;

import java.util.List;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */

public class HomeContract {

    public interface Presenter {
        PagerAdapter getViewPagerAdapter();

        void initPageChangeListener(RVPIndicator mRVPIndicator, ViewPager pager);
    }

    public interface View {
        List<Fragment> initFragments();
    }
}
