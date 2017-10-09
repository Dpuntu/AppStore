package com.seuic.app.store.ui.presenter;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.adapter.InstallAppAdapter;
import com.seuic.app.store.bean.AppInfo;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleTitleMoreBean;
import com.seuic.app.store.bean.RecycleViewType;
import com.seuic.app.store.ui.contact.InstallContact;
import com.seuic.app.store.utils.AppsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class InstallPresenter implements InstallContact.Presenter {
    private InstallContact.View mView;

    public InstallPresenter(InstallContact.View view) {
        this.mView = view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initInstallApps(List<AppInfo> appInfos) {
        List<RecycleObject> mRecycleObjectList = new ArrayList<>();
        RecycleTitleMoreBean bean = new RecycleTitleMoreBean(AppStoreApplication.getApp().getString(R.string.install), false, null);
        mRecycleObjectList.add(new RecycleObject(RecycleViewType.RECYCEL_TITLE, bean));
        List<AppInfo> appInfoList;
        if (appInfos != null && appInfos.size() > 0) {
            appInfoList = appInfos;
        } else {
            appInfoList = AppStoreApplication.getApp().getAppInfos();
        }
        if (appInfoList == null || appInfoList.size() <= 0) {
            appInfoList = AppsUtils.getUserAppInfos(AppStoreApplication.getApp());
            AppStoreApplication.getApp().setAppInfos(appInfoList);
        }
        for (AppInfo info : appInfoList) {
            mRecycleObjectList.add(new RecycleObject(RecycleViewType.RECYCEL_DATA, info));
        }
        mView.setInstallAppAdapter(new InstallAppAdapter(mRecycleObjectList));
    }

}
