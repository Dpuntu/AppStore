package com.seuic.app.store.ui.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.seuic.app.store.greendao.DownloadTaskTable;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.greendao.RecommendReceiveTable;
import com.seuic.app.store.ui.contact.HomeContact;
import com.seuic.app.store.ui.fragment.AssortmentFragment;
import com.seuic.app.store.ui.fragment.ManagerFragment;
import com.seuic.app.store.ui.fragment.RecommendFragment;
import com.seuic.app.store.utils.AppStoreUtils;

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

    @Override
    public int checkDownloadCount() {
        int count = 0;
        List<DownloadTaskTable> downloadTaskTables = GreenDaoManager.getInstance().queryDownloadTaskTable();
        if (downloadTaskTables == null) {
            return count;
        }
        for (DownloadTaskTable downloadTaskTable : downloadTaskTables) {
            RecommendReceiveTable recommendReceiveTable = GreenDaoManager.getInstance().queryRecommendReceive(downloadTaskTable.getTaskId());
            if (recommendReceiveTable == null) {
                continue;
            }
            if (recommendReceiveTable.getPackageName().equals(AppStoreUtils.getAppPackageName())) {
                GreenDaoManager.getInstance().removeDownloadTaskTable(recommendReceiveTable.getAppVersionId());
                GreenDaoManager.getInstance().removeRecommendReceiveTable(recommendReceiveTable.getAppVersionId());
                continue;
            }
            count++;
        }
        return count;
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
