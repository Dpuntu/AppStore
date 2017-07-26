package com.fmx.dpuntu.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fmx.dpuntu.mvp.R;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */

public class StatusBarCompat {
    private static final int INVALID_VAL = -1;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static View compat(Activity activity, int statusColor) {
        int color = ContextCompat.getColor(activity, R.color.colorPrimaryDark);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            activity.getWindow().setStatusBarColor(color);
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            View statusBarView = contentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
                statusBarView.setBackgroundColor(color);
                return statusBarView;
            }
            statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                   getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
            return statusBarView;
        }
        return null;

    }

    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     *         context
     *
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        Window window = activity.getWindow();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                                          | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                window.setStatusBarColor(activity.getResources().getColor(colorResId));
                window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
                View childAt = contentView.getChildAt(0);
                if (childAt != null) {
                    childAt.setFitsSystemWindows(true);
                }
                View view = new View(activity);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity)));
                view.setBackgroundColor(activity.getResources().getColor(colorResId));
                contentView.addView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
