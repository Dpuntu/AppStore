package com.seuic.app.store.ui.presenter;

import android.view.View;

import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleTitleMoreBean;
import com.seuic.app.store.bean.RecycleViewType;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.greendao.CheckUpdateAppsTable;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.ui.contact.UpdateContact;
import com.seuic.app.store.ui.dialog.DialogManager;
import com.seuic.app.store.utils.Loger;
import com.seuic.app.store.utils.NetworkUtils;
import com.seuic.app.store.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/26.
 *
 * @author dpuntu
 */

public class UpdatePresenter implements UpdateContact.Presenter {
    private UpdateContact.View mView;

    public UpdatePresenter(UpdateContact.View view) {
        this.mView = view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void checkUpdateApps() {
        List<CheckUpdateAppsTable> checkUpdateAppsTableList = GreenDaoManager.getInstance().queryCheckUpdateApps();
        List<RecycleObject> recycleObjects = new ArrayList<>();
        recycleObjects.add(new RecycleObject(RecycleViewType.RECYCEL_TITLE,
                                             new RecycleTitleMoreBean("可用更新", false, "")));
        List<RecommendReceive> recommendReceives = new ArrayList<>();
        if (checkUpdateAppsTableList != null && checkUpdateAppsTableList.size() > 0) {
            for (CheckUpdateAppsTable checkUpdateAppsTable : checkUpdateAppsTableList) {
                RecommendReceive recommendReceive = new RecommendReceive(checkUpdateAppsTable.getAppName(),
                                                                         checkUpdateAppsTable.getPackageName(),
                                                                         checkUpdateAppsTable.getAppSize(),
                                                                         checkUpdateAppsTable.getAppVersion(),
                                                                         checkUpdateAppsTable.getAppVersionId(),
                                                                         checkUpdateAppsTable.getAppVersionDesc(),
                                                                         checkUpdateAppsTable.getAppDesc(),
                                                                         checkUpdateAppsTable.getMD5(),
                                                                         checkUpdateAppsTable.getDownloadName(),
                                                                         checkUpdateAppsTable.getAppIconName());
                recommendReceives.add(recommendReceive);
                recycleObjects.add(new RecycleObject(RecycleViewType.RECYCEL_DATA, recommendReceive));
            }
        }
        mView.setRecommendReceives(recommendReceives);
        mView.updateRecycleView(recycleObjects);
    }

    @Override
    public void updateAllApps(final List<RecommendReceive> recommendReceives) {
        NetworkUtils.NETTYPE netType = NetworkUtils.getNetType();
        switch (netType) {
            case NONE_NET:
                ToastUtils.showToast("请检查网络链接是否正常");
                break;
            case DATA_NET:
                DialogManager.getInstance()
                        .showHintDialog("下载提示",
                                        "正在使用数据连接是否继续下载？",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                DialogManager.getInstance().dismissHintDialog();
                                                downloadAllApps(recommendReceives);
                                            }
                                        },
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                DialogManager.getInstance().dismissHintDialog();
                                            }
                                        });
                break;
            case WIFI_NET:
                downloadAllApps(recommendReceives);
                break;
        }
    }

    private void downloadAllApps(List<RecommendReceive> recommendReceives) {
        DialogManager.getInstance().dismissHintDialog();
        if (recommendReceives != null && recommendReceives.size() > 0) {
            for (RecommendReceive recommendReceive : recommendReceives) {
                DownloadManager.getInstance().start(recommendReceive.getAppVersionId());
            }
        } else {
            Loger.e("没有可以更新的软件");
        }
    }
}
