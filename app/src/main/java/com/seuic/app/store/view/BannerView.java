package com.seuic.app.store.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.bean.response.AdvertisementsReceive;
import com.seuic.app.store.glide.GlideAppManager;
import com.seuic.app.store.utils.AppStoreUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created on 2017/9/18.
 *
 * @author dpuntu
 *         <p>
 *         图片轮播图
 *         <p>
 *         参考:https://github.com/byblinkdagger/BannerView
 */

public class BannerView extends FrameLayout {
    private static final int MSG_LOOP = 1000;
    private static long LOOP_TIME = 5000;
    private LinearLayout mLinearPosition = null;
    private ViewPager mViewPager = null;
    private BannerHandler mBannerHandler = null;
    private String imageType = AppStoreUtils.AppStoreImageType.AD;
    private List<ImageView> viewList;
    private List<AdvertisementsReceive.AdReceiveDetails> mDetails;
    private int viewSize;

    private static class BannerHandler extends Handler {
        private WeakReference<BannerView> weakReference = null;

        public BannerHandler(BannerView bannerView) {
            super(Looper.getMainLooper());
            this.weakReference = new WeakReference<>(bannerView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.weakReference == null) {
                return;
            }
            BannerView bannerView = this.weakReference.get();
            if (bannerView == null || bannerView.mViewPager == null
                    || bannerView.mViewPager.getAdapter() == null
                    || bannerView.mViewPager.getAdapter().getCount() <= 0) {
                return;
            }
            int curPos = bannerView.mViewPager.getCurrentItem();
            curPos = (curPos + 1) % bannerView.mViewPager.getAdapter().getCount();
            bannerView.mViewPager.setCurrentItem(curPos);
            if (hasMessages(MSG_LOOP)) {
                removeMessages(MSG_LOOP);
            }
            sendEmptyMessageDelayed(MSG_LOOP, LOOP_TIME);
        }
    }

    public BannerView(Context context) {
        super(context);
        init();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 是否开启自动轮播
     */
    public void isAutoLoop(boolean flag) {
        if (flag) {
            if (mBannerHandler == null) {
                mBannerHandler = new BannerHandler(this);
            }
            mBannerHandler.sendEmptyMessageDelayed(MSG_LOOP, LOOP_TIME);
        } else {
            if (mBannerHandler != null) {
                if (mBannerHandler.hasMessages(MSG_LOOP)) {
                    mBannerHandler.removeMessages(MSG_LOOP);
                    mBannerHandler = null;
                }
            }
        }
    }

    private void init() {
        initViewPager();
        initLinearPosition();
        this.addView(mViewPager);
        this.addView(mLinearPosition);
    }

    private void initViewPager() {
        mViewPager = new ViewPager(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        mViewPager.setLayoutParams(layoutParams);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateLinearPosition();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mBannerHandler != null) {
                            if (mBannerHandler.hasMessages(MSG_LOOP)) {
                                mBannerHandler.removeMessages(MSG_LOOP);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mBannerHandler != null) {
                            mBannerHandler.sendEmptyMessageDelayed(MSG_LOOP, LOOP_TIME);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initLinearPosition() {
        mLinearPosition = new LinearLayout(getContext());
        mLinearPosition.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dimen_9dp);
        mLinearPosition.setPadding(getResources().getDimensionPixelSize(R.dimen.dimen_9dp), 0, 0, 0);
        mLinearPosition.setLayoutParams(layoutParams);
    }

    public void setAdapter(PagerAdapter adapter) {
        mViewPager.setAdapter(adapter);
        adapter.registerDataSetObserver(mDataObserver);
        updateLinearPosition();
    }

    private DataSetObserver mDataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updateLinearPosition();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    private void updateLinearPosition() {
        if (viewSize < 1) {
            return;
        } else if (viewSize == 1) {
            mLinearPosition.removeAllViews();
            return;
        }
        if (viewList == null || viewList.size() <= 0) {
            return;
        }
        int curPos = mViewPager.getCurrentItem();
        switch (imageType) {
            case AppStoreUtils.AppStoreImageType.AD:
                if (mLinearPosition.getChildCount() != viewSize) {
                    int diffCnt = mLinearPosition.getChildCount() - viewSize;
                    boolean needAdd = diffCnt < 0;
                    diffCnt = Math.abs(diffCnt);
                    for (int i = 0; i < diffCnt; i++) {
                        if (needAdd) {
                            ImageView img = new ImageView(getContext());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_9dp);
                            img.setLayoutParams(layoutParams);
                            img.setBackgroundResource(R.mipmap.banner_point);
                            mLinearPosition.addView(img);
                        } else {
                            mLinearPosition.removeViewAt(0);
                        }
                    }
                }

                for (int i = 0; i < mLinearPosition.getChildCount(); i++) {
                    if (i == (curPos % viewSize)) {
                        mLinearPosition.getChildAt(i).setBackgroundResource(R.mipmap.banner_point_select);
                    } else {
                        mLinearPosition.getChildAt(i).setBackgroundResource(R.mipmap.banner_point);
                    }
                }
                break;
            case AppStoreUtils.AppStoreImageType.SCREEN:
                if (mLinearPosition.getChildCount() <= 0) {
                    TextView text = new TextView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    text.setLayoutParams(layoutParams);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        text.setTextColor(getContext().getResources().getColor(R.color.white, null));
                    } else {
                        text.setTextColor(getContext().getResources().getColor(R.color.white));
                    }
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, getContext().getResources().getDimension(R.dimen.dialog_images_position));
                    text.setText((curPos + 1) + "/" + viewSize);
                    mLinearPosition.addView(text);
                } else {
                    ((TextView) mLinearPosition.getChildAt(0)).setText((curPos + 1) + "/" + viewSize);
                }
                break;
        }
    }

    /**
     * 轮播图网络地址
     */
    public void setViewUrlList(List<AdvertisementsReceive.AdReceiveDetails> mDetails, int count, int showCount) {
        setViewUrlList(mDetails, count, showCount, AppStoreUtils.AppStoreImageType.AD);
    }

    /**
     * 轮播图网络地址
     */
    public void setViewUrlList(List<AdvertisementsReceive.AdReceiveDetails> mDetails, int count, int showCount, String imageType) {
        if (mDetails == null || mDetails.size() <= 0 || count <= 0) {
            throw new NullPointerException("mDetails is null");
        }
        viewSize = count;
        this.mDetails = mDetails;
        this.imageType = imageType;
        Collections.sort(this.mDetails, new DetailsComparator());
        if (viewList != null) {
            viewList.clear();
            viewList = null;
        }
        viewList = new ArrayList<>();
        for (int i = 0; i < mDetails.size(); i++) {
            ImageView mImageView = new ImageView(AppStoreApplication.getApp());
            viewList.add(mImageView);
        }
        if (this.mViewPager.getAdapter() != null) {
            this.mViewPager.getAdapter().notifyDataSetChanged();
        }
        this.mViewPager.setOffscreenPageLimit(viewList.size());
        this.mViewPager.setCurrentItem(showCount);
        setAdapter(new BannerAdapter());
    }

    /**
     * 排序
     */
    class DetailsComparator implements Comparator<AdvertisementsReceive.AdReceiveDetails> {
        @Override
        public int compare(AdvertisementsReceive.AdReceiveDetails details1, AdvertisementsReceive.AdReceiveDetails details2) {
            double details1Order = Double.parseDouble(details1.getOrder());
            double details2Order = Double.parseDouble(details1.getOrder());
            return (int) (details1Order - details2Order);
        }
    }

    /**
     * 轮播时间间隔
     */
    public void setLoopTime(long loopTime) {
        LOOP_TIME = loopTime;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBannerHandler != null) {
            mBannerHandler.removeMessages(MSG_LOOP);
            mBannerHandler = null;
        }
    }


    public class BannerAdapter extends PagerAdapter {
        private final int cacheCount = 3;

        public BannerAdapter() {
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (viewList.size() > cacheCount) {
                container.removeView(viewList.get(position % viewSize));
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = viewList.get(position % viewSize);
            ViewGroup parent = (ViewGroup) imageView.getParent();
            if (parent != null) {
                parent.removeView(imageView);
            }
            GlideAppManager.loadImage(mDetails.get(position % viewSize).getImageName(),
                                      imageView,
                                      defaultImageId(),
                                      imageType);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String skipUrl = mDetails.get(position % viewSize).getSkipUrl();
                    if (mOnClickListener != null && skipUrl != null && !skipUrl.isEmpty()) {
                        mOnClickListener.onClick(view,
                                                 skipUrl,
                                                 mDetails.get(position % viewSize).getIsAddHeader().equals("1"),
                                                 position % viewSize);
                    }
                }
            });
            return imageView;
        }

        @Override
        public int getCount() {
            return mDetails.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int defaultImageId() {
            int defaultId = R.drawable.default_banner;
            switch (imageType) {
                case AppStoreUtils.AppStoreImageType.AD:
                    defaultId = R.drawable.default_banner;
                    break;
                case AppStoreUtils.AppStoreImageType.SCREEN:
                    defaultId = R.mipmap.screen_default;
                    break;
            }
            return defaultId;
        }
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public interface OnClickListener {
        void onClick(View view, String url, boolean isAddHeader, int position);
    }
}
