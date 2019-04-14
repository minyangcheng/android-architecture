package com.min.common.widget.upload;

import android.content.Context;
import android.graphics.BitmapFactory;
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

    private int mColumnNum;
    private int mHeight;
    private boolean mEnable = true;
    private boolean showCenter;
    private boolean hideCameraIndicator = false;

    private IUploadImagesHandler mUploadImagesHandler;

    public UploadImagesAreaAdapter(Context context, IUploadImagesHandler uploadImagesHandler) {
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

    public void setHideCameraIndicator() {
        hideCameraIndicator = true;
    }

    @Override
    public void onBindDataItemViewHolder(ItemViewHolder holder, final int position) {
        UploadImageBean bean = getData().get(position);
        holder.uploadImageView.setImageName(bean.name);
        holder.uploadImageView.setStatus(bean.status);

        if (!TextUtils.isEmpty(bean.url)) {
            holder.uploadImageView.getImageView().setScaleType(ImageView.ScaleType.FIT_XY);
            mUploadImagesHandler.loadImage(bean.url,holder.uploadImageView.getImageView());
        } else if (!TextUtils.isEmpty(bean.path)) {
            holder.uploadImageView.getImageView().setScaleType(ImageView.ScaleType.FIT_XY);
            mUploadImagesHandler.loadImage(bean.path,holder.uploadImageView.getImageView());
        } else {
            if (showCenter) {
                ImageView imageView = holder.uploadImageView.getImageView();
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                holder.uploadImageView.getImageView().setScaleType(ImageView.ScaleType.FIT_XY);
            }
            holder.uploadImageView.setImageResId(bean.resId);
        }

        if (mEnable && bean.status == UploadImageBean.UPLOAD_PREPARED) {
            holder.uploadImageView.showCameraIndicator();
        } else {
            holder.uploadImageView.hideCameraIndicator();
        }

        if (!TextUtils.isEmpty(bean.fileUrl)) {
            holder.uploadImageView.showVideoIndicator();
        } else {
            holder.uploadImageView.hideVideoIndicator();
        }

        if (!mEnable || bean.status == UploadImageBean.UPLOAD_PREPARED) {
            holder.deleteIv.setVisibility(View.GONE);
        } else {
            holder.deleteIv.setVisibility(View.VISIBLE);
        }
        if (hideCameraIndicator) {
            holder.uploadImageView.hideCameraIndicator();
        }
    }

    private int imgHeight;

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public void setItemHeight(UploadImageView mIv) {
        int tempHeight = mHeight;
        if (mColumnNum == 1 && imgHeight != 0) {
            tempHeight = imgHeight;
        } else {
            try {
                int imgId = getData().get(0).resId;
                if (imgId != 0) {
                    int imageHeight = getImageHeight(mContext, mColumnNum, imgId);
                    if (imageHeight > 0) {
                        tempHeight = imageHeight;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ViewGroup.LayoutParams layoutParams = mIv.getImageView().getLayoutParams();
        layoutParams.height = tempHeight;
        mIv.getImageView().setLayoutParams(layoutParams);
    }

    public void setFooterHeaderHeight(View view) {
        if (view != null && mHeight > 0) {
            UploadImageView uiv = (UploadImageView) view.findViewById(R.id.uiv);
            if (uiv != null) {
                ViewGroup.LayoutParams layoutParams = uiv.getLayoutParams();
                layoutParams.height = mHeight;
                uiv.setLayoutParams(layoutParams);
            }
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

    public int getImageHeight(Context context, int colmNumber, int imgId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imgId, options);
        float imgWidth = options.outWidth;
        float imgHeight = options.outHeight;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels - Kit.dip2px(context, 30 + (colmNumber - 1) * 6);
        float itemWidth = screenWidth / colmNumber;
        float ratio = imgWidth / imgHeight;
        return (int) (itemWidth / ratio);
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

    public void setClomNum(int clomnNum) {
        this.mColumnNum = clomnNum;
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

    public void setUploadImagesHandler(IUploadImagesHandler uploadImagesHandler){

    }

}

