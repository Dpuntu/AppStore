package com.seuic.app.store.ui.activity;

import android.support.v7.widget.RecyclerView;

import com.seuic.app.store.R;
import com.seuic.app.store.adapter.DownLoadAdapter;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.response.RecommendReceive;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.ui.agent.ActivityService;
import com.seuic.app.store.ui.contact.DownLoadContact;
import com.seuic.app.store.ui.presenter.DownLoadPresenter;
import com.seuic.app.store.utils.MuTextViewClickUtils;
import com.seuic.app.store.view.MultifunctionalTextView;
import com.seuic.app.store.view.recycleview.RecyclerViewUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created on 2017/9/16.
 *
 * @author dpuntu
 */

public class DownLoadActivity extends DefaultBaseActivity implements DownLoadContact.View {
    private DownLoadPresenter mDownLoadPresenter;
    @BindView(R.id.download_recycle)
    RecyclerView mRecyclerView;
    private DownLoadAdapter mDownLoadAdapter;
    private boolean isRefresh = false;

    @Override
    protected void initService() {
        createService(ActivityService.class).downLoadActivity();
    }

    @Override
    protected void eventHandler() {
        mDownLoadPresenter = new DownLoadPresenter(this);
        mDownLoadPresenter.queryDownLoadData(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void updateRecycleView(List<RecycleObject> recycleObjects) {
        mDownLoadAdapter = new DownLoadAdapter(recycleObjects);
        RecyclerViewUtils.createVerticalRecyclerView(this, mRecyclerView, !isRefresh, true);
        isRefresh = false;
        mRecyclerView.setAdapter(mDownLoadAdapter);

        mDownLoadAdapter.setOnDeleteTextClickListener(new DownLoadAdapter.OnDeleteTextClickListener() {
            @Override
            public void onDeleteTextClick(MultifunctionalTextView view, String taskId) {
                DownloadManager.getInstance().removeLoadingTask(taskId);
                mDownLoadPresenter.queryDownLoadData(true);
            }
        });

        mDownLoadAdapter.setOnPauseTextClickListener(new DownLoadAdapter.OnPauseTextClickListener() {
            @Override
            public void onPauseTextClick(MultifunctionalTextView view, RecommendReceive recommendReceive, int typeState) {
                MuTextViewClickUtils.clickChangeState(recommendReceive, typeState);
            }
        });
    }

    @Override
    public void refreshRecycleView(List<RecycleObject> recycleObjects) {
        if (mDownLoadAdapter != null) {
            mDownLoadAdapter.setOnPauseTextClickListener(null);
            mDownLoadAdapter.setOnDeleteTextClickListener(null);
            mDownLoadAdapter = null;
            mRecyclerView.setAdapter(null);
            isRefresh = true;
        }
        updateRecycleView(recycleObjects);
    }
}
