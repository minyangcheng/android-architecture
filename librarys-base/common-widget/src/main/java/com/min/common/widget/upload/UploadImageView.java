package com.min.common.widget.upload;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.min.common.widget.R;

public class UploadImageView extends FrameLayout {

    private ImageView mIv;
    private TextView mStatusTv;
    private TextView mImgNameTv;
    private ImageView mCameraIndicatorIv;
    private ImageView mPlayIndicatorIv;

    private int mStatus;

    public UploadImageView(Context context) {
        this(context, null);
    }

    public UploadImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_upload_image, this);
        findViews();
        setStatus(UploadImageBean.UPLOAD_PREPARED);
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        mIv.setScaleType(scaleType);
    }

    private void findViews() {
        mIv = findViewById(R.id.iv);
        mStatusTv = findViewById(R.id.tv_status);
        mImgNameTv = findViewById(R.id.tv_imgName);
        mPlayIndicatorIv = findViewById(R.id.iv_play_indicator);
        mCameraIndicatorIv = findViewById(R.id.iv_camera_indicator);
    }

    public void setImageResource(int resId) {
        mIv.setImageResource(resId);
    }

    public void setImageName(String imageName) {
        if (TextUtils.isEmpty(imageName)) {
            return;
        }
        mImgNameTv.setText(imageName);
        mImgNameTv.setVisibility(VISIBLE);
    }

    public ImageView getImageView() {
        return mIv;
    }

    public void setStatus(int status) {
        this.mStatus = status;
        switch (status) {
            case UploadImageBean.UPLOAD_PREPARED:
                mStatusTv.setText("");
                mStatusTv.setVisibility(INVISIBLE);
                break;
            case UploadImageBean.UPLOAD_ING:
                mStatusTv.setText("上传中...");
                mStatusTv.setVisibility(VISIBLE);
                break;
            case UploadImageBean.UPLOAD_SUCCESS:
                mStatusTv.setText("上传成功...");
                mStatusTv.setVisibility(INVISIBLE);
                break;
            case UploadImageBean.UPLOAD_FAIL:
                mStatusTv.setText("上传失败...");
                mStatusTv.setVisibility(VISIBLE);
                break;
        }
    }

    public int getStatus() {
        return mStatus;
    }

    public void hidePlayIndicator() {
        mPlayIndicatorIv.setVisibility(GONE);
    }

    public void showPlayIndicator() {
        mPlayIndicatorIv.setVisibility(VISIBLE);
    }

    public void showCameraIndicator() {
        mCameraIndicatorIv.setVisibility(VISIBLE);
    }

    public void hideCameraIndicator() {
        mCameraIndicatorIv.setVisibility(GONE);
    }

}
