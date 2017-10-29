package com.seuic.app.store.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.utils.AndroidUtils;


/**
 * Created on 2017/8/17.
 *
 * @author dpuntu
 *         <p>
 *         ViewPager导航指示器
 */

public class ViewPagerIndicator extends LinearLayout {
    private static final int STYLE_LINE = 0;
    private static final int STYLE_TRIANGLE = 1;

    private int textSize;
    private int textColor;
    private int count;

    private float mHeight;// 指示符高度
    private float mWidth; //宽度
    private float mTranslationX; //偏移量
    private int mStyle = STYLE_LINE;
    private Paint mPaint;
    private RectF mRectF;
    private int position = 0;

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.viewpagerselector, 0, 0);
        textColor = array.getColor(R.styleable.viewpagerselector_textcolor, 0X0000FF);
        textSize = array.getDimensionPixelSize(R.styleable.viewpagerselector_textszie, 16);
        count = array.getInt(R.styleable.viewpagerselector_count, 3);
        mStyle = array.getInt(R.styleable.viewpagerselector_style, STYLE_LINE);
        array.recycle();
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float singleWidth = w / count / 1f;
        switch (mStyle) {
            case STYLE_LINE:
                mWidth = singleWidth / 2f;
                mHeight = AndroidUtils.dip2px(2); // 2dip
                mTranslationX = 0;
                mRectF = new RectF((singleWidth - mWidth) / 2f, 0, (singleWidth - mWidth) / 2f + mWidth, mHeight);
                break;
            case STYLE_TRIANGLE:
                mWidth = singleWidth / 8f;
                mHeight = h / 5f;
                mTranslationX = 0;
                break;
            default:
                break;
        }
        setItemClickEvent();
        notifyHeadSelector(position);
    }


    private ViewPager mViewPager;

    /**
     * 设置点击事件
     */
    private void setItemClickEvent() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        switch (mStyle) {
            case STYLE_LINE:
                canvas.translate(mTranslationX, getHeight() - mHeight);
                canvas.drawRect(mRectF, mPaint);
                break;
            case STYLE_TRIANGLE:
                canvas.translate(mTranslationX, 0);
                Path mPath = new Path();
                mPath.moveTo((getWidth() / count - mWidth) / 2f, getHeight() + 1);
                mPath.lineTo(getWidth() / count / 2f, getHeight() - mHeight);
                mPath.lineTo((getWidth() / count + mWidth) / 2f, getHeight() + 1);
                mPath.close();
                canvas.drawPath(mPath, mPaint);
                break;
            default:
                break;
        }
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.homeTitleColor));
    }

    public void setViewPager(ViewPager viewPager, int position) {
        this.mViewPager = viewPager;
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onScroll(position, positionOffset);
                mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                notifyHeadSelector(position);
                mOnPageChangeListener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
        });

        this.position = position;
        this.mViewPager.setOffscreenPageLimit(getChildCount());
        this.mViewPager.setCurrentItem(position);
        notifyHeadSelector(position);
    }

    private void onScroll(int position, float offset) {
        // 不断改变偏移量，invalidate
        mTranslationX = getWidth() / count * (position + offset);
        int tabWidth = (int) (getWidth() / count / 8f);
        // 容器滚动，当移动到倒数第二个的时候，开始滚动
        if (offset > 0 && position >= (count - 2)
                && getChildCount() > count
                && position < (getChildCount() - 2)) {
            if (count != 1) {
                int xValue = (position - (count - 2)) * tabWidth
                        + (int) (tabWidth * offset);
                this.scrollTo(xValue, 0);
            } else {
                // 为count为1时特殊处理
                this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        /**触发 dispatchDraw()*/
        invalidate();
    }

    private void notifyHeadSelector(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(AndroidUtils.px2sp(textSize));
                if (i == position) {
                    ((TextView) view).setTextColor(textColor);
                } else {
                    ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), R.color.titleColor));
                }
            }
        }
    }

    private OnPageChangeListener mOnPageChangeListener;

    public void addOnPageChangeListener(OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }
}
