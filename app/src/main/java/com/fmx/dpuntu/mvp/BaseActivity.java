package com.fmx.dpuntu.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fmx.dpuntu.utils.LAYOUT;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public abstract class BaseActivity extends AppCompatActivity {
    private LAYOUT mLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = getClass().getAnnotation(LAYOUT.class);
        int layout = mLayout.contentView();
        if (layout != -1) {
            setContentView(layout);
        } else {
            setContentView(initLayout(true));
        }
    }

    protected abstract int initLayout(boolean isLayoutFail);
}
