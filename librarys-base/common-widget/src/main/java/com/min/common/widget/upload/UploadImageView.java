package com.min.common.widget.upload;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
    private String imageName;

    private ImageView mVideoIndicatorIv;
    private ImageView mCameraIndicatorIv;

    private int mStatus;

    public UploadImageView(Context context) {
        this(context, null);
    }

    public UploadImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_upload_image, this);
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.UploadImageView, defStyle, 0);
        String imgName = a.getString(R.styleable.UploadImageView_imgName);
        Drawable originalDrawable = a.getDrawable(R.styleable.UploadImageView_originalImg);
        int scaleType = a.getInteger(R.styleable.UploadImageView_scaleType, 0);
        a.recycle();

        findViews();

        if (!TextUtils.isEmpty(imgName)) {
            mImgNameTv.setText(imgName);
            mImgNameTv.setVisibility(VISIBLE);
        } else {
            mImgNameTv.setVisibility(GONE);
        }
        if (originalDrawable != null) {
            mIv.setImageDrawable(originalDrawable);
        }
        setScaleType(scaleType);
        setStatus(UploadImageBean.UPLOAD_PREPARED);
    }

    public void setScaleType(int scaleType) {
        switch (scaleType) {
            case 0:  //fitCenter
                mIv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;
            case 1:  //centerCrop
                mIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case 2:  //center
                mIv.setScaleType(ImageView.ScaleType.CENTER);
                break;
            case 3:  //FIT_XY
                mIv.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }
    }

    private void findViews() {
        mIv = (ImageView) findViewById(R.id.iv);
        mStatusTv = (TextView) findViewById(R.id.tv_status);
        mImgNameTv = (TextView) findViewById(R.id.tv_imgName);
        mVideoIndicatorIv = (ImageView) findViewById(R.id.iv_video_play);
        mCameraIndicatorIv = (ImageView) findViewById(R.id.iv_camera);
    }

    public void setImageResId(int resId) {
        if (resId > 0) {
            mIv.setImageResource(resId);
        }
    }

    public void setImageName(String imageName) {
        if (TextUtils.isEmpty(imageName)) {
            return;
        }
        this.imageName = imageName;
        mImgNameTv.setText(imageName);
        mImgNameTv.setVisibility(VISIBLE);
    }

    public String getImageName() {
        return imageName;
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

    public void hideVideoIndicator() {
        mVideoIndicatorIv.setVisibility(GONE);
    }

    public void showVideoIndicator() {
        mVideoIndicatorIv.setVisibility(VISIBLE);
    }

    public void showCameraIndicator() {
        mCameraIndicatorIv.setVisibility(VISIBLE);
    }

    public void hideCameraIndicator() {
        mCameraIndicatorIv.setVisibility(GONE);
    }

}
