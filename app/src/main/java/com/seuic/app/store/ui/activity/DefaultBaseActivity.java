package com.seuic.app.store.ui.activity;

import android.view.View;
import android.widget.TextView;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 *         <p>
 *         带返回键的父类
 */

public abstract class DefaultBaseActivity<T> extends BaseActivity<T> {
    @Override
    protected void initTitle() {
        normalLeft.setOnClickListener(onBackClick());
        normalRightTitle.setOnClickListener(onRightClick());
        setTitleShow(normalTitle, normalTitleText);
        setTitleShow(normalLeftTitle, leftTitleText);
        setTitleShow(normalRightTitle, rightTitleText);
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
}
