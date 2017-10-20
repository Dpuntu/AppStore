package com.seuic.app.store.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seuic.app.store.R;
import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleSearchBean;
import com.seuic.app.store.greendao.GreenDaoManager;

import java.util.List;

/**
 * Created on 2017/9/26.
 *
 * @author dpuntu
 */

public class SearchHistoryAdapter extends BaseRecycleViewAdapter<SearchHistoryAdapter.SearchHistoryViewHolder, RecycleSearchBean> {

    public SearchHistoryAdapter(List<RecycleObject> mRecycleObjectList) {
        super(mRecycleObjectList, SearchHistoryViewHolder.class, R.layout.activity_search_history_item);
    }

    @Override
    protected void loadRecycleData(final SearchHistoryViewHolder holder, final RecycleSearchBean recycleSearchBean) {
        holder.nameText.setText(recycleSearchBean.getAppName());
        holder.deleteImage.setVisibility(recycleSearchBean.isShowDelete() ? View.VISIBLE : View.GONE);
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GreenDaoManager.getInstance().removeSearchHistoryTable(holder.nameText.getText().toString());
                if (mOnDeleteClickListener != null) {
                    mOnDeleteClickListener.onDelete(view, recycleSearchBean);
                }
            }
        });
    }

    public static class SearchHistoryViewHolder extends BaseRecycleViewAdapter.DataViewHolder {
        ImageView deleteImage;
        TextView nameText;

        public SearchHistoryViewHolder(View itemView) {
            super(itemView);
            deleteImage = (ImageView) itemView.findViewById(R.id.search_history_delete);
            nameText = (TextView) itemView.findViewById(R.id.search_history_name);
        }
    }

    private OnDeleteClickListener mOnDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener mOnDeleteClickListener) {
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }

    public interface OnDeleteClickListener {
        void onDelete(View view, RecycleSearchBean recycleSearchBean);
    }
}
