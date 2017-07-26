package com.fmx.dpuntu.ui.fragment.loaclapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fmx.dpuntu.mvp.R;
import com.fmx.dpuntu.ui.AbsActivity;
import com.fmx.dpuntu.ui.fragment.BaseFragment;
import com.fmx.dpuntu.utils.AndroidUtils;
import com.fmx.dpuntu.utils.AppInfo;
import com.fmx.dpuntu.utils.LAYOUT;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */
@LAYOUT(contentView = R.layout.fragment_localapp)
public class LocalAppFragment extends BaseFragment implements LocalAppContract.View {
    @BindView(R.id.localapplist)
    RecyclerView localAppListView;

    private LoaclAppPresenter mLoaclAppPresenter;

    @Override
    protected void initViews(View view) {
        mLoaclAppPresenter = new LoaclAppPresenter(LocalAppFragment.this, (AbsActivity) context);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLoaclAppPresenter.initAppList();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_localapp;
    }

    @Override
    public ArrayList<AppInfo> getAppsList() {
        return AndroidUtils.getAppInfos(context);
    }

    public RecyclerView getLocalAppListView() {
        return localAppListView;
    }
}
