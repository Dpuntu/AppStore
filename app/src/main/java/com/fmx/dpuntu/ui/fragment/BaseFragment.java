package com.fmx.dpuntu.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fmx.dpuntu.utils.LAYOUT;

import butterknife.ButterKnife;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */

public abstract class BaseFragment extends Fragment {
    private LAYOUT mLayout;
    protected Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayout = getClass().getAnnotation(LAYOUT.class);
        int layout = mLayout.contentView();
        View rootView;
        if (layout != -1) {
            rootView = inflater.inflate(layout, container, false);
        } else {
            rootView = inflater.inflate(initLayout(), container, false);
        }
        context = getActivity();
        ButterKnife.bind(this, rootView);
        initViews(rootView);
        return rootView;
    }

    protected abstract void initViews(View view);

    protected abstract int initLayout();
}
