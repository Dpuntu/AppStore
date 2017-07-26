package com.fmx.dpuntu.ui;

import android.content.Context;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */

public abstract class AbsActivity extends BaseActivity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ButterKnife.bind(this);
        initDatas();
    }

    public abstract void initDatas();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
