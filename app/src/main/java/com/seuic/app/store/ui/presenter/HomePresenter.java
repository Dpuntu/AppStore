package com.seuic.app.store.ui.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.seuic.app.store.ui.contact.HomeContact;
import com.seuic.app.store.ui.fragment.AssortmentFragment;
import com.seuic.app.store.ui.fragment.ManagerFragment;
import com.seuic.app.store.ui.fragment.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */

public class HomePresenter implements HomeContact.Presenter {
    private HomeContact.View mView;

    private List<Fragment> mFragments = new ArrayList<>();

    public HomePresenter(HomeContact.View view) {
        this.mView = view;
    }

    @Override
    public void initHomeFragments() {
        mFragments.add(new RecommendFragment());
        mFragments.add(new AssortmentFragment());
        mFragments.add(new ManagerFragment());
    }


    public PagerAdapter getPagerAdapter() {
        return new FragmentPagerAdapter(mView.getViewSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }
        };
    }
}
