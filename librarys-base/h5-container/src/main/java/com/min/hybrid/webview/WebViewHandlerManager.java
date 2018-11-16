package com.min.hybrid.webview;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by minych on 18-11-9.
 */

public class WebViewHandlerManager {

    private static Map<Context, IWebViewHandler> weexHandlerMap = new HashMap<>();

    public static void add(Context context, IWebViewHandler weexHandler) {
        if (context == null || weexHandler == null) {
            return;
        }
        weexHandlerMap.put(context, weexHandler);
    }

    public static void remove(Context context) {
        if (context == null) {
            return;
        }
        weexHandlerMap.remove(context);
    }

    public static IWebViewHandler getWeexHandler(Context context) {
        if (context == null) {
            return null;
        }
        return weexHandlerMap.get(context);
    }

}
