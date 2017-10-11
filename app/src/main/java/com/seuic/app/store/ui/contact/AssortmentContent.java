package com.seuic.app.store.ui.contact;


/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 */

public interface AssortmentContent {
    interface View extends FragmentBaseContent.View {
    }

    interface Presenter extends FragmentBaseContent.Presenter {
        void getAppTypes(boolean isRefresh);
    }
}
