package com.seuic.app.store.ui.dialog;

import android.view.View;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.bean.RecycleObject;

import java.util.List;

/**
 * Created on 2017/9/29.
 *
 * @author dpuntu
 */

public class DialogManager {
    private HintDialog mHintDialog;
    private DetailImageDialog mDetailImageDialog;
    private static DialogManager mDialogManager = new DialogManager();

    public DialogManager() {
    }

    public static DialogManager getInstance() {
        return mDialogManager;
    }

    public void showHintDialog(String title, String summary, View.OnClickListener okClick) {
        if (mHintDialog != null && !mHintDialog.isShowing()) {
            mHintDialog.dismiss();
            mHintDialog = null;
        }
        mHintDialog = new HintDialog(AppStoreApplication.getApp(), HintDialog.ButtonStyle.ONE);
        mHintDialog.setHintTitle(title);
        mHintDialog.setHintContent(summary);
        mHintDialog.setOkClickListener(okClick);
        mHintDialog.setCancelable(false);
        mHintDialog.show();
    }

    public void showHintDialog(String title, String summary, View.OnClickListener okClick, View.OnClickListener cancelClick) {
        if (mHintDialog != null && !mHintDialog.isShowing()) {
            mHintDialog.dismiss();
            mHintDialog.setCancelable(false);
            mHintDialog = null;
        }
        mHintDialog = new HintDialog(AppStoreApplication.getApp(), HintDialog.ButtonStyle.TWO);
        mHintDialog.setHintTitle(title);
        mHintDialog.setHintContent(summary);
        mHintDialog.setCancelClickListener(cancelClick);
        mHintDialog.setOkClickListener(okClick);
        mHintDialog.setCancelable(false);
        mHintDialog.show();
    }


    public void dismissHintDialog() {
        if (mHintDialog != null && mHintDialog.isShowing()) {
            mHintDialog.dismiss();
        }
    }

    public void showImageDetailDialog(int position, List<RecycleObject> mRecycleObjectList) {
        if (mDetailImageDialog == null) {
            mDetailImageDialog = new DetailImageDialog(AppStoreApplication.getApp());
        }
        mDetailImageDialog.initBannerView(position, mRecycleObjectList);
        if (!mDetailImageDialog.isShowing()) {
            mDetailImageDialog.show();
        }
    }

    public void dismissImageDetailDialog() {
        if (mDetailImageDialog != null && mDetailImageDialog.isShowing()) {
            mDetailImageDialog.dismiss();
        }
    }
}
