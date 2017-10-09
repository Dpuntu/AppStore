package com.fmx.dpuntu;

import android.app.Application;

import com.fmx.dpuntu.component.ApiModule;
import com.fmx.dpuntu.component.AppComponent;
import com.fmx.dpuntu.component.AppModule;
import com.fmx.dpuntu.component.DaggerAppComponent;

/**
 * Created on 2017/8/2.
 *
 * @author dpuntu
 */

public class AppStoreApp extends Application {

    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
    }

    private void initComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .build();
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

}
