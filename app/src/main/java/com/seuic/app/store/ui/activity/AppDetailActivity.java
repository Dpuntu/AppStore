package com.seuic.app.store.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.adapter.BaseRecycleViewAdapter;
import com.seuic.app.store.adapter.ScreenShotAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.AppDetailReceive;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.glide.GlideAppManager;
import com.seuic.app.store.ui.agent.ActivityService;
import com.seuic.app.store.ui.contact.AppDetailContact;
import com.seuic.app.store.ui.dialog.DialogManager;
import com.seuic.app.store.ui.presenter.AppDetailPresenter;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.utils.MuTextViewClickUtils;
import com.seuic.app.store.view.MultifunctionalTextView;
import com.seuic.app.store.view.recycleview.CustomLinearLayoutManager;

import java.util.List;

import butterknife.BindView;

/**
 * Created on 2017/9/22.
 *
 * @author dpuntu
 */

public class AppDetailActivity extends DefaultBaseActivity<ActivityService> implements AppDetailContact.View {
    private AppDetailPresenter mAppDetailPresenter;
    public static final String APP_DETAIL = "app_detail";
    private RecommendReceive mRecommendReceive;
    @BindView(R.id.app_detail_image)
    ImageView mImageView;
    @BindView(R.id.app_detail_install)
    MultifunctionalTextView mMultifunctionalTextView;
    @BindView(R.id.app_detail_name)
    TextView mDetailName;
    @BindView(R.id.app_detail_type)
    TextView mDetailType;
    @BindView(R.id.app_detail_screen_gallery)
    RecyclerView mGalleryRecycle;
    @BindView(R.id.app_detail_desc)
    TextView mAppDesc;
    @BindView(R.id.app_detail_version_desc)
    TextView mAppVersionDesc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mRecommendReceive = (RecommendReceive) getIntent().getSerializableExtra(APP_DETAIL);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initService() {
        ActivityService mActivityService = createService(ActivityService.class);
        mActivityService.appDetailActivity(getNormalTitle());
    }

    protected String getNormalTitle() {
        if (mRecommendReceive != null) {
            return mRecommendReceive.getAppName();
        } else {
            return getString(R.string.act_app_detail);
        }
    }

    @Override
    protected void eventHandler() {
        mAppDetailPresenter = new AppDetailPresenter(this);
        mAppDetailPresenter.getAppDetail(mRecommendReceive.getPackageName());
    }

    @Override
    public void updateView(AppDetailReceive appDetailReceive) {
        GlideAppManager.loadImage(appDetailReceive.getAppIconName(),
                                  mImageView,
                                  R.mipmap.default_icon,
                                  AppStoreUtils.AppStoreImageType.ICON);
        mMultifunctionalTextView.setTextState(MultifunctionalTextView.TextViewState.NORMAL);
        mDetailName.setText(appDetailReceive.getAppName());
        mDetailType.setText(appDetailReceive.getAppTypes());
        mAppDesc.setText(appDetailReceive.getAppDesc());
        mAppVersionDesc.setText(appDetailReceive.getAppVersionIdDesc());
        mMultifunctionalTextView.bindDownloadTask(mRecommendReceive.getAppVersionId());
        mMultifunctionalTextView.setTextOnClickListener(new MultifunctionalTextView.TextOnClickListener() {
            @Override
            public void onTextClick(View view, int typeState) {
                MuTextViewClickUtils.clickChangeState(mRecommendReceive, typeState);
            }
        });
    }

    @Override
    public void updateRecycleView(ScreenShotAdapter screenShotAdapter) {
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(this, CustomLinearLayoutManager.HORIZONTAL, false);
        mGalleryRecycle.setLayoutManager(layoutManager);
        mGalleryRecycle.setAdapter(screenShotAdapter);
        screenShotAdapter.setOnItemClickAllListener(new BaseRecycleViewAdapter.OnItemClickAllListener() {
            @Override
            public void OnItemClickAll(View view, int position, List<RecycleObject> mRecycleObjectList) {
                DialogManager.getInstance().showImageDetailDialog(position, mRecycleObjectList);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogManager.getInstance().dismissImageDetailDialog();
    }
}
