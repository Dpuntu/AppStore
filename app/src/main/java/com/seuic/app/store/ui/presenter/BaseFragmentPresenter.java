package com.seuic.app.store.ui.presenter;

import com.seuic.app.store.ui.contact.FragmentBaseContent;

/**
 * Created on 2017/10/11.
 *
 * @author dpuntu
 */

public abstract class BaseFragmentPresenter<T extends FragmentBaseContent.View> implements FragmentBaseContent.Presenter {
    protected T mView;

    BaseFragmentPresenter(T mView) {
        this.mView = mView;
    }
}
