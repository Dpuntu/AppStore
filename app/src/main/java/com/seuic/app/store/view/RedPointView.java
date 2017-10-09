package com.seuic.app.store.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.utils.AndroidUtils;

import static com.seuic.app.store.view.RedPointView.RedPointType.TYPE_GONE;
import static com.seuic.app.store.view.RedPointView.RedPointType.TYPE_TEXT;


/**
 * Created on 2017/9/19.
 *
 * @author dpuntu
 *         <p>
 *         小红点
 */

public class RedPointView extends ImageView {

    public enum RedPointType {// 小红点类型
        /**
         * 数字类型，可为空，空的情况，就是一个小点
         */
        TYPE_NUM,
        /**
         * 文字类型，不可为空，抛出异常
         */
        TYPE_TEXT,
        /**
         * 隐藏类型，将该控件隐藏
         */
        TYPE_GONE
    }

    private RedPointType type = RedPointType.TYPE_NUM;
    private String contentText = "";
    private int width, height;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mRect = new Rect();
    private RelativeLayout mLayout = null;
    private TextView mNumText;
    private Drawable mDPoint = null;

    public RedPointView(Context context) {
        super(context);
        init();
    }

    public RedPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RedPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setFilterBitmap(true);
        mLayout = new RelativeLayout(getContext());
        mNumText = new TextView(getContext());
        mNumText.setTextSize(AndroidUtils.sp2px(6));// 设置字体6sp
        mNumText.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLayout.addView(mNumText, params);
        initUI();
    }

    private void initUI() {
        mNumText.setText(contentText);
        switch (type) {
            case TYPE_NUM:
                mLayout.setBackgroundResource(R.drawable.red_point_num);
                break;
            case TYPE_TEXT:
                mLayout.setBackgroundResource(R.drawable.red_point_new);
                break;
        }
        mDPoint = new BitmapDrawable(null, convertViewToBitmap(mLayout));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 不让设置宽高, 就写死
        switch (type) {
            case TYPE_NUM:
                if (contentText == null || contentText.isEmpty()) {
                    width = getResources().getDimensionPixelOffset(R.dimen.dimen_9dp);
                    height = getResources().getDimensionPixelOffset(R.dimen.dimen_9dp);
                } else {
                    width = getResources().getDimensionPixelOffset(R.dimen.dimen_14dp);
                    height = getResources().getDimensionPixelOffset(R.dimen.dimen_14dp);
                }
                break;
            case TYPE_TEXT:
                width = getResources().getDimensionPixelOffset(R.dimen.dimen_25dp);
                height = getResources().getDimensionPixelOffset(R.dimen.dimen_14dp);
                break;
        }
        mRect.set(0, 0, width, height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRedPoint(canvas);
    }

    private void drawRedPoint(Canvas canvas) {
        if (mDPoint == null) {
            return;
        }
        canvas.save();
        mDPoint.setBounds(mRect);
        mDPoint.draw(canvas);
        canvas.restore();
    }

    /**
     * 对外接口，设置类型和内容
     */
    public void setTypeText(RedPointType type, String contentText) {
        if (type == TYPE_GONE) {
            setVisibility(View.GONE);
            return;
        } else {
            setVisibility(View.VISIBLE);
        }

        if (type == TYPE_TEXT && (contentText == null || contentText.isEmpty())) {
            throw new NullPointerException("contentText is null");
        }

        this.type = type;
        this.contentText = contentText;
        init();
        invalidate();
    }

    private Bitmap convertViewToBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
