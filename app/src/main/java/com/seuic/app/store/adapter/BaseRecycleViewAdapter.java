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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
 *         如果是RECYCLE_TITLE，则使用RecycleTitleMoreBean类作为数据源，只需要继承，不用再做操作
 *         如果是RECYCLE_DATA和RECYCLE_APP_TYPE，可自定义数据源，继承后，需要自己做具体的配置
 *         继承后需要再继承BaseTitleRecycleViewAdapter.DataViewHolder，作为数据的ViewHolder
 */

public abstract class BaseRecycleViewAdapter<VH extends BaseRecycleViewAdapter.DataViewHolder, E>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RecycleObject> mRecycleObjectList;
    private int dataLayout;
    private Class<VH> cls = null;

    private static final int ITEM_HEAD = Integer.MAX_VALUE;

    /**
     * @param mRecycleObjectList
     *         数据源
     * @param dataLayout
     *         数据源使用的布局
     */
    public BaseRecycleViewAdapter(List<RecycleObject> mRecycleObjectList, int dataLayout) {
        this.mRecycleObjectList = mRecycleObjectList;
        this.dataLayout = dataLayout;
    }

    /**
     * @param mRecycleObjectList
     *         数据源
     * @param cls
     *         DataViewHolder 类
     * @param dataLayout
     *         数据源使用的布局
     */
    public BaseRecycleViewAdapter(List<RecycleObject> mRecycleObjectList, Class<VH> cls, int dataLayout) {
        this(mRecycleObjectList, dataLayout);
        this.cls = cls;
    }

    /**
     * 刷新RecycleView
     *
     * @param mRecycleObjectList
     *         新的数据源
     */
    public void refreshAdapter(List<RecycleObject> mRecycleObjectList) {
        if (mRecycleObjectList != null) {
            this.mRecycleObjectList.clear();
        }
        this.mRecycleObjectList = mRecycleObjectList;
        notifyDataSetChanged();
    }

    /**
     * 添加的头View
     */
    private View headView = null;

    /**
     * 添加的头部View
     *
     * @param headView
     *         view
     */
    public void addHeadView(View headView) {
        this.headView = headView;
        notifyItemInserted(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ITEM_HEAD:
                return new BaseRecycleViewAdapter.HeadViewHolder(headView);
            case RecycleViewType.RECYCLE_TITLE:
                view = LayoutInflater
                        .from(AppStoreApplication.getApp())
                        .inflate(R.layout.recycle_item_head, parent, false);
                return new BaseRecycleViewAdapter.TitleViewHolder(view);
            case RecycleViewType.RECYCLE_DATA:
                view = LayoutInflater
                        .from(AppStoreApplication.getApp())
                        .inflate(dataLayout, parent, false);
                return createDataViewHolder(view);
            default:
                return null;
        }
    }

    /**
     * 配置DataViewHolder
     */
    protected void setViewHolderClass(Class<VH> cls) {
        this.cls = cls;
    }

    /**
     * 创建子类使用的ViewHolder, 必须继承自DataViewHolder
     *
     * @param view
     *         为数据源的itemView
     */
    @SuppressWarnings("unchecked")
    private VH createDataViewHolder(View view) {
        if (cls == null) {
            throw new NullPointerException("please use setViewHolderClass function first");
        }
        VH vh = null;
        try {
            Constructor constructor = cls.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            vh = (VH) constructor.newInstance(view);
        } catch (InstantiationException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return vh;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int index = headView != null ? position - 1 : position;
        switch (getItemViewType(position)) {
            case ITEM_HEAD:
                break;
            case RecycleViewType.RECYCLE_TITLE:
                final RecycleTitleMoreBean bean = (RecycleTitleMoreBean) mRecycleObjectList.get(index).getObject();
                ((BaseRecycleViewAdapter.TitleViewHolder) holder).headText.setText(bean.getTitle());
                ((BaseRecycleViewAdapter.TitleViewHolder) holder).moreLayout.setVisibility(bean.isShowMore() ? View.VISIBLE : View.GONE);
                ((BaseRecycleViewAdapter.TitleViewHolder) holder).moreLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnMoreClickListener != null) {
                            mOnMoreClickListener.onMoreClick(view, bean.getAssortmentId(), bean.getTitle());
                        }
                    }
                });
                break;
            case RecycleViewType.RECYCLE_DATA:
                loadRecycleData((VH) holder, (E) mRecycleObjectList.get(index).getObject());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(view, (E) mRecycleObjectList.get(index).getObject());
                        }
                        if (mOnItemClickAllListener != null) {
                            mOnItemClickAllListener.onItemClickAll(view, index, mRecycleObjectList);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * title中的更多 按钮监听器
     */
    private OnMoreClickListener mOnMoreClickListener;

    public interface OnMoreClickListener {
        void onMoreClick(View view, String assortmentId, String typeTitle);
    }

    public void setOnMoreClickListener(OnMoreClickListener mOnMoreClickListener) {
        this.mOnMoreClickListener = mOnMoreClickListener;
    }

    /**
     * RecycleView Item 监听器1
     */
    private OnItemClickListener<E> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<E> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener<E> {
        void onItemClick(View view, E e);
    }

    /**
     * RecycleView Item 监听器2
     */
    private OnItemClickAllListener mOnItemClickAllListener;

    public void setOnItemClickAllListener(OnItemClickAllListener mOnItemClickAllListener) {
        this.mOnItemClickAllListener = mOnItemClickAllListener;
    }

    public interface OnItemClickAllListener {
        void onItemClickAll(View view, int position, List<RecycleObject> mRecycleObjectList);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && headView != null) {
            return ITEM_HEAD;
        }
        return mRecycleObjectList.get(headView != null ? position - 1 : position).getType();
    }

    @Override
    public int getItemCount() {
        return headView == null ? mRecycleObjectList.size() : mRecycleObjectList.size() + 1;
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

    private class HeadViewHolder extends RecyclerView.ViewHolder {
        HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        public DataViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 实际加载dataView
     *
     * @param vh
     *         数据的ViewHolder
     * @param e
     *         单项数据
     */
    protected abstract void loadRecycleData(VH vh, E e);

    public static class RecycleViewType {
        public static final int RECYCLE_TITLE = 100;

        public static final int RECYCLE_DATA = 101;

        public static final int RECYCLE_APP_TYPE = 102;
    }
}

