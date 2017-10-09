package com.seuic.app.store.ui.contact;

import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.RecommendReceive;

import java.util.List;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */
public interface UpdateContact {
    interface View {
        void updateRecycleView(List<RecycleObject> recycleObjects);

        void setRecommendReceives(List<RecommendReceive> recommendReceives);
    }

    interface Presenter {
        void checkUpdateApps();

        void updateAllApps(List<RecommendReceive> recommendReceives);
    }
}
