package com.min.common.widget.upload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.min.common.widget.Kit;
import com.min.common.widget.R;
import com.min.common.widget.recyclerview.HFRecyclerViewAdapter;

/**
 * Created by minyangcheng on 2019/4/12.
 */
public class UploadImagesAreaAdapter extends HFRecyclerViewAdapter<UploadImageBean, UploadImagesAreaAdapter.ItemViewHolder> {

    private OnDeleteIvClickListener mOnDeleteIvClickListener;

    private int mHeight;
    private boolean mEnable = true;

    private AbstractUploadImagesHandler mUploadImagesHandler;

    public UploadImagesAreaAdapter(Context context, AbstractUploadImagesHandler uploadImagesHandler) {
        super(context);
        this.mUploadImagesHandler = uploadImagesHandler;
    }

    @Override
    public ItemViewHolder onCreateDataItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_upload_image, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        setItemHeight(viewHolder.uploadImageView);
        return viewHolder;
    }

    @Override
    public void onBindDataItemViewHolder(ItemViewHolder holder, final int position) {
        UploadImageBean bean = getData().get(position);
        holder.uploadImageView.setImageName(bean.name);
        holder.uploadImageView.setStatus(bean.status);

        if (!TextUtils.isEmpty(bean.url)) {
            holder.uploadImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mUploadImagesHandler.displayImage(bean.url, holder.uploadImageView.getImageView());
        } else if (!TextUtils.isEmpty(bean.path)) {
            holder.uploadImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mUploadImagesHandler.displayImage(bean.path, holder.uploadImageView.getImageView());
        } else {
            holder.uploadImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.uploadImageView.setImageResource(bean.resId);
        }

        if (mEnable && bean.status == UploadImageBean.UPLOAD_PREPARED) {
            holder.uploadImageView.showCameraIndicator();
        } else {
            holder.uploadImageView.hideCameraIndicator();
        }

        if (!TextUtils.isEmpty(bean.fileUrl)) {
            holder.uploadImageView.showPlayIndicator();
        } else {
            holder.uploadImageView.hidePlayIndicator();
        }

        if (!mEnable || bean.status == UploadImageBean.UPLOAD_PREPARED) {
            holder.deleteIv.setVisibility(View.GONE);
        } else {
            holder.deleteIv.setVisibility(View.VISIBLE);
        }
    }

    public void setItemHeight(View view) {
        if (mHeight > 0) {
            ImageView iv = view.findViewById(R.id.iv);
            ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
            layoutParams.height = mHeight;
            iv.setLayoutParams(layoutParams);
        }
    }

    public void setFooterHeaderHeight(View view) {
        if (mHeight > 0) {
            ImageView iv = view.findViewById(R.id.iv);
            ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
            layoutParams.height = mHeight;
            iv.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void setFooterView(View foot) {
        super.setFooterView(foot);
        setFooterHeaderHeight(foot);
    }

    @Override
    public void setHeaderView(View header) {
        super.setHeaderView(header);
        setFooterHeaderHeight(header);
    }

    public void setColumn(int column, float ratio) {
        if (column > 0 && ratio > 0) {
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels - Kit.dip2px(mContext, 5 * (column - 1) + 30);
            int itemWidth = screenWidth / column;
            mHeight = (int) (itemWidth / ratio);
        }
    }

    @Override
    public int getDataItemType(int position) {
        return position;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        UploadImageView uploadImageView;
        ImageView deleteIv;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            uploadImageView = itemView.findViewById(R.id.uiv);
            deleteIv = itemView.findViewById(R.id.iv_delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemClick(itemView, itemPositionInData(getLayoutPosition()));
                    }
                }
            });
            deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDeleteIvClickListener != null) {
                        mOnDeleteIvClickListener.onDeleteIvClick(itemPositionInData(getLayoutPosition()));
                    }
                }
            });
        }
    }

    public void setEnable(boolean enable) {
        this.mEnable = enable;
        notifyDataSetChanged();
    }

    public interface OnDeleteIvClickListener {
        void onDeleteIvClick(int position);
    }

    public void setOnDeleteIvClickListener(OnDeleteIvClickListener onDeleteIvClickListener) {
        mOnDeleteIvClickListener = onDeleteIvClickListener;
    }

}

