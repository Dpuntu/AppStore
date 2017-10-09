package com.seuic.app.store.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.seuic.app.store.R;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 *         <p>
 *         带返回键的父类
 */

public abstract class DefaultBaseActivity extends BaseActivity {
    @Override
    protected void initTitle() {
        normalLeft.setOnClickListener(onBackClick());
        normalRightTitle.setOnClickListener(onRightClick());
        setTitleShow(normalTitle, setNormalTitle());
        setTitleShow(normalLeftTitle, setNormalLeftTitle());
        setTitleShow(normalRightTitle, setNormalRightTitle());
        normalTitle.setSingleLine();
    }

    private void setTitleShow(TextView titleView, String title) {
        if (title == null || title.isEmpty()) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title);
        }
    }

    protected View.OnClickListener onRightClick() {
        return null;
    }

    protected View.OnClickListener onBackClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    protected abstract String setNormalRightTitle();

    protected abstract String setNormalLeftTitle();

    protected abstract String setNormalTitle();

    @Override
    protected boolean isHome() {
        return false;
    }

    @Override
    protected int setHeadBackgroundColor() {
        return R.color.appStoreColor;
    }
}
