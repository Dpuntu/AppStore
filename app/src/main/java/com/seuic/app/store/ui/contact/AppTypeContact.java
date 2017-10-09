package com.seuic.app.store.ui.contact;

import com.seuic.app.store.adapter.TypeAppsAdapter;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */
public interface AppTypeContact {
    interface View {
        void updateRecycleView(TypeAppsAdapter typeAppsAdapter);
    }

    interface Presenter {
        void getTypeApps(String typeName, String typeId);
    }
}
