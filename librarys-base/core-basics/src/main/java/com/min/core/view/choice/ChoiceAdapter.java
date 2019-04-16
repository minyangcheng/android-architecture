package com.min.core.view.choice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.min.common.widget.recyclerview.HFRecyclerViewAdapter;
import com.min.core.R;
import com.min.core.helper.inject.ViewInject;
import com.min.core.helper.inject.annotation.BindView;

public class ChoiceAdapter extends HFRecyclerViewAdapter<ChoiceDialog.ChoiceBean, ChoiceAdapter.ItemViewHolder> {

    private boolean mSingleFlag;
    private boolean mShowArrow;

    public ChoiceAdapter(Context context, boolean singleFlag, boolean showArrow) {
        super(context);
        this.mSingleFlag = singleFlag;
        this.mShowArrow = showArrow;
    }

    @Override
    public ItemViewHolder onCreateDataItemViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_choice, parent,
                false));
    }

    @Override
    public void onBindDataItemViewHolder(ItemViewHolder holder, int position) {
        ChoiceDialog.ChoiceBean data = getData().get(position);
        if (data.statusStr.contains("_")) {
            String[] arr = data.statusStr.split("_");
            holder.statusNameTv.setText(arr[0]);
            if (arr.length > 1) {
                holder.statusRightTv.setVisibility(View.VISIBLE);
                holder.statusRightTv.setText(arr[1]);
            }
        } else {
            holder.statusNameTv.setText(data.statusStr);
        }
        if (mSingleFlag) {
            if (mShowArrow) {
                holder.selectIv.setImageResource(R.drawable.ic_arrow_right);
                holder.selectIv.setVisibility(View.VISIBLE);
            } else {
                holder.selectIv.setVisibility(View.GONE);
            }
        } else {
            holder.selectIv.setVisibility(View.VISIBLE);
            holder.selectIv.setSelected(data.selectFlag);
            if (data.selectFlag) {
                holder.selectIv.setImageResource(R.mipmap.icon_pic_selected);
            } else {
                holder.selectIv.setImageResource(R.mipmap.icon_pic_unselected);
            }
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.tv_status_name)
        public TextView statusNameTv;
//        @BindView(R.id.tv_status_right)
        public TextView statusRightTv;
//        @BindView(R.id.iv_select)
        public ImageView selectIv;

        public ItemViewHolder(View itemView) {
            super(itemView);
//            ViewInject.inject(this, itemView);
            statusNameTv = itemView.findViewById(R.id.tv_status_name);
            statusRightTv = itemView.findViewById(R.id.tv_status_right);
            selectIv = itemView.findViewById(R.id.iv_select);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemClick(itemView, itemPositionInData(getLayoutPosition()));
                    }
                }
            });
        }
    }

}
