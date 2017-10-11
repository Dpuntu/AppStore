package com.seuic.app.store.ui.contact;

import com.seuic.app.store.bean.response.AdvertisementsReceive;

import java.util.List;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 */

public interface RecommendContent {
    interface View extends FragmentBaseContent.View{
        void updateBannerView(String count, boolean isHidden, long intervalTime, List<AdvertisementsReceive.AdReceiveDetails> mDetailses);
    }

    interface Presenter extends FragmentBaseContent.Presenter {
        void isRefresh(boolean isRefresh);

        void getAdvertisements();

        void getRecommendApps();
    }
}
