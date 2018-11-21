package com.min.sample.app;

import com.min.core.base.BaseApp;
import com.min.router.GlobalRouter;
import com.min.sample.BuildConfig;

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
        GlobalRouter.getInstance()
                .init(this, BuildConfig.DEBUG);
    }

}
