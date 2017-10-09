package com.seuic.app.store.ui.contact;

import com.seuic.app.store.adapter.InstallAppAdapter;
import com.seuic.app.store.bean.AppInfo;

import java.util.List;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */
public interface InstallContact {
    interface View {
        void setInstallAppAdapter(InstallAppAdapter mInstallAppAdapter);
    }

    interface Presenter {
        void initInstallApps(List<AppInfo> appInfos);
    }
}
