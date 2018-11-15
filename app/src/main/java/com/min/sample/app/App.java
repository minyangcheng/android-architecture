package com.min.sample.app;

import com.min.core.base.BaseApp;
import com.min.sample.BuildConfig;

import me.drakeet.floo.Floo;
import me.drakeet.floo.extensions.LogInterceptor;
import me.drakeet.floo.extensions.OpenDirectlyHandler;

/**
 * Created by minyangcheng on 2016/10/13.
 */
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        initRouter();
    }

    public void initRouter() {
        Floo.configuration()
                .setDebugEnabled(BuildConfig.DEBUG)
                .addRequestInterceptor(new LogInterceptor("Request"))
                .addTargetInterceptor(new LogInterceptor("Target"))
                .addTargetNotFoundHandler(new OpenDirectlyHandler());
    }

}
