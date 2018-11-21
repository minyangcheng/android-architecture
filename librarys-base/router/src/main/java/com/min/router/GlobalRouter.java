package com.min.router;

import android.content.Context;
import android.os.Bundle;

import com.min.router.handler.H5Handler;
import com.min.router.handler.NativeHandler;
import com.min.router.handler.WeexHandler;

import java.util.Map;

import me.drakeet.floo.Floo;
import me.drakeet.floo.extensions.LogInterceptor;

/**
 * Created by minych on 18-11-21.
 */

public class GlobalRouter {

    private static GlobalRouter router;

    private Context context;

    public static GlobalRouter getInstance() {
        if (router == null) {
            synchronized (GlobalRouter.class) {
                if (router == null) {
                    router = new GlobalRouter();
                }
            }
        }
        return router;
    }

    private GlobalRouter() {
    }

    public void init(Context context, boolean debug) {
        this.context = context.getApplicationContext();
        Floo.configuration()
                .setDebugEnabled(debug)
                .addRequestInterceptor(new LogInterceptor("Request"))
                .addTargetInterceptor(new LogInterceptor("Target"))
                .addTargetNotFoundHandler(new WeexHandler())
                .addTargetNotFoundHandler(new NativeHandler())
                .addTargetNotFoundHandler(new H5Handler());
    }

    public Route navigation(String url, Bundle bundle, Map<String, String> queries) {
        Route route = new Route(url, bundle, queries);
        return route;
    }

    public Route navigation(String url) {
        Route route = new Route(url);
        return route;
    }

    public Context getContext() {
        return context;
    }

}
