package com.fmx.dpuntu.ui.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fmx.dpuntu.api.AppListResponse;
import com.fmx.dpuntu.download.DownLoadInfo;
import com.fmx.dpuntu.ui.BaseActivity;
import com.fmx.dpuntu.mvp.R;
import com.fmx.dpuntu.utils.LAYOUT;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/7/14.
 *
 * @author dpuntu
 */
@LAYOUT(contentView = R.layout.activity_dialog)
public class DialogActivity extends BaseActivity implements DialogContract.View {

    @BindView(R.id.dialog_title)
    TextView dialogTitle;
    @BindView(R.id.dialog_summary)
    TextView dialogSummary;
    @BindView(R.id.dialog_progressbar)
    ProgressBar dialogProgressBar;
    @BindView(R.id.dialog_progress)
    TextView dialogProgress;
    @BindView(R.id.dialog_pbar)
    LinearLayout dialogPbar;
    @BindView(R.id.dialog_button)
    TextView dialogButton;
    @BindView(R.id.dialog_view)
    View dialogView;

    private DialogPresenter mDialogPresenter;
    private AppListResponse.DownloadAppInfo info;

    public AppListResponse.DownloadAppInfo getInfo() {
        return info;
    }

    public void setInfo(AppListResponse.DownloadAppInfo info) {
        this.info = info;
    }

    private void showDialog(boolean isDownload, String title, String summary, DownLoadInfo info, View.OnClickListener listener) {
        dialogTitle.setText(title);
        if (isDownload) {
            dialogPbar.setVisibility(View.VISIBLE);
            dialogSummary.setVisibility(View.GONE);
            dialogProgressBar.setMax((int) info.getAppSize());
            dialogProgressBar.setProgress((int) info.getDownloadSize());
            dialogProgress.setText(info.getDownloadSize() * 100 / info.getAppSize() + "%");
        } else {
            dialogPbar.setVisibility(View.GONE);
            dialogSummary.setVisibility(View.VISIBLE);
            dialogSummary.setText(summary);
        }
        dialogButton.setVisibility(View.VISIBLE);
        dialogView.setVisibility(View.VISIBLE);
        dialogButton.setText("确定");
        dialogButton.setOnClickListener(listener);
    }


    public void showDefaultDialog(String title, String summary, View.OnClickListener listener) {
        showDialog(false, title, summary, null, listener);
    }

    public void showDownloadtDialog(String title, DownLoadInfo info, View.OnClickListener listener) {
        showDialog(true, title, null, info, listener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(false);
        mDialogPresenter = new DialogPresenter(DialogActivity.this, mHandler);
        if (getIntent() != null) {
            info = (AppListResponse.DownloadAppInfo) getIntent().getSerializableExtra("appinfo");
            setInfo(info);
            mDialogPresenter.onStart();
        } else {
            mDialogPresenter.onFinish();
        }
    }

    @Override
    protected int initLayout(boolean isLayoutFail) {
        if (isLayoutFail) {
            return R.layout.activity_dialog;
        }
        return -1;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DownLoadInfo info = (DownLoadInfo) msg.obj;
            switch (msg.what) {
                case DialogPresenter.DOWN_PROGRESS:
                    mDialogPresenter.onDownLoadProgress(info);
                    dialogButton.setVisibility(View.GONE);
                    dialogView.setVisibility(View.GONE);
                    break;
                case DialogPresenter.DOWN_START:
                    mDialogPresenter.onDownLoadStart(info);
                    break;
                case DialogPresenter.DOWN_PAUSE:
                    mDialogPresenter.onDownLoadPause(info);
                    break;
                case DialogPresenter.DOWN_STOP:
                    mDialogPresenter.onDownLoadStop(info);
                    break;
                case DialogPresenter.DOWN_ERROR:
                    mDialogPresenter.onDownLoadFail(info);
                    break;
                case DialogPresenter.DOWN_FINISH:
                    mDialogPresenter.onDownLoadFinish(info);
                    break;
            }
        }
    };

    @Override
    public void startDownLoad() {
        mDialogPresenter.startDownLoad();
    }
}
