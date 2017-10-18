package com.seuic.app.store.ui.presenter;

import com.seuic.app.store.adapter.BaseRecycleViewAdapter;
import com.seuic.app.store.adapter.ScreenShotAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.ScreenShotBean;
import com.seuic.app.store.bean.response.AppDetailReceive;
import com.seuic.app.store.net.ApiManager;
import com.seuic.app.store.ui.contact.AppDetailContact;
import com.seuic.app.store.utils.Loger;
import com.seuic.app.store.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/9/22.
 *
 * @author dpuntu
 */

public class AppDetailPresenter implements AppDetailContact.Presenter {
    private AppDetailContact.View mView;

    public AppDetailPresenter(AppDetailContact.View view) {
        this.mView = view;
    }

    @Override
    public void getAppDetail(String appPackageName) {
        RxUtils.onErrorInterceptor(
                ApiManager.getInstance()
                        .getService()
                        .getAppDetail(appPackageName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppDetailObserver("AppDetail"));
    }

    private class AppDetailObserver extends RxUtils.ResponseObserver<AppDetailReceive> {
        public AppDetailObserver(String observeName) {
            super(observeName);
        }

        @Override
        public void onSuccess(AppDetailReceive appDetailReceive) {
            mView.updateView(appDetailReceive);
            loadScreenShot(appDetailReceive);
        }

        @Override
        public void onError(String errorMsg) {
            Loger.e("错误提示:" + errorMsg);
        }
    }

    List<RecycleObject> screenShots = new ArrayList<>();

    private void loadScreenShot(AppDetailReceive appDetailReceive) {
        checkScreenShotName(appDetailReceive.getAppShot1());
        checkScreenShotName(appDetailReceive.getAppShot2());
        checkScreenShotName(appDetailReceive.getAppShot3());
        checkScreenShotName(appDetailReceive.getAppShot4());
        checkScreenShotName(appDetailReceive.getAppShot5());
        if (screenShots.size() <= 0) {
            screenShots.add(null);
        }
        mView.updateRecycleView(new ScreenShotAdapter(screenShots));
    }

    private void checkScreenShotName(String appShot) {
        if (appShot != null && !appShot.isEmpty()) {
            screenShots.add(new RecycleObject<>(BaseRecycleViewAdapter.RecycleViewType.RECYCLE_DATA, new ScreenShotBean(appShot)));
        }
    }
}
