package com.seuic.app.store.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.bean.AppInfo;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.view.MultifunctionalTextView;

import java.util.List;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public class InstallAppAdapter extends BaseRecycleViewAdapter<InstallAppAdapter.InstallDataViewHolder, AppInfo> {

    public InstallAppAdapter(List<RecycleObject> mRecycleObjectList) {
        super(mRecycleObjectList, R.layout.activity_install_item);
    }

    @Override
    protected InstallDataViewHolder createDataViewHolder(View view) {
        return new InstallAppAdapter.InstallDataViewHolder(view);
    }

    @Override
    protected void loadRecycleData(InstallDataViewHolder holder, final AppInfo info) {
        holder.titleText.setText(info.getAppName());
        holder.summaryText.setText(AppStoreApplication.getApp().getString(R.string.app_version, info.getAppVersion()));
        holder.appImage.setImageDrawable(info.getAppIcon());
        holder.unInstall.setTextState(MultifunctionalTextView.TextViewState.UNINSTALL);

//        Loger.d("-----------------------------------start--------------------------------------");
//        Loger.e("流量下载->" + TimesBytesUtils.getDownloadDataUsage4G());
//        Loger.e("流量上传->" + TimesBytesUtils.getUploadDataUsage4G());
//        Loger.e("WIFI下载->" + TimesBytesUtils.getDownloadDataUsageWifi());
//        Loger.e("WIFI上传->" + TimesBytesUtils.getUploadDataUsageWifi());
//        Loger.e("总流量下载->" + TimesBytesUtils.getDownloadDataUsage());
//        Loger.e("总流量上传->" + TimesBytesUtils.getUploadDataUsage());
//        String[] bytes = TimesBytesUtils.getAppDataUsage(info.getPackageName());
//        if (bytes != null) {
//            Loger.i(info.getAppName() + " 下载->" + bytes[0]);
//            Loger.i(info.getAppName() + " 上传->" + bytes[1]);
//        }
//        String msg = TimesBytesUtils.appRunTime(info.getPackageName());
//        if (msg != null) {
//            Loger.i(msg);
//        } else {
//            Loger.i("没有运行");
//        }
//        Loger.d("----------------------------------end---------------------------------------");

        holder.unInstall.setTextOnClickListener(new MultifunctionalTextView.TextOnClickListener() {
            @Override
            public void onTextClick(View view, int typeState) {
                if (typeState == MultifunctionalTextView.TextViewState.UNINSTALL) {
                    mTextOnClickListener.unInstall(info);
                }
            }
        });
    }

    class InstallDataViewHolder extends BaseRecycleViewAdapter.DataViewHolder {
        TextView titleText, summaryText;
        MultifunctionalTextView unInstall;
        ImageView appImage;

        public InstallDataViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.install_app_item_title);
            summaryText = (TextView) itemView.findViewById(R.id.install_app_item_summary);
            appImage = (ImageView) itemView.findViewById(R.id.install_app_item_image);
            unInstall = (MultifunctionalTextView) itemView.findViewById(R.id.install_app_item_uninstall);
        }
    }

    private TextOnClickListener mTextOnClickListener;

    public void setTextOnClickListener(TextOnClickListener mTextOnClickListener) {
        this.mTextOnClickListener = mTextOnClickListener;
    }

    public interface TextOnClickListener {
        void unInstall(AppInfo info);
    }
}
