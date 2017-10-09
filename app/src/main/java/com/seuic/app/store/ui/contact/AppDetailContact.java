package com.seuic.app.store.ui.contact;

import com.seuic.app.store.adapter.ScreenShotAdapter;
import com.seuic.app.store.bean.response.AppDetailReceive;

/**
 * Created on 2017/9/22.
 *
 * @author dpuntu
 */

public interface AppDetailContact {
    interface View {
        void updateView(AppDetailReceive appDetailReceive);

        void updateRecycleView(ScreenShotAdapter screenShotAdapter);
    }

    interface Presenter {
        void getAppDetail(String appPackageName);
    }
}
