package com.min.sample.app;

import android.widget.ImageView;

import com.fit.we.library.FitConfiguration;
import com.fit.we.library.FitWe;
import com.min.core.base.BaseApp;
import com.min.core.util.ImageLoaderWrap;
import com.min.hybrid.FitHybrid;
import com.min.router.GlobalRouter;
import com.min.sample.BuildConfig;

/**
 * Created by minyangcheng on 2016/10/13.
 */
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init() {
        GlobalRouter.getInstance()
                .init(this, BuildConfig.DEBUG);

        FitHybrid.getInstance()
                .setImageLoader(new FitHybrid.HybridImageLoader() {
                    @Override
                    public void loadImage(ImageView view, String url) {
                        ImageLoaderWrap.displayHttpImage(url, view);
                    }
                });

        FitConfiguration configuration = new FitConfiguration(this)
                .setDebug(BuildConfig.DEBUG)
                .setFitWeServer("http://10.10.12.170")
                .addNativeParam("apiServer", "http://www.cg.com")
                .setImageLoader(new FitConfiguration.FitWeImageLoader() {
                    @Override
                    public void loadImage(ImageView view, String url) {
                        ImageLoaderWrap.displayImage(view,url);
                    }
                });
        FitWe.getInstance().init(configuration);
    }

}
