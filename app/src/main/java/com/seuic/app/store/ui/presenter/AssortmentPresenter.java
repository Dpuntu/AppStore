package com.seuic.app.store.ui.presenter;

import com.seuic.app.store.adapter.BaseRecycleViewAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleTitleMoreBean;
import com.seuic.app.store.bean.response.AppTypeReceive;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.net.ApiManager;
import com.seuic.app.store.ui.contact.AssortmentContent;
import com.seuic.app.store.utils.Loger;
import com.seuic.app.store.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 */

public class AssortmentPresenter extends BaseFragmentPresenter<AssortmentContent.View> implements AssortmentContent.Presenter {
    private boolean isRefresh = false;

    public AssortmentPresenter(AssortmentContent.View view) {
        super(view);
    }

    @Override
    public void getAppTypes(boolean isRefresh) {
        // 查询数据前，先删除所有历史数据
        GreenDaoManager.getInstance().removeTypeAppsTableAll();
        this.isRefresh = isRefresh;
        RxUtils.onErrorInterceptor(
                ApiManager.getInstance()
                        .getService()
                        .getAppTypes())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppTypesObserver("AppTypes"));
    }


    @SuppressWarnings("unchecked")
    private class AppTypesObserver extends RxUtils.ResponseObserver<List<AppTypeReceive>> {
        AppTypesObserver(String observeName) {
            super(observeName);
        }

        @Override
        public void onSuccess(List<AppTypeReceive> appTypeReceives) {
            List<RecycleObject> recycleObjectList = new ArrayList<>();
            for (AppTypeReceive appTypeReceive : appTypeReceives) {
                recycleObjectList.add(new RecycleObject(
                        BaseRecycleViewAdapter.RecycleViewType.RECYCLE_TITLE,
                        new RecycleTitleMoreBean(
                                appTypeReceive.getAppType(),
                                true,
                                appTypeReceive.getAppTypeId())));
                List<RecycleObject> recommendReceiveList = new ArrayList<>();
                for (int i = 0; i < appTypeReceive.getApps().size(); i++) {
                    // 将该类的所有APP存储到数据库
                    GreenDaoManager.getInstance().insertTypeAppsTableDao(
                            appTypeReceive.getApps().get(i),
                            appTypeReceive.getAppType(),
                            appTypeReceive.getAppTypeId());
                    if (i < 4) {
                        recommendReceiveList.add(new RecycleObject(
                                BaseRecycleViewAdapter.RecycleViewType.RECYCLE_APP_TYPE,
                                appTypeReceive.getApps().get(i)));
                    }
                }
                recycleObjectList.add(new RecycleObject(BaseRecycleViewAdapter.RecycleViewType.RECYCLE_DATA, recommendReceiveList));
            }
            if (isRefresh) {
                mView.refreshRecycleView(recycleObjectList);
            } else {
                mView.updateRecycleView(recycleObjectList);
            }
        }

        @Override
        public void onError(String errorMsg) {
            Loger.e("错误提示:" + errorMsg);
            mView.onLoadError(errorMsg);
        }
    }
}
