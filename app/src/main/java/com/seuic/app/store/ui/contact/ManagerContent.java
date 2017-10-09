package com.seuic.app.store.ui.contact;

import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.RecommendReceive;

import java.util.List;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 */

public interface ManagerContent {
    interface View {
        void updateView(List<RecycleObject> mRecycleObjectList);

        void refreshView(String updateCount, String updateSelf);

        void updateSelf(RecommendReceive recommendReceive);
    }


    interface Presenter {
        void checkUpdate(boolean isRefresh);

        void checkGreenDao4Self();
    }
}
