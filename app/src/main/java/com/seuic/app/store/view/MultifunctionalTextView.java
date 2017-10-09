package com.seuic.app.store.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.net.download.DownloadBean;
import com.seuic.app.store.net.download.DownloadManager;
import com.seuic.app.store.net.download.DownloadObserver;
import com.seuic.app.store.utils.AndroidUtils;
import com.seuic.app.store.utils.Loger;

/**
 * Created on 2017/9/21.
 *
 * @author dpuntu
 *         <p>
 *         多功能TextView
 */

public class MultifunctionalTextView extends TextView implements View.OnClickListener {
    private int textViewState;
    private Paint mPaint, mPaintLoading;
    private RectF mRectF;
    private RectF mRectFLoading;
    private float progressWidth = 0f;
    private String taskId;
    private int progress = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public MultifunctionalTextView(Context context) {
        this(context, null);
    }

    public MultifunctionalTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultifunctionalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.multifunctional, 0, 0);
        textViewState = array.getInt(R.styleable.multifunctional_textstate, TextViewState.NORMAL);
        array.recycle();
    }

    public void setTextState(int textViewState) {
        this.textViewState = textViewState;
        switch (textViewState) {
            case TextViewState.WAIT:
                setText("等待");
                break;
            case TextViewState.INSTALL_FINISH:
                setText("打开");
                break;
            case TextViewState.INSTALL_FAIL:
            case TextViewState.LOADING_FAIL:
                setText("失败");
                break;
            case TextViewState.NORMAL:
                setText("安装");
                break;
            case TextViewState.LOADING_PAUSE:
                setText("重下");
                break;
            case TextViewState.LOADING_FINISH:
                progressWidth = getWidth();
                setText("安装中");
                break;
            case TextViewState.LOADING:
                if (progress == 0) {
                    progressWidth = 0;
                } else {
                    progressWidth = getWidth() * (progress * 1f / 100f);
                }
                setText(progress + "%");
                break;
            case TextViewState.UNINSTALL:
                setText("卸载");
                break;
            case TextViewState.CANCEL:
                setText("取消");
                break;
            case TextViewState.UPDATE:
                setText("更新");
                break;
        }
    }

    public int getTextState() {
        return this.textViewState;
    }

    public void setProgress(int progress) {
        if (progress > 100 || progress < 0) {
            throw new IllegalStateException("progress is more than 100 or less than 0");
        } else {
            this.progress = progress;
        }
    }

    public int getProgress() {
        return progress;
    }

    public void bindDownloadTask(String taskId) {
        this.taskId = taskId;
        DownloadBean mDownloadBean = DownloadManager.getInstance().getDownloadBean(taskId);
        if (mDownloadBean != null) {
            updateTextState(mDownloadBean);
            bindObserver();
        }
    }

    private void updateTextState(final DownloadBean mDownloadBean) {
        switch (mDownloadBean.getLoadState()) {
            case STATE_UPDATE:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextState(TextViewState.UPDATE);
                    }
                });
                break;
            case STATE_NEWTASK:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextState(TextViewState.WAIT);
                    }
                });
                break;
            case STATE_NORMAL:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextState(TextViewState.NORMAL);
                    }
                });
                break;
            case STATE_ERROR:
                Loger.e(DownloadManager.getInstance().getErrorCode() + "_" + DownloadManager.getInstance().getErrorMsg());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextState(TextViewState.LOADING_FAIL);
                    }
                });
                break;
            case STATE_LOADING:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextState(TextViewState.LOADING);
                        if (mDownloadBean.getTotalSize() <= 0) {
                            setProgress(0);
                        } else {
                            setProgress((int) ((mDownloadBean.getLoadedLength() * 1f / mDownloadBean.getTotalSize() * 1f) * 100));
                        }
                    }
                });
                break;
            case STATE_FINISH:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextState(TextViewState.LOADING_FINISH);
                    }
                });
                break;
            case STATE_PAUSE:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextState(TextViewState.LOADING_PAUSE);
                    }
                });
                break;
            case STATE_INSTALL_SUCCESS:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextState(TextViewState.INSTALL_FINISH);
                    }
                });
                break;
            case STATE_INSTALL_FAIL:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextState(TextViewState.INSTALL_FAIL);
                    }
                });
                break;
        }
    }

    public void bindObserver() {
        DownloadManager.getInstance().registerObserver(taskId, new DownloadObserver() {
            @Override
            public void update(DownloadBean bean) {
                updateTextState(bean);
            }
        });
    }

    public String getTaskId() {
        return taskId;
    }

    private TextOnClickListener mTextOnClickListener;

    @Override
    public void onClick(View view) {
        if (mTextOnClickListener != null) {
            mTextOnClickListener.onTextClick(view, textViewState);
        }
    }

    public interface TextOnClickListener {
        void onTextClick(View view, int typeState);
    }

    public void setTextOnClickListener(TextOnClickListener mTextOnClickListener) {
        this.mTextOnClickListener = mTextOnClickListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPath();
        canvas.drawRoundRect(mRectF, AndroidUtils.dip2px(2), AndroidUtils.dip2px(2), mPaint);
        if (mRectFLoading != null) {
            canvas.drawRoundRect(mRectFLoading, AndroidUtils.dip2px(2), AndroidUtils.dip2px(2), mPaintLoading);
        }
        super.onDraw(canvas);
    }

    private void initPath() {
        mPaint = null;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mRectF = new RectF(0, 0, getWidth(), getHeight());
        if (textViewState != TextViewState.LOADING
                && textViewState != TextViewState.LOADING_FINISH) {
            mRectFLoading = null;
            switch (textViewState) {
                case TextViewState.INSTALL_FINISH:
                case TextViewState.NORMAL:
                case TextViewState.WAIT:
                    mPaint.setColor(ContextCompat.getColor(getContext(), R.color.installNormalColor));
                    setTextColor(ContextCompat.getColor(getContext(), R.color.installNormalColor));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setBackground(getResources().getDrawable(R.drawable.text_normal_click, null));
                    }
                    break;
                case TextViewState.LOADING_PAUSE:
                    mPaint.setColor(ContextCompat.getColor(getContext(), R.color.installNormalColor));
                    setTextColor(ContextCompat.getColor(getContext(), R.color.nightblack));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setBackground(getResources().getDrawable(R.drawable.text_normal_click, null));
                    }
                    break;
                case TextViewState.UNINSTALL:
                case TextViewState.LOADING_FAIL:
                case TextViewState.CANCEL:
                case TextViewState.INSTALL_FAIL:
                    mPaint.setColor(ContextCompat.getColor(getContext(), R.color.deleteColor));
                    setTextColor(ContextCompat.getColor(getContext(), R.color.deleteColor));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setBackground(getResources().getDrawable(R.drawable.text_error_click, null));
                    }
                    break;
                case TextViewState.UPDATE:
                    mPaint.setColor(ContextCompat.getColor(getContext(), R.color.updateAppColor));
                    setTextColor(ContextCompat.getColor(getContext(), R.color.updateAppColor));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setBackground(getResources().getDrawable(R.drawable.text_update_click, null));
                    }
                    break;
            }
        } else {
            if (textViewState == TextViewState.LOADING_FINISH) {
                setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            } else {
                setTextColor(ContextCompat.getColor(getContext(), R.color.nightblack));
            }
            mPaintLoading = new Paint();
            mPaintLoading.setAntiAlias(true);
            mPaintLoading.setDither(true);
            mPaintLoading.setStyle(Paint.Style.FILL);
            mPaint.setColor(ContextCompat.getColor(getContext(), R.color.installNormalColor));
            mPaintLoading.setColor(ContextCompat.getColor(getContext(), R.color.installNormalColor));
            mRectFLoading = new RectF(0, 0, progressWidth, getHeight());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setBackground(getResources().getDrawable(R.drawable.text_normal_click, null));
            }
        }
    }

    public static class TextViewState {
        /**
         * 可安装状态
         */
        public static final int NORMAL = 0;
        /**
         * 正在下载中
         */
        public static final int LOADING = 1;
        /**
         * 可暂停状态
         */
        public static final int LOADING_PAUSE = 2;
        /**
         * 可停止状态
         */
        public static final int CANCEL = 3; // 独立状态，不与其他有联系
        /**
         * 下载完成状态
         */
        public static final int LOADING_FAIL = 4;
        /**
         * 安装完成状态
         */
        public static final int LOADING_FINISH = 5;
        /**
         * 可卸载状态
         */
        public static final int UNINSTALL = 6; // 独立状态，不与其他有联系
        /**
         * 可更新状态
         */
        public static final int UPDATE = 7;
        /**
         * 安装完成成功状态
         */
        public static final int INSTALL_FINISH = 8;
        /**
         * 安装完成失败状态
         */
        public static final int INSTALL_FAIL = 9;
        /**
         * 等待状态
         */
        public static final int WAIT = 10;
    }
}
