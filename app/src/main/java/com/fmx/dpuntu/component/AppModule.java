package com.fmx.dpuntu.component;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 2017/8/2.
 *
 * @author dpuntu
 */
@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context providerContext() {
        return context;
    }

}
