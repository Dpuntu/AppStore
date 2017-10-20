package com.seuic.app.store.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.utils.FileUtils;

import java.io.InputStream;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */
@GlideModule
public final class AppStoreGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setMemoryCache(new LruResourceCache(GlideParams.MEMORY_CACHE_SIZE));
        if (FileUtils.isExternalStorage()) {
            builder.setDiskCache(
                    new DiskLruCacheFactory(
                            FileUtils.getCachePath(),
                            GlideParams.DISK_CACHE_SIZE));
        } else {
            builder.setDiskCache(
                    new InternalCacheDiskCacheFactory(
                            AppStoreApplication.getApp(),
                            null,
                            GlideParams.DISK_CACHE_SIZE));
        }
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
