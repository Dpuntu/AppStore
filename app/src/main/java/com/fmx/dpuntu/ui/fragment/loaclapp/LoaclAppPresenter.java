package com.fmx.dpuntu.ui.fragment.loaclapp;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fmx.dpuntu.ui.BaseActivity;
import com.fmx.dpuntu.ui.RecyclerViewDecoration;
import com.fmx.dpuntu.utils.AppInfo;
import com.fmx.dpuntu.utils.AppRecyclerViewAdapter;
import com.fmx.dpuntu.utils.Loger;

import java.util.ArrayList;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */

public class LoaclAppPresenter implements LocalAppContact.Presenter, AppRecyclerViewAdapter.LocalRecyclerViewClickListener {
    private LocalAppContact.View view;
    private BaseActivity context;
    private AppRecyclerViewAdapter mAppRecyclerViewAdapter = null;

    public LoaclAppPresenter(LocalAppContact.View view, BaseActivity context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void initAppList() {
        ArrayList<AppInfo> appInfos = view.getAppsList();
        Loger.e("appInfos.size = " + appInfos.size());

        if (appInfos.size() > 0) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            ((LocalAppFragment) view).getLocalAppListView().setLayoutManager(mLayoutManager);
            ((LocalAppFragment) view).getLocalAppListView().addItemDecoration(new RecyclerViewDecoration(context, RecyclerViewDecoration.VERTICAL_LIST));
            mAppRecyclerViewAdapter = new AppRecyclerViewAdapter(context, appInfos);
            ((LocalAppFragment) view).getLocalAppListView().setAdapter(mAppRecyclerViewAdapter);
        }

        if (mAppRecyclerViewAdapter != null) {
            mAppRecyclerViewAdapter.setLocalRecyclerViewClickListener(this);
        }
    }

    @Override
    public void onClick(View v, AppInfo info) {
        Loger.e(info.getAppName());
    }
}
