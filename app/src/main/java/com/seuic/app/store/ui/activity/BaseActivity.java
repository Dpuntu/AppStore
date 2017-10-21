package com.seuic.app.store.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.ui.agent.ActivityParams;
import com.seuic.app.store.ui.agent.ParamsBody;
import com.seuic.app.store.ui.agent.ParamsType;
import com.seuic.app.store.view.RedPointView;
import com.seuic.app.store.view.SearchBar;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */

public abstract class BaseActivity<T> extends AbsStatusBarActivity {
    private Unbinder mUnBinder;
    protected RelativeLayout normalLeft;
    protected TextView normalTitle, normalLeftTitle, normalRightTitle;
    protected ImageView normalImage;
    protected FrameLayout mRightLayout;
    protected ImageView homeDownImage;
    protected SearchBar mSearchBar;
    protected RedPointView mRedPointView;
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    private int layoutId = -1;
    private boolean isHome = true;
    private int headBackgroundColor = R.color.appStoreColor;
    protected boolean isSearchBarFocusable = false, isRightLayoutShow = false;
    protected String normalTitleText, leftTitleText, rightTitleText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abs);
        initService();
        FrameLayout headLayout = (FrameLayout) findViewById(R.id.abs_head);
        FrameLayout bodyLayout = (FrameLayout) findViewById(R.id.abs_body);
        View headView = LayoutInflater.from(BaseActivity.this).inflate(R.layout.actionbar_appstore, headLayout, false);
        headLayout.addView(headView);
        View bodyView = LayoutInflater.from(BaseActivity.this).inflate(layoutId, bodyLayout, false);
        bodyLayout.addView(bodyView);
        mUnBinder = ButterKnife.bind(this, bodyView);
        initHeadView(headView);
    }

    private void initHeadView(View headView) {
        headView.setBackgroundResource(headBackgroundColor);
        View homeLayout = headView.findViewById(R.id.home_appstore);
        View normalLayout = headView.findViewById(R.id.normal_appstore);
        // 主页
        homeDownImage = (ImageView) homeLayout.findViewById(R.id.actionbar_home_down_image);
        mSearchBar = (SearchBar) homeLayout.findViewById(R.id.actionbar_home_search);
        mRedPointView = (RedPointView) homeLayout.findViewById(R.id.actionbar_home_down_num);
        mRightLayout = (FrameLayout) homeLayout.findViewById(R.id.actionbar_home_down);
        // 其他
        normalTitle = (TextView) normalLayout.findViewById(R.id.actionbar_title);
        normalLeftTitle = (TextView) normalLayout.findViewById(R.id.actionbar_left_title);
        normalRightTitle = (TextView) normalLayout.findViewById(R.id.actionbar_right_title);
        normalImage = (ImageView) normalLayout.findViewById(R.id.actionbar_left_image);
        normalLeft = (RelativeLayout) normalLayout.findViewById(R.id.actionbar_left);
        initTitle();
        homeLayout.setVisibility(isHome ? View.VISIBLE : View.GONE);
        normalLayout.setVisibility(isHome ? View.GONE : View.VISIBLE);
        eventHandler();
    }

    /**
     * 创建代理实例，并获得数据
     */
    @SuppressWarnings("unchecked")
    protected T createService(Class<T> cls) {
        T se;
        if (cls == null) {
            throw new NullPointerException("the Class is Null");
        }
        se = (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[] {cls}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ActivityParams mActivityParams = method.getAnnotation(ActivityParams.class);
                layoutId = mActivityParams.layoutId();
                isHome = mActivityParams.isHome();
                headBackgroundColor = mActivityParams.headBgColor();
                isSearchBarFocusable = mActivityParams.isSearchBarFocusable();
                isRightLayoutShow = mActivityParams.isRightLayoutShow();
                normalTitleText = checkTitleText(mActivityParams.normalTitle(), mActivityParams.normalTitleId());
                leftTitleText = checkTitleText(mActivityParams.leftTitle(), mActivityParams.leftTitleId());
                rightTitleText = checkTitleText(mActivityParams.rightTitle(), mActivityParams.rightTitleId());
                BaseActivity.this.invoke(method, args);
                return null;
            }
        });
        return se;
    }

    /**
     * 检查参数配置
     */
    private void invoke(Method method, Object[] args) {
        Annotation[][] mAnnotations = method.getParameterAnnotations();
        if (mAnnotations.length > 0) {
            for (int i = 0; i < mAnnotations.length; i++) {
                for (int j = 0; j < mAnnotations[i].length; j++) {
                    ParamsBody mParamsBody = (ParamsBody) mAnnotations[i][j];
                    switch (mParamsBody.value()) {
                        case ParamsType.NORMAL_TITLE:
                            normalTitleText = args[i].toString();
                            break;
                        case ParamsType.LEFT_TITLE:
                            leftTitleText = args[i].toString();
                            break;
                        case ParamsType.RIGHT_TITLE:
                            rightTitleText = args[i].toString();
                            break;
                        case ParamsType.RIGHT_LAYOUT_SHOW:
                            isRightLayoutShow = (boolean) args[i];
                            break;
                        case ParamsType.SEARCH_BAR_FOCUS:
                            isSearchBarFocusable = (boolean) args[i];
                            break;
                    }
                }
            }
        }
    }

    /**
     * 检查配置的String和ID，如果两个都存在则以String为准
     */
    private String checkTitleText(String text, int resId) {
        if (text == null || text.isEmpty()) {
            if (resId == -1) {
                return null;
            } else {
                return getString(resId);
            }
        } else {
            return text;
        }
    }

    protected abstract void initService();

    protected abstract void initTitle();

    protected abstract void eventHandler();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }
}
