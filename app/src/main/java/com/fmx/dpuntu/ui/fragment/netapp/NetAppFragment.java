package com.fmx.dpuntu.ui.fragment.netapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fmx.dpuntu.mvp.R;
import com.fmx.dpuntu.ui.AbsActivity;
import com.fmx.dpuntu.ui.fragment.BaseFragment;
import com.fmx.dpuntu.utils.LAYOUT;

import butterknife.BindView;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */
@LAYOUT(contentView = R.layout.fragment_netapp)
public class NetAppFragment extends BaseFragment implements NetAppContract.View {
    @BindView(R.id.netapplist)
    RecyclerView netAppListView;

    private NetAppPresenter mNetAppPresenter;

    @Override
    protected void initViews(View view) {
        mNetAppPresenter = new NetAppPresenter(NetAppFragment.this, (AbsActivity) context);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_netapp;
    }

    @Override
    public void onStart() {
        super.onStart();
        mNetAppPresenter.fromNetToList();
    }

    @Override
    public void getDownLoadAppsList() {
        mNetAppPresenter.getDownLoadAppsList();
    }

    public RecyclerView getRecyclerView() {
        return netAppListView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mNetAppPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
