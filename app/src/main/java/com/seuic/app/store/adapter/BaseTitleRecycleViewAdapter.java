package com.seuic.app.store.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.R;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleTitleMoreBean;
import com.seuic.app.store.bean.RecycleViewType;

import java.util.List;

/**
 * Created on 2017/9/22.
 *
 * @author dpuntu
 *         <p>
 *         带title的recycleView adapter
 *         <p>
 *         配置数据都为List<RecycleObject>类型 需要为RecycleObject配置类型：
 *         <p>
 *         RecycleViewType中设置的三种类型，该父类会根据诶型自动配置
 *         如果是RECYCEL_TITLE，则使用RecycleTitleMoreBean类作为数据源，只需要继承，不用再做操作
 *         如果是RECYCEL_DATA和RECYCEL_APP_TYPE，可自定义数据源，继承后，需要自己做具体的配置
 *         继承后需要再继承BaseTitleRecycleViewAdapter.DataViewHolder，作为数据的ViewHolder
 */

public abstract class BaseTitleRecycleViewAdapter<T extends BaseTitleRecycleViewAdapter.DataViewHolder, V>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RecycleObject> mRecycleObjectList;
    private int dataLayout;

    /**
     * @param mRecycleObjectList
     *         数据源
     * @param dataLayout
     *         数据源使用的布局
     */
    public BaseTitleRecycleViewAdapter(List<RecycleObject> mRecycleObjectList, int dataLayout) {
        this.mRecycleObjectList = mRecycleObjectList;
        this.dataLayout = dataLayout;
    }

    /**
     * 刷新RecycleView
     *
     * @param mRecycleObjectList
     *         新的数据源
     */
    public void refreshAdapter(List<RecycleObject> mRecycleObjectList) {
        this.mRecycleObjectList.clear();
        this.mRecycleObjectList = mRecycleObjectList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case RecycleViewType.RECYCEL_TITLE:
                view = LayoutInflater
                        .from(AppStoreApplication.getApp())
                        .inflate(R.layout.recycle_item_head, parent, false);
                return new BaseTitleRecycleViewAdapter.TitleViewHolder(view);
            case RecycleViewType.RECYCEL_DATA:
                view = LayoutInflater
                        .from(AppStoreApplication.getApp())
                        .inflate(dataLayout, parent, false);
                return createDataViewHolder(view);
            default:
                return null;
        }
    }

    /**
     * 创建子类使用的ViewHolder
     *
     * @param view
     *         为数据源的itemView
     */
    protected abstract T createDataViewHolder(View view);

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (mRecycleObjectList.get(position).getType()) {
            case RecycleViewType.RECYCEL_TITLE:
                final RecycleTitleMoreBean bean = (RecycleTitleMoreBean) mRecycleObjectList.get(position).getObject();
                ((BaseTitleRecycleViewAdapter.TitleViewHolder) holder).headText.setText(bean.getTitle());
                ((BaseTitleRecycleViewAdapter.TitleViewHolder) holder).moreLayout.setVisibility(bean.isShowMore() ? View.VISIBLE : View.GONE);
                ((BaseTitleRecycleViewAdapter.TitleViewHolder) holder).moreLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnMoreClickListener != null) {
                            mOnMoreClickListener.OnMoreClick(view, bean.getAssortmentId(), bean.getTitle());
                        }
                    }
                });
                break;
            case RecycleViewType.RECYCEL_DATA:
                loadRecycleData((T) holder, (V) mRecycleObjectList.get(position).getObject());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(view, (V) mRecycleObjectList.get(position).getObject());
                        }
                        if (mOnItemClickAllListener != null) {
                            mOnItemClickAllListener.OnItemClickAll(view, position, mRecycleObjectList);
                        }
                    }
                });
                break;
        }
    }

    /**
     * title中的更多 按钮监听器
     */
    private OnMoreClickListener mOnMoreClickListener;

    public interface OnMoreClickListener {
        void OnMoreClick(View view, String assortmentId, String typeTitle);
    }

    public void setOnMoreClickListener(OnMoreClickListener mOnMoreClickListener) {
        this.mOnMoreClickListener = mOnMoreClickListener;
    }

    /**
     * RecycleView Item 监听器1
     */
    private OnItemClickListener<V> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<V> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener<V> {
        void onItemClick(View view, V v);
    }

    /**
     * RecycleView Item 监听器2
     */
    private OnItemClickAllListener mOnItemClickAllListener;

    public void setOnItemClickAllListener(OnItemClickAllListener mOnItemClickAllListener) {
        this.mOnItemClickAllListener = mOnItemClickAllListener;
    }

    public interface OnItemClickAllListener {
        void OnItemClickAll(View view, int position, List<RecycleObject> mRecycleObjectList);
    }

    @Override
    public int getItemViewType(int position) {
        return mRecycleObjectList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mRecycleObjectList.size();
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView headText;
        LinearLayout moreLayout;

        TitleViewHolder(View itemView) {
            super(itemView);
            headText = (TextView) itemView.findViewById(R.id.recycle_item_title);
            moreLayout = (LinearLayout) itemView.findViewById(R.id.recycle_item_more);
        }
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        DataViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 实际加载dataView
     *
     * @param t
     *         数据的ViewHolder
     * @param v
     *         单项数据
     */
    protected abstract void loadRecycleData(T t, V v);
}

