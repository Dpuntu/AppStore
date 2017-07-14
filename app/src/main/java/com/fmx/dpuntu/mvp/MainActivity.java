package com.fmx.dpuntu.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.fmx.dpuntu.utils.AndroidUtils;
import com.fmx.dpuntu.utils.AppInfo;
import com.fmx.dpuntu.utils.LAYOUT;
import com.fmx.dpuntu.utils.Loger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@LAYOUT(contentView = R.layout.activity_main)
public class MainActivity extends BaseActivity implements MainContact.View {
    private MainPresenter presenter;

    @BindView(R.id.appListRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Loger.i("onCreate");
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        presenter = new MainPresenter(MainActivity.this);
    }

    @Override
    protected void onStart() {
        Loger.i("onStart");
        super.onStart();
//        presenter.initView();
        presenter.fromNetToList();
    }

    @Override
    protected int initLayout(boolean isLayoutFail) {
        Loger.i("initLayout");
        if (isLayoutFail) {
            return R.layout.activity_main;
        } else {
            return -1;
        }
    }

    @Override
    public ArrayList<AppInfo> getAppsList() {
        Loger.i("getAppsList");
        return AndroidUtils.getAppInfos(MainActivity.this);
    }

    @Override
    public void getDownLoadAppsList() {
        Loger.i("getDownLoadAppsList");
        presenter.getDownLoadAppsList();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}
