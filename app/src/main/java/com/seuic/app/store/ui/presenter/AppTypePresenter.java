package com.seuic.app.store.ui.presenter;

import com.seuic.app.store.adapter.TypeAppsAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleViewType;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.greendao.TypeAppsTable;
import com.seuic.app.store.ui.contact.AppTypeContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/22.
 *
 * @author dpuntu
 */

public class AppTypePresenter implements AppTypeContact.Presenter {
    private AppTypeContact.View mView;

    public AppTypePresenter(AppTypeContact.View view) {
        this.mView = view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getTypeApps(String typeName, String typeId) {
        if (typeName == null || typeName.isEmpty()) {
            return;
        }
        List<TypeAppsTable> typeAppsTables = GreenDaoManager.getInstance().queryTypeAppsTable(typeName, typeId);
        if (typeAppsTables != null) {
            List<RecycleObject> mRecycleObjectList = new ArrayList<>();
            for (TypeAppsTable typeAppsTable : typeAppsTables) {
                mRecycleObjectList.add(
                        new RecycleObject(RecycleViewType.RECYCEL_DATA,
                                          new RecommendReceive(
                                                  typeAppsTable.getAppName(),
                                                  typeAppsTable.getPackageName(),
                                                  typeAppsTable.getAppSize(),
                                                  typeAppsTable.getAppVersion(),
                                                  typeAppsTable.getAppVersionId(),
                                                  typeAppsTable.getAppDesc(),
                                                  typeAppsTable.getMD5(),
                                                  typeAppsTable.getDownloadName(),
                                                  typeAppsTable.getAppIconName())));
            }
            mView.updateRecycleView(new TypeAppsAdapter(mRecycleObjectList));
        } else {
            return;
        }

//        RxUtils.onErrorInterceptor(
//                ApiManager.getInstance()
//                        .getService()
//                        .getTypeApps(typeId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new AppTypeObserver("AppType"));
    }

//    @SuppressWarnings("unchecked")
//    private class AppTypeObserver extends RxUtils.ResponseObserver<AppTypeReceive> {
//        AppTypeObserver(String observeName) {
//            super(observeName);
//        }
//
//        @Override
//        public void onSuccess(AppTypeReceive appTypeReceive) {
//            List<RecycleObject> mRecycleObjectList = new ArrayList<>();
//            for (RecommendReceive recommendReceive : appTypeReceive.getApps()) {
//                mRecycleObjectList.add(new RecycleObject(RecycleViewType.RECYCEL_DATA, recommendReceive));
//            }
//            mView.updateRecycleView(new TypeAppsAdapter(mRecycleObjectList));
//        }
//
//        @Override
//        public void onError(String errorMsg) {
//            Loger.e("错误提示:" + errorMsg);
//        }
//    }
}
