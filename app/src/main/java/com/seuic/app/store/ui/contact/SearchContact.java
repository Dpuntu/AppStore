package com.seuic.app.store.ui.contact;

import com.seuic.app.store.bean.RecycleObject;

import java.util.List;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */
public interface SearchContact {
    interface View {
        void updateHistoryRecycleView(List<RecycleObject> recycleObjects);

        void updateSearchRecycleView(List<RecycleObject> recycleObjects);
    }

    interface Presenter {
        void queryHistorySearch(String searchText);

        void searchApps(String appName);
    }
}
