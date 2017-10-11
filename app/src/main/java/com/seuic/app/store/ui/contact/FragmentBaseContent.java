package com.seuic.app.store.ui.contact;

import com.seuic.app.store.bean.RecycleObject;

import java.util.List;

/**
 * Created on 2017/9/25.
 *
 * @author dpuntu
 */

public interface FragmentBaseContent {
    interface View {
        void updateRecycleView(List<RecycleObject> recycleObjectList);

        void refreshRecycleView(List<RecycleObject> recycleObjectList);

        void onLoadError(String errorMsg);

        void removeRefresh();
    }

    interface Presenter {
    }
}
