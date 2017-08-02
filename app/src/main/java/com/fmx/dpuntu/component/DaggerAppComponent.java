package com.fmx.dpuntu.component;

import android.content.Context;

import com.fmx.dpuntu.api.ApiService;

import javax.annotation.Generated;

/**
 * Created on 2017/8/2.
 *
 * @author dpuntu
 */

@Generated("dagger.internal.codegen.ComponentProcessor")
public class DaggerAppComponent implements AppComponent {
    private AppModule appModule;
    private ApiModule apiModule;

    public DaggerAppComponent(Builder mBuilder) {
        this.appModule = mBuilder.getAppModule();
        this.apiModule = mBuilder.getApiModule();
    }

    @Override
    public Context getContext() {
        return appModule.providerContext();
    }

    @Override
    public ApiService getApiService() {
        return apiModule.provideApiService();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AppModule appModule;
        private ApiModule apiModule;

        public AppModule getAppModule() {
            return appModule;
        }

        public ApiModule getApiModule() {
            return apiModule;
        }

        public Builder appModule(AppModule mAppModule) {
            this.appModule = mAppModule;
            return this;
        }

        public Builder apiModule(ApiModule mApiModule) {
            this.apiModule = mApiModule;
            return this;
        }

        public DaggerAppComponent build() {
            return new DaggerAppComponent(this);
        }
    }
}
