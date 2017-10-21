package com.seuic.app.store.ui.activity;

import android.support.v7.widget.RecyclerView;

import com.seuic.app.store.R;
import com.seuic.app.store.adapter.InstallAppAdapter;
import com.seuic.app.store.bean.AppInfo;
import com.seuic.app.store.ui.agent.ActivityService;
import com.seuic.app.store.ui.contact.InstallContact;
import com.seuic.app.store.ui.presenter.InstallPresenter;
import com.seuic.app.store.utils.AppsUtils;
import com.seuic.app.store.view.recycleview.RecyclerViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class InstallActivity extends DefaultBaseActivity<ActivityService> implements InstallContact.View {
    private InstallPresenter mInstallPresenter;
    private boolean isRefresh = false;
    @BindView(R.id.install_recycle)
    RecyclerView mRecyclerView;

    @Override
    protected void initService() {
        ActivityService mActivityService = createService(ActivityService.class);
        mActivityService.installActivity();
    }

    @Override
    protected void eventHandler() {
        isRefresh = false;
        mInstallPresenter = new InstallPresenter(this);
        mInstallPresenter.initInstallApps(null);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterSuccess(List<AppInfo> appInfos) {
        mInstallPresenter.initInstallApps(appInfos);
    }

    @Override
    public void setInstallAppAdapter(InstallAppAdapter mInstallAppAdapter) {
        RecyclerViewUtils.createVerticalRecyclerView(this, mRecyclerView, !isRefresh, true);
        isRefresh = false;
        mRecyclerView.setAdapter(mInstallAppAdapter);
        mInstallAppAdapter.setTextOnClickListener(new InstallAppAdapter.TextOnClickListener() {
            @Override
            public void unInstall(AppInfo info) {
                AppsUtils.unInstallApp(info.getPackageName());
            }
        });
    }
}
