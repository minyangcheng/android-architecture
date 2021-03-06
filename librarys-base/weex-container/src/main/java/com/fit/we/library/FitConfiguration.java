package com.fit.we.library;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.fit.we.library.resource.CheckApiHandler;
import com.fit.we.library.util.SharePreferenceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by minyangcheng on 2018/4/1.
 */

public class FitConfiguration {

    private Application application;
    private String fitWeServer;
    private CheckApiHandler checkApiHandler;
    private FitWeImageLoader imageLoader;
    private Map<String, String> nativeParams = new HashMap<>();
    private boolean debug;

    public FitConfiguration(Application application) {
        if (application == null) {
            throw new RuntimeException("fit-we application can not be null");
        }
        this.application = application;
    }

    public Context getContext() {
        return application.getApplicationContext();
    }

    public Application getApplication() {
        return application;
    }

    public String getFitWeServer() {
        if (FitWe.getInstance().getConfiguration().isDebug()) {
            String str = SharePreferenceUtil.getFitWeServer(application.getApplicationContext());
            if (!TextUtils.isEmpty(str)) {
                fitWeServer = str;
            }
        }
        return fitWeServer;
    }

    public FitConfiguration setFitWeServer(String fitWeServer) {
        this.fitWeServer = fitWeServer;
        return this;
    }

    public CheckApiHandler getCheckApiHandler() {
        return checkApiHandler;
    }

    public FitConfiguration setCheckApiHandler(CheckApiHandler checkApiHandler) {
        this.checkApiHandler = checkApiHandler;
        return this;
    }

    public FitConfiguration addNativeParam(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return this;
        }
        nativeParams.put(key, value);
        return this;
    }

    public Map<String, String> getNativeParams() {
        return nativeParams;
    }

    public boolean isDebug() {
        return debug;
    }

    public FitConfiguration setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public FitConfiguration setImageLoader(FitWeImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public FitWeImageLoader getImageLoader() {
        return imageLoader;
    }

    public interface FitWeImageLoader {
        void loadImage(ImageView view, String url);
    }
}
