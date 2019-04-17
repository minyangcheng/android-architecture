package com.min.sample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.min.common.widget.recyclerview.HFRecyclerViewAdapter;
import com.min.sample.R;
import com.min.sample.data.model.InfoBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by minyangcheng on 2016/10/13.
 */
public class InfoAdapter extends HFRecyclerViewAdapter<InfoBean, InfoAdapter.ItemViewHolder> {

    public InfoAdapter(Context context) {
        super(context);
    }

    @Override
    public ItemViewHolder onCreateDataItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_info, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindDataItemViewHolder(ItemViewHolder holder, int position) {
        InfoBean data = getData().get(position);
        holder.titleTv.setText(data.title);
        holder.itemView.setOnClickListener((v) -> {
            if (mOnItemClickLitener != null) {
                mOnItemClickLitener.onItemClick(v, position);
            }
        });
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView titleTv;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
