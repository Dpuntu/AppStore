package com.fmx.dpuntu.component;

import android.content.Context;

import com.fmx.dpuntu.api.ApiService;

import dagger.Component;

/**
 * Created on 2017/8/2.
 *
 * @author dpuntu
 */
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
    Context getContext();

    ApiService getApiService();
}
