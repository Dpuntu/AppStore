package com.seuic.app.store.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 *         <p>
 *         沉浸模式父类
 */

public abstract class AbsStatusBarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // home base xml add //android:layout_marginTop="24dip"// tag
        // AndroidUtils.initStatusBar(AbsStatusBarActivity.this);
    }
}
