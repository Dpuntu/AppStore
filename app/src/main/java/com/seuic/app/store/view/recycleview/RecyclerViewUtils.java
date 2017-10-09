package com.seuic.app.store.view.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created on 2017/9/27.
 *
 * @author dpuntu
 */

public class RecyclerViewUtils {
    /**
     * 垂直RecycleView处理，分割线
     */
    public static void createVerticalRecyclerView(Context context, RecyclerView mRecyclerView,
                                                  boolean addItemDecoration, boolean isScrollEnabled) {
        CustomLinearLayoutManager mLinearLayoutManager =
                new CustomLinearLayoutManager(context, CustomLinearLayoutManager.VERTICAL, false);
        mLinearLayoutManager.setScrollEnabled(isScrollEnabled);
        RecyclerViewDecoration mRecyclerViewDecoration =
                new RecyclerViewDecoration(context, RecyclerViewDecoration.VERTICAL_LIST);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        if (addItemDecoration) {
            mRecyclerView.addItemDecoration(mRecyclerViewDecoration);
        }
    }
}
