package com.seuic.app.store.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.seuic.app.store.R;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.ScreenShotBean;
import com.seuic.app.store.bean.response.AdvertisementsReceive;
import com.seuic.app.store.utils.AppStoreUtils;
import com.seuic.app.store.view.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/10/9.
 *
 * @author dpuntu
 */

public class DetailImageDialog extends Dialog {
    private BannerView mBannerView;

    public DetailImageDialog(Context context) {
        this(context, R.style.AlertDialogTheme);
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    public DetailImageDialog(Context context, int themeResId) {
        super(context, themeResId);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_detail_image, null, false);
        this.setContentView(dialogView);
        this.setCanceledOnTouchOutside(false);
        mBannerView = (BannerView) dialogView.findViewById(R.id.dialog_images);
    }

    public void initBannerView(int position, List<RecycleObject> mRecycleObjectList) {
        List<AdvertisementsReceive.AdReceiveDetails> mDetails = new ArrayList<>();
        for (int i = 0; i < mRecycleObjectList.size(); i++) {
            ScreenShotBean mScreenShotBean = (ScreenShotBean) mRecycleObjectList.get(i).getObject();
            mDetails.add(new AdvertisementsReceive.AdReceiveDetails(mScreenShotBean.getShotName(), position + "", null, "1"));
        }
        mBannerView.setViewUrlList(mDetails, mRecycleObjectList.size(), position, AppStoreUtils.AppStoreImageType.SCREEN);
        mBannerView.isAutoLoop(false);
    }
}

