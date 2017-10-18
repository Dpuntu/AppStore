package com.seuic.app.store.ui.presenter;

import com.seuic.app.store.adapter.BaseRecycleViewAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleTitleMoreBean;
import com.seuic.app.store.bean.response.AdvertisementsReceive;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.net.ApiManager;
import com.seuic.app.store.ui.contact.RecommendContent;
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

public class RecommendPresenter extends BaseFragmentPresenter<RecommendContent.View>
        implements RecommendContent.Presenter {
    private List<AdvertisementsReceive.AdReceiveDetails> mDetailses;
    private boolean isRefresh = false;

    public RecommendPresenter(RecommendContent.View view) {
        super(view);
    }

    @Override
    public void isRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    @Override
    public void getAdvertisements() {
        RxUtils.onErrorInterceptor(
                ApiManager.getInstance()
                        .getService()
                        .getAdvertisements())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AdvertisementsObserver("Advertisements"));
    }


    @Override
    public void getRecommendApps() {
        RxUtils.onErrorInterceptor(
                ApiManager.getInstance()
                        .getService()
                        .getRecommendApps())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RecommendObserver("Recommend"));
    }

    @SuppressWarnings("unchecked")
    private class AdvertisementsObserver extends RxUtils.ResponseObserver<AdvertisementsReceive> {
        AdvertisementsObserver(String observeName) {
            super(observeName);
        }

        @Override
        public void onSuccess(AdvertisementsReceive advertisementsReceive) {
            String count = advertisementsReceive.getCount();
            // 0-否 1-是
            boolean isHidden = advertisementsReceive.getIsHidden().equals("1");
            long intervalTime = Long.parseLong(advertisementsReceive.getIntervalTime()) * 1000;
            if (Integer.parseInt(count) <= 0) {
                count = "1";
                mDetailses.add(new AdvertisementsReceive.AdReceiveDetails(null, "0", null, "0"));
            } else {
                if (mDetailses != null) {
                    mDetailses.clear();
                    mDetailses.addAll(advertisementsReceive.getAdDetails());
                } else {
                    mDetailses = advertisementsReceive.getAdDetails();
                }
            }
            mView.updateBannerView(count, isHidden, intervalTime, mDetailses);
        }

        @Override
        public void onError(String errorMsg) {
            Loger.e("错误提示:" + errorMsg);
            if (mDetailses != null) {
                mDetailses.clear();
            } else {
                mDetailses = new ArrayList<>();
            }
            mDetailses.add(new AdvertisementsReceive.AdReceiveDetails(null, "0", null, "0"));
            mView.updateBannerView("1", false, 5000, mDetailses);
        }
    }

    @SuppressWarnings("unchecked")
    private class RecommendObserver extends RxUtils.ResponseObserver<List<RecommendReceive>> {
        RecommendObserver(String observeName) {
            super(observeName);
        }

        @Override
        public void onSuccess(List<RecommendReceive> recommendReceives) {
            mView.removeRefresh();
            List<RecycleObject> recycleObjectList = new ArrayList<>();
            recycleObjectList.add(new RecycleObject(BaseRecycleViewAdapter.RecycleViewType.RECYCLE_TITLE, new RecycleTitleMoreBean("推荐应用", false, null)));
            for (RecommendReceive recommendReceive : recommendReceives) {
                recycleObjectList.add(new RecycleObject(BaseRecycleViewAdapter.RecycleViewType.RECYCLE_DATA, recommendReceive));
            }
            if (isRefresh) {
                mView.refreshRecycleView(recycleObjectList);
            } else {
                mView.updateRecycleView(recycleObjectList);
            }
        }

        @Override
        public void onError(String errorMsg) {
            mView.removeRefresh();
            Loger.e("错误提示:" + errorMsg);
            mView.onLoadError(errorMsg);
        }
    }
}
