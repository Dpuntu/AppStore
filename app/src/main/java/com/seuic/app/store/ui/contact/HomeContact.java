package com.seuic.app.store.ui.contact;

import android.support.v4.app.FragmentManager;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */

public interface HomeContact {
    interface View {
        FragmentManager getViewSupportFragmentManager();
    }

    interface Presenter {
        void initHomeFragments();
    }
}
