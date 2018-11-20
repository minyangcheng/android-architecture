package com.min.hybrid.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.min.common.widget.TitleBar;
import com.min.hybrid.FitHybrid;
import com.min.hybrid.HybridConstants;

/**
 * Created by minych on 18-11-20.
 *
 * 提供TitleBar加载网络图片功能
 */

public class TitleBarImageLoader implements TitleBar.TitleBarImageLoader {

    @Override
    public void loadImage(ImageView view, String url) {
        if (view == null || TextUtils.isEmpty(url)) {
            return;
        }
        FitHybrid.HybridImageLoader imageLoader = FitHybrid.getInstance().getImageLoader();
        if (imageLoader != null) {
            imageLoader.loadImage(view, url);
        } else {
            L.d(HybridConstants.TAG, "pls set imageloader to FitHybrid");
        }
    }
}
