package com.min.sample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.min.common.widget.recyclerview.HFRecyclerViewAdapter;
import com.min.sample.R;
import com.min.sample.data.model.MainItem;

public class MainAdapter extends HFRecyclerViewAdapter<MainItem, MainAdapter.ItemViewHolder> {

    public MainAdapter(Context context) {
        super(context);
    }

    @Override
    public MainAdapter.ItemViewHolder onCreateDataItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
        return new MainAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindDataItemViewHolder(MainAdapter.ItemViewHolder holder, int position) {
        MainItem data = getData().get(position);
        holder.titleTv.setText(data.title);
        holder.itemView.setOnClickListener((v) -> {
            if (mOnItemClickLitener != null) {
                mOnItemClickLitener.onItemClick(v, position);
            }
        });
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            this.titleTv = itemView.findViewById(R.id.tv_title);
        }

    }

}
