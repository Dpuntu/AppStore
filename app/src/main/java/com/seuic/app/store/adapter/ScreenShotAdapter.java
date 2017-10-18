package com.seuic.app.store.adapter;

import android.view.View;
import android.widget.ImageView;

import com.seuic.app.store.R;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.ScreenShotBean;
import com.seuic.app.store.glide.GlideAppManager;
import com.seuic.app.store.utils.AppStoreUtils;

import java.util.List;

/**
 * Created on 2017/9/22.
 *
 * @author dpuntu
 *         <p>
 *         屏幕快照画廊适配器
 */

public class ScreenShotAdapter extends BaseRecycleViewAdapter<ScreenShotAdapter.ScreenShotViewHolder, ScreenShotBean> {

    public ScreenShotAdapter(List<RecycleObject> mRecycleObjectList) {
        super(mRecycleObjectList, R.layout.activity_screen_shot_recycle_item);
    }

    @Override
    protected ScreenShotViewHolder createDataViewHolder(View view) {
        return new ScreenShotViewHolder(view);
    }

    @Override
    protected void loadRecycleData(ScreenShotViewHolder holder, ScreenShotBean bean) {
        GlideAppManager.loadImage(bean.getShotName(),
                                  holder.screenItemImage,
                                  R.mipmap.screen_default,
                                  AppStoreUtils.AppStoreImageType.SCREEN);
    }

    class ScreenShotViewHolder extends BaseRecycleViewAdapter.DataViewHolder {
        ImageView screenItemImage;

        ScreenShotViewHolder(View itemView) {
            super(itemView);
            screenItemImage = (ImageView) itemView.findViewById(R.id.screen_item_image);
        }
    }
}
