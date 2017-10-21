package com.seuic.app.store.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.seuic.app.store.R;
import com.seuic.app.store.adapter.BaseRecycleViewAdapter;
import com.seuic.app.store.adapter.TypeAppsAdapter;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.ui.agent.ActivityService;
import com.seuic.app.store.ui.contact.AppTypeContact;
import com.seuic.app.store.ui.fragment.AssortmentFragment;
import com.seuic.app.store.ui.presenter.AppTypePresenter;
import com.seuic.app.store.utils.MuTextViewClickUtils;
import com.seuic.app.store.view.MultifunctionalTextView;
import com.seuic.app.store.view.recycleview.RecyclerViewUtils;

import butterknife.BindView;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class AppTypeActivity extends DefaultBaseActivity<ActivityService> implements AppTypeContact.View {
    private Intent mIntent;
    private AppTypePresenter mAppTypePresenter;
    @BindView(R.id.apptype_recycle)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mIntent = getIntent();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initService() {
        ActivityService mActivityService = createService(ActivityService.class);
        mActivityService.appTypeActivity(mIntent.getStringExtra(AssortmentFragment.APPS_TYPE));
    }

    @Override
    protected void eventHandler() {
        mAppTypePresenter = new AppTypePresenter(this);
        mAppTypePresenter.getTypeApps(mIntent.getStringExtra(AssortmentFragment.APPS_TYPE),
                                      mIntent.getStringExtra(AssortmentFragment.APPS_TYPE_ID));
    }

    @Override
    public void updateRecycleView(TypeAppsAdapter typeAppsAdapter) {
        RecyclerViewUtils.createVerticalRecyclerView(this, mRecyclerView, true, true);
        mRecyclerView.setAdapter(typeAppsAdapter);
        typeAppsAdapter.setTextOnClickListener(new TypeAppsAdapter.TextOnClickListener() {
            @Override
            public void onTextClick(MultifunctionalTextView multifunctionalTextView, RecommendReceive recommendReceive, int textState) {
                MuTextViewClickUtils.clickChangeState(recommendReceive, textState);
            }
        });
        typeAppsAdapter.setOnItemClickListener(new BaseRecycleViewAdapter.OnItemClickListener<RecommendReceive>() {
            @Override
            public void onItemClick(View view, RecommendReceive recommendReceive) {
                // 应用详情
                Intent intent = new Intent();
                intent.putExtra(AppDetailActivity.APP_DETAIL, recommendReceive);
                intent.setClass(AppTypeActivity.this, AppDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
