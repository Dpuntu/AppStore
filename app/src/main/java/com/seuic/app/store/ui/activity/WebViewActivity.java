package com.seuic.app.store.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.ui.agent.ActivityService;
import com.seuic.app.store.utils.FileUtils;
import com.seuic.app.store.utils.HttpHeadUtils;

import butterknife.BindView;


/**
 * Created on 2017/9/23.
 *
 * @author dpuntu
 *         <p>
 *         webview安全类
 */

public class WebViewActivity extends DefaultBaseActivity<ActivityService> {
    private Intent mIntent;
    @BindView(R.id.web_view_layout)
    FrameLayout mFrameLayout;
    public static final String WEB_URL = "web_url";
    public static final String WEB_HEAD = "web_head";
    private WebView mWebView;
    private boolean isAddHeader = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mIntent = getIntent();
        isAddHeader = mIntent.getBooleanExtra(WEB_HEAD, false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initService() {
        ActivityService mActivityService = createService(ActivityService.class);
        mActivityService.webViewActivity(mIntent.getStringExtra(WEB_URL));
    }

    @Override
    protected void eventHandler() {
        mWebView = new WebView(this);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(mLayoutParams);
        mFrameLayout.addView(mWebView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initWebView();
        loadUrl(mWebView, mIntent.getStringExtra(WEB_URL));
    }

    private void loadUrl(WebView webView, String url) {
        if (isAddHeader) {
            webView.loadUrl(url, HttpHeadUtils.getHeadMap());
        } else {
            webView.loadUrl(url);
        }
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCachePath(FileUtils.getWebCachePath(AppStoreApplication.getApp()));
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 100) {
                    normalTitle.setText(view.getTitle());
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.setWebViewClient(new WebViewClient() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    loadUrl(view, request.getUrl().toString());
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    // ... 加载网页失败
                }
            });
        } else {
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    loadUrl(view, url);
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    // ... 加载网页失败
                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        if (mFrameLayout != null) {
            mFrameLayout.removeAllViews();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
