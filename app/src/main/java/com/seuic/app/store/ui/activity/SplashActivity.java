package com.seuic.app.store.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.utils.AndroidUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 2017/9/15.
 *
 * @author dpuntu
 */

public class SplashActivity extends AbsStatusBarActivity {
    private Unbinder mUnBinder;
    private boolean flag = false;
    private Runnable mRunnable;

    @BindView(R.id.app_store_skip)
    TextView skipText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtils.initStatusBar(SplashActivity.this);
        setContentView(R.layout.activity_splash);
        mUnBinder = ButterKnife.bind(SplashActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        skipTextFunction();
    }

    /**
     * 设置skipText的功能
     */
    private void skipTextFunction() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                goHome();
            }
        };

        skipText.postDelayed(mRunnable, 3000);

        skipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = true;
        if (mRunnable != null) {
            skipText.removeCallbacks(mRunnable);
        }
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    private synchronized void goHome() {
        if (!flag) {
            flag = true;
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }
    }
}
