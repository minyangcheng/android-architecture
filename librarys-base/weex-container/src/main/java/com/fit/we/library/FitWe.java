package com.fit.we.library;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.fit.we.library.resource.ResourceCheck;
import com.fit.we.library.resource.ResourceParse;
import com.fit.we.library.util.ActivityHandler;
import com.fit.we.library.util.FitLog;
import com.fit.we.library.util.FitUtil;
import com.fit.we.library.util.LifecycleHandler;
import com.fit.we.library.util.ModuleLoader;
import com.fit.we.library.util.SharePreferenceUtil;
import com.fit.we.library.util.UriHandler;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

/**
 * Created by minyangcheng on 2018/4/1.
 */

public class FitWe {

    private static FitWe fitWe;

    private FitConfiguration configuration;
    private ResourceCheck resourceCheck;
    private ResourceParse resourceParse;

    private FitWe() {
    }

    public static FitWe getInstance() {
        if (fitWe == null) {
            synchronized (FitWe.class) {
                if (fitWe == null) {
                    fitWe = new FitWe();
                }
            }
        }
        return fitWe;
    }

    public void init(FitConfiguration configuration) {
        if (configuration.getContext() != null && FitUtil.shouldInit(configuration.getContext())) {
            this.configuration = configuration;
            checkParams();
            initConfig();
        }
    }

    private void checkParams() {
        if (configuration.getFitWeServer() == null) {
            throw new RuntimeException("config hostServer can not be null");
        }
        if (configuration.getImageLoader() == null) {
            throw new RuntimeException("config imageLoader can not be null");
        }
        if (configuration.getCheckApiHandler() == null) {
            FitLog.d(FitConstants.LOG_TAG, "请注意:未设置更新检测处理,将会导致不会从远程更新最新的包");
        }
    }

    private void initConfig() {
        LifecycleHandler lifecycleHandler = new LifecycleHandler(new LifecycleHandler.OnTaskSwitchListener() {
            @Override
            public void onActivityCreated(Activity activity) {
                ActivityHandler.push(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityHandler.pop(activity);
            }

            @Override
            public void onTaskSwitchToForeground() {
                FitWe.getInstance().checkVersion();
            }

            @Override
            public void onTaskSwitchToBackground() {

            }
        });
        configuration.getApplication().registerActivityLifecycleCallbacks(lifecycleHandler);
        initWeexConfig();
        resourceCheck = new ResourceCheck(configuration.getContext(), configuration.getCheckApiHandler());
        resourceParse = new ResourceParse();
        FitLog.writeLogs(configuration.isDebug());
    }

    private void initWeexConfig() {
        InitConfig.Builder builder = new InitConfig.Builder();
        builder.setImgAdapter(new IWXImgLoaderAdapter() {
            @Override
            public void setImage(String url, ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
                if(view==null|| TextUtils.isEmpty(url)){
                    return;
                }
                String uri = UriHandler.handleImageUri(view.getContext(), url);
                configuration.getImageLoader().loadImage(view, uri);
            }
        });
        WXSDKEngine.initialize(configuration.getApplication(), builder.build());
        ModuleLoader.loadModuleFromAsset(getContext());
        WXEnvironment.setOpenDebugLog(configuration.isDebug());
    }

    public ResourceParse getResourceParse() {
        return resourceParse;
    }

    public ResourceCheck getResourceCheck() {
        return resourceCheck;
    }

    public FitConfiguration getConfiguration() {
        return configuration;
    }

    public void checkVersion() {
        getResourceCheck().checkVersion();
    }

    public long prepareJsBundle(Context context) {
        return getResourceParse().prepareJsBundle(context);
    }

    public String getVersion() {
        return SharePreferenceUtil.getVersion(configuration.getContext());
    }

    public Context getContext() {
        return configuration.getContext();
    }

}
