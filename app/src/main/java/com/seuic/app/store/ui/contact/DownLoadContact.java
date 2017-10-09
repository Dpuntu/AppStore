package com.seuic.app.store.ui.contact;

import com.seuic.app.store.bean.RecycleObject;

import java.util.List;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */

public interface DownLoadContact {
    interface View {
        void updateRecycleView(List<RecycleObject> recycleObjects);

        void refreshRecycleView(List<RecycleObject> recycleObjects);
    }

    interface Presenter {
        void queryDownLoadData(boolean isRefresh);
    }
}
