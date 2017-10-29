package com.seuic.app.store.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleSummaryBean;
import com.seuic.app.store.greendao.CheckUpdateAppsTable;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.listener.UpdateCountListener;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.ui.activity.InstallActivity;
import com.seuic.app.store.ui.activity.UpdateActivity;
import com.seuic.app.store.ui.contact.ManagerContent;
import com.seuic.app.store.ui.presenter.ManagerPresenter;
import com.seuic.app.store.utils.AndroidUtils;
import com.seuic.app.store.utils.SpUtils;
import com.seuic.app.store.view.RedPointView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 */

public class ManagerFragment extends Fragment implements ManagerContent.View, UpdateCountListener {
    public static final int MANAGER_APPS_UPDATE = 0;
    public static final int MANAGER_INSTALL = 1;
    public static final int MANAGER_AUTO_UPDATE = 2;
    public static final int MANAGER_UPDATE_SELF = 3;

    private Unbinder mUnbinder;
    @BindView(R.id.manager_parent)
    LinearLayout mLinearLayout;
    private ManagerPresenter mManagerPresenter;
    private RedPointView appUpdate, updateSelf;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private long updateClickTime = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manager, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        mManagerPresenter = new ManagerPresenter(this);
        mManagerPresenter.checkUpdate(false);
        DownloadManager.getInstance().setUpdateCountListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void updateView(List<RecycleObject> mRecycleObjectList) {
        for (RecycleObject recycleObject : mRecycleObjectList) {
            mLinearLayout.addView(createChildView(recycleObject.getType(), (RecycleSummaryBean) recycleObject.getObject()));
            View lineView = new View(getActivity());
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(AndroidUtils.dip2px(1), AndroidUtils.dip2px(1));
            lineView.setLayoutParams(mLayoutParams);
            lineView.setBackgroundResource(R.color.summaryColor);
            mLinearLayout.addView(lineView);
        }
    }

    @Override
    public void refreshView(String updateCount, String updateSelf) {
        refreshView(this.appUpdate, updateCount, RedPointView.RedPointType.TYPE_NUM);
        refreshView(this.updateSelf, updateSelf, RedPointView.RedPointType.TYPE_TEXT);
    }

    private View createChildView(final int bindType, RecycleSummaryBean mRecycleSummaryBean) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_manager_item, mLinearLayout, false);
        final TextView mTitle = (TextView) view.findViewById(R.id.manager_title);
        final TextView mSummary = (TextView) view.findViewById(R.id.manager_summary);
        final FrameLayout mFrameLayout = (FrameLayout) view.findViewById(R.id.manager_right);
        final Switch mSwitch = (Switch) view.findViewById(R.id.manager_right_switch);
        final ImageView mImageView = (ImageView) view.findViewById(R.id.manager_right_image);
        final RedPointView mRedPointView = (RedPointView) view.findViewById(R.id.manager_right_redpoint);

        mTitle.setText(mRecycleSummaryBean.getTitle());

        if (mRecycleSummaryBean.getSummary() == null || mRecycleSummaryBean.getSummary().isEmpty()) {
            mSummary.setVisibility(View.GONE);
        } else {
            mSummary.setText(mRecycleSummaryBean.getSummary());
        }

        switch (bindType) {
            // 应用更新
            case MANAGER_APPS_UPDATE:
                mSwitch.setVisibility(View.GONE);
                refreshView(mRedPointView, mRecycleSummaryBean.getUpdateText(), RedPointView.RedPointType.TYPE_NUM);
                appUpdate = mRedPointView;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), UpdateActivity.class));
                    }
                });
                break;
            // 安装管理
            case MANAGER_INSTALL:
                mSwitch.setVisibility(View.GONE);
                mRedPointView.setTypeText(RedPointView.RedPointType.TYPE_GONE,
                                          mRecycleSummaryBean.getUpdateText());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), InstallActivity.class));
                    }
                });
                break;
            // 自动更新
            case MANAGER_AUTO_UPDATE:
                mSwitch.setChecked(
                        SpUtils.getInstance().getBoolean(SpUtils.SP_SWITCH_AUTO, false));
                mImageView.setVisibility(View.GONE);
                mRedPointView.setTypeText(RedPointView.RedPointType.TYPE_GONE,
                                          mRecycleSummaryBean.getUpdateText());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAuto = SpUtils.getInstance().getBoolean(SpUtils.SP_SWITCH_AUTO, false);
                        SpUtils.getInstance()
                                .putBoolean(SpUtils.SP_SWITCH_AUTO, !isAuto);
                        mSwitch.setChecked(!isAuto);
                    }
                });
                break;
            // 检测新版本
            case MANAGER_UPDATE_SELF:
                mFrameLayout.setVisibility(View.GONE);
                refreshView(mRedPointView, mRecycleSummaryBean.getUpdateText(), RedPointView.RedPointType.TYPE_TEXT);
                updateSelf = mRedPointView;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AndroidUtils.isCanClick(updateClickTime)) {
                            updateClickTime = System.currentTimeMillis();
                            mManagerPresenter.checkUpdate(true);
                        }
                    }
                });
                break;
            default:
                break;
        }
        return view;
    }

    private void refreshView(RedPointView redPointView, String updateText, RedPointView.RedPointType type) {
        if (updateText == null || updateText.isEmpty()) {
            redPointView.setTypeText(RedPointView.RedPointType.TYPE_GONE, updateText);
        } else {
            redPointView.setTypeText(type, updateText);
        }
    }

    @Override
    public void onUpdateCountChange() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                List<CheckUpdateAppsTable> checkUpdateAppsTables = GreenDaoManager.getInstance().queryCheckUpdateApps();
                if (checkUpdateAppsTables == null || checkUpdateAppsTables.size() <= 0) {
                    refreshView(appUpdate, null, RedPointView.RedPointType.TYPE_NUM);
                } else {
                    refreshView(appUpdate, checkUpdateAppsTables.size() + "", RedPointView.RedPointType.TYPE_NUM);
                }
            }
        });
    }
}
