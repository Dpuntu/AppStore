package com.seuic.app.store.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.seuic.app.store.R;
import com.seuic.app.store.adapter.UpdateAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.ui.contact.UpdateContact;
import com.seuic.app.store.ui.presenter.UpdatePresenter;
import com.seuic.app.store.view.recycleview.RecyclerViewUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 */

public class UpdateActivity extends DefaultBaseActivity implements UpdateContact.View {
    @BindView(R.id.update_recycle)
    RecyclerView mRecyclerView;
    private List<RecommendReceive> recommendReceives;
    private UpdatePresenter mUpdatePresenter;

    @Override
    protected View.OnClickListener onRightClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdatePresenter.updateAllApps(recommendReceives);
            }
        };
    }

    @Override
    protected String setNormalRightTitle() {
        return getString(R.string.act_update_all);
    }

    @Override
    protected String setNormalLeftTitle() {
        return getString(R.string.act_back);
    }

    @Override
    protected String setNormalTitle() {
        return getString(R.string.act_update_title);
    }

    @Override
    protected void eventHandler() {
        mUpdatePresenter = new UpdatePresenter(this);
        mUpdatePresenter.checkUpdateApps();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_update;
    }

    @Override
    public void updateRecycleView(List<RecycleObject> recycleObjects) {
        UpdateAdapter updateAdapter = new UpdateAdapter(recycleObjects);
        RecyclerViewUtils.createVerticalRecyclerView(this, mRecyclerView, true, true);
        mRecyclerView.setAdapter(updateAdapter);
    }

    @Override
    public void setRecommendReceives(List<RecommendReceive> recommendReceives) {
        this.recommendReceives = recommendReceives;
    }
}
