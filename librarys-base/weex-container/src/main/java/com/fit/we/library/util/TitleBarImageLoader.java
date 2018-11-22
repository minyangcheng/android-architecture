package com.fit.we.library.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.fit.we.library.FitConfiguration;
import com.fit.we.library.FitConstants;
import com.fit.we.library.FitWe;
import com.min.common.widget.TitleBar;

/**
 * Created by minych on 18-11-20.
 * <p>
 * 提供TitleBar加载网络图片功能
 */

public class TitleBarImageLoader implements TitleBar.TitleBarImageLoader {

    @Override
    public void loadImage(ImageView view, String url) {
        if (view == null || TextUtils.isEmpty(url)) {
            return;
        }
        FitConfiguration.FitWeImageLoader imageLoader = FitWe.getInstance().getConfiguration().getImageLoader();
        if (imageLoader != null) {
            imageLoader.loadImage(view, url);
        } else {
            FitLog.d(FitConstants.LOG_TAG, "pls set imageloader to FitConfiguration");
        }
    }
}
