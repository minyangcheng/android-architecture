package com.min.common.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleBar extends FrameLayout implements View.OnClickListener {

    private ImageView mBackIv;
    private ImageView mCloseIv;
    private ImageView mLeftIv;
    private TextView mLeftTv;

    private ViewGroup mTitleVg;
    private ViewGroup mCustomVg;
    private TextView mMainTitleTv;
    private TextView mSubTitleTv;

    private ImageView[] mRightIvs = new ImageView[2];
    private TextView[] mRightTvs = new TextView[2];

    private TitleBarListener mListener;
    private TitleBarImageLoader mImageLoader;

    public TitleBar(@NonNull Context context) {
        this(context, null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, 0);
        try {
            String imageLoaderClassName = typedArray.getString(R.styleable.TitleBar_imageLoader);
            if (!TextUtils.isEmpty(imageLoaderClassName)) {
                mImageLoader = (TitleBarImageLoader) Class.forName(imageLoaderClassName).newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_title_bar, this);
        findView();
    }

    private void findView() {
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mBackIv.setOnClickListener(this);
        mCloseIv = (ImageView) findViewById(R.id.iv_close);
        mCloseIv.setOnClickListener(this);
        mLeftIv = (ImageView) findViewById(R.id.iv_left);
        mLeftIv.setOnClickListener(this);
        mLeftTv = (TextView) findViewById(R.id.tv_left);
        mLeftTv.setOnClickListener(this);

        mTitleVg = (ViewGroup) findViewById(R.id.view_title);
        mTitleVg.setOnClickListener(this);
        mCustomVg = (ViewGroup) findViewById(R.id.view_custom);
        mMainTitleTv = (TextView) findViewById(R.id.tv_main_title);
        mSubTitleTv = (TextView) findViewById(R.id.tv_sub_title);

        mRightIvs[0] = (ImageView) findViewById(R.id.iv_right_1);
        mRightIvs[0].setOnClickListener(this);
        mRightTvs[0] = (TextView) findViewById(R.id.tv_right_1);
        mRightTvs[0].setOnClickListener(this);
        mRightIvs[1] = (ImageView) findViewById(R.id.iv_right_2);
        mRightIvs[1].setOnClickListener(this);
        mRightTvs[1] = (TextView) findViewById(R.id.tv_right_2);
        mRightTvs[1].setOnClickListener(this);
    }

    public void setBackVisibility(boolean visible) {
        mBackIv.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setCloseVisibility(boolean visible) {
        mCloseIv.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setLeftBtn(String imageUrl, String text) {
        if (!TextUtils.isEmpty(imageUrl)) {
            mLeftTv.setVisibility(View.GONE);
            mLeftIv.setVisibility(View.VISIBLE);
            if (mImageLoader != null) {
                mImageLoader.loadImage(mLeftIv, imageUrl);
            }
        } else {
            mLeftIv.setVisibility(View.GONE);
            mLeftTv.setVisibility(View.VISIBLE);
            mLeftTv.setText(text);
        }
    }

    public void hideLeftButton() {
        mLeftIv.setVisibility(GONE);
        mLeftTv.setVisibility(GONE);
    }

    public void setRightBtn(int which, String imageUrl, String text) {
        if (!TextUtils.isEmpty(imageUrl)) {
            mRightTvs[which].setVisibility(View.GONE);
            mRightIvs[which].setVisibility(View.VISIBLE);
            if (mImageLoader != null) {
                mImageLoader.loadImage(mRightIvs[which], imageUrl);
            }
        } else {
            mRightIvs[which].setVisibility(View.GONE);
            mRightTvs[which].setVisibility(View.VISIBLE);
            mRightTvs[which].setText(text);
        }
    }

    public void hideRightButtons() {
        for (View view : mRightIvs) {
            view.setVisibility(GONE);
        }
        for (View view : mRightTvs) {
            view.setVisibility(GONE);
        }
    }

    public void hideRightButton(int which) {
        if (which >= 0 && which < mRightIvs.length) {
            mRightIvs[which].setVisibility(GONE);
        }
        if (which >= 0 && which < mRightTvs.length) {
            mRightTvs[which].setVisibility(GONE);
        }
    }


    public void addCustomTitleView(View view) {
        mTitleVg.setVisibility(View.GONE);
        mCustomVg.setVisibility(View.VISIBLE);
        mCustomVg.removeAllViews();
        mCustomVg.addView(view);
    }

    public void setTitle(String title) {
        mMainTitleTv.setText(title);
    }

    public void setSubTitle(String subTitle) {
        mSubTitleTv.setVisibility(GONE);
        mSubTitleTv.setText(subTitle);
    }

    public void onClick(View v) {
        if (mListener == null) {
            //如果没有手动设置监听器,则默认点击back按钮为关闭activity
            if (v == mBackIv && v.getContext() instanceof Activity) {
                Activity activity = (Activity) v.getContext();
                activity.finish();
            }
            return;
        }
        if (v == mBackIv) {
            mListener.onClickBack();
        } else if (v == mCloseIv) {
            mListener.onClickClose();
        } else if (v == mLeftIv || v == mLeftIv) {
            mListener.onClickLeft();
        } else if (v == mTitleVg) {
            mListener.onClickTitle();
        } else {
            for (int i = 0; i < mRightIvs.length; i++) {
                if (v == mRightIvs[i]) {
                    mListener.onClickRight(v, i);
                }
            }
            for (int i = 0; i < mRightTvs.length; i++) {
                if (v == mRightTvs[i]) {
                    mListener.onClickRight(v, i);
                }
            }
        }
    }

    public void setTitleBarListener(TitleBarListener listener) {
        this.mListener = listener;
    }

    public interface TitleBarListener {

        void onClickBack();

        void onClickClose();

        void onClickLeft();

        void onClickTitle();

        void onClickRight(View view, int which);

    }

    public interface TitleBarImageLoader {
        void loadImage(ImageView view, String url);
    }

}
