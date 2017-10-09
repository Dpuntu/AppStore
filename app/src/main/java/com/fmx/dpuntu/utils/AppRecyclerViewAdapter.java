package com.fmx.dpuntu.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmx.dpuntu.api.AppListResponse;
import com.fmx.dpuntu.mvp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class AppRecyclerViewAdapter extends RecyclerView.Adapter {
    private ArrayList<AppInfo> appInfos;
    private List<AppListResponse.DownloadAppInfo> appDownLoadInfos;
    private Context context;

    public AppRecyclerViewAdapter(Context context, ArrayList<AppInfo> appInfos) {
        this.context = context;
        this.appInfos = appInfos;
    }

    public AppRecyclerViewAdapter(Context context, List<AppListResponse.DownloadAppInfo> appDownLoadInfos) {
        this.context = context;
        this.appDownLoadInfos = appDownLoadInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list_item, null);
        ViewHolder mViewHolder = new ViewHolder(rootView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mViewHolder = (ViewHolder) holder;

        if (appDownLoadInfos != null) {
            mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRecyclerViewClickListener != null) {
                        mRecyclerViewClickListener.onClick(v, appDownLoadInfos.get(position));
                    }
                }
            });
            AppListResponse.DownloadAppInfo info = appDownLoadInfos.get(position);
            if (holder != null) {
                mViewHolder.appName.setText("应用名:" + info.getAppName() + "   安装包大小:" + info.getAppSize());
                mViewHolder.appVersion.setText("最新版本:" + info.getAppVersion());
                mViewHolder.packageName.setText("程序包名:" + info.getAppPackageName());
                String url = AndroidUtils.decrypt(info.getIconUrl(), info.getAppHashCode().substring(0, 8));
                Picasso.with(context)
                        //load()下载图片
                        .load(url)
                        //裁剪图片尺寸
                        .resize(100, 100)
                        //设置图片圆角
                        .centerCrop()
                        //下载中显示的图片
                        .placeholder(R.mipmap.ic_launcher)
                        //下载失败显示的图片
                        .error(R.mipmap.ic_launcher)
                        //init()显示到指定控件
                        .into(mViewHolder.mImageView);
            }
        } else {
            mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLocalRecyclerViewClickListener != null) {
                        mLocalRecyclerViewClickListener.onClick(v, appInfos.get(position));
                    }
                }
            });
            AppInfo info = appInfos.get(position);
            if (holder != null) {
                mViewHolder.appName.setText(info.getAppName());
                mViewHolder.appVersion.setText(info.getAppVersion());
                mViewHolder.packageName.setText(info.getPackageName());
                mViewHolder.mImageView.setImageDrawable(info.getAppIcon());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (appDownLoadInfos != null) {
            return appDownLoadInfos.size();
        }
        return appInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView appName, packageName, appVersion;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.item_image);
            appName = (TextView) itemView.findViewById(R.id.item_app_name);
            packageName = (TextView) itemView.findViewById(R.id.item_package_name);
            appVersion = (TextView) itemView.findViewById(R.id.item_app_version);
        }
    }

    private RecyclerViewClickListener mRecyclerViewClickListener;

    public void serRecyclerViewClickListener(RecyclerViewClickListener mRecyclerViewClickListener) {
        this.mRecyclerViewClickListener = mRecyclerViewClickListener;
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, AppListResponse.DownloadAppInfo info);
    }

    private LocalRecyclerViewClickListener mLocalRecyclerViewClickListener;

    public void setLocalRecyclerViewClickListener(LocalRecyclerViewClickListener mLocalRecyclerViewClickListener) {
        this.mLocalRecyclerViewClickListener = mLocalRecyclerViewClickListener;
    }

    public interface LocalRecyclerViewClickListener {
        void onClick(View v, AppInfo info);
    }
}
