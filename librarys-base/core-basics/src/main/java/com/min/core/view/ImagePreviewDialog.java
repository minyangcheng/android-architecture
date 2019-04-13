package com.min.core.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.min.common.util.ScreenUtils;
import com.min.core.R;
import com.min.core.helper.ImageLoaderHelper;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class ImagePreviewDialog extends AppCompatDialog {

    private static final String TAG = ImagePreviewDialog.class.getSimpleName();

    private ViewPager mVp;
    private TextView mNumTv;

    private int mPos;

    private List<ImageItem> mImageItemList;

    private int mImageWidth;
    private int mImageHeight;

    public ImagePreviewDialog(Context context, List<ImageItem> imageItemList, int pos) {
        super(context);
        setDialogTheme();
        this.mImageItemList = imageItemList;
        this.mPos = pos;

        setImageSize();
    }

    private void setImageSize() {
        mImageWidth = ScreenUtils.getScreenWidth() / 2;
        mImageHeight = ScreenUtils.getScreenHeight() / 2;
    }

    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image_preview);
        initViews();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }

    private void initViews() {
        mVp = findViewById(R.id.vp);
        mNumTv = findViewById(R.id.tv);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                displayImage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ImagePageAdapter pageAdapter = new ImagePageAdapter(mImageItemList);
        mVp.setAdapter(pageAdapter);

        if (mPos == 0) {
            displayImage(mPos);
        }
        mVp.setCurrentItem(mPos);
    }

    private void displayImage(int pos) {
        this.mPos = pos;
        ImageItem imageItem = mImageItemList.get(mPos);
    }

    public class ImagePageAdapter extends PagerAdapter {

        private List<ImageItem> imageItems;

        public ImagePageAdapter(List<ImageItem> imageItems) {
            this.imageItems = imageItems;
        }

        @Override
        public int getCount() {
            return imageItems.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView imageView = new PhotoView(container.getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            container.addView(imageView);
            displayImage(position, imageView);
            return imageView;
        }

        private void displayImage(int pos, ImageView imageView) {
            mNumTv.setText((pos + 1) + "/" + mImageItemList.size());
            ImageItem imageItem = imageItems.get(pos);
            if (imageItem != null && !TextUtils.isEmpty(imageItem.source)) {
                ImageLoaderHelper.displayImage(imageItem.source, imageView, mImageWidth, mImageHeight);
            }
        }

    }

    public static class ImageItem {

        public String source;

        public ImageItem(String source){
            this.source = source;
        }

    }

}
