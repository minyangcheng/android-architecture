package com.min.hybrid.webview.bridge;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.min.hybrid.HybridConstants;
import com.min.hybrid.util.HybridUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JSBridge {

    public static Map<String, HashMap<String, Method>> mExposedMethods = new HashMap<>();

    public static void register(String moduleName, Class clazz) {
        try {
            HashMap<String, Method> mMethodsMap = new HashMap<>();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                mMethodsMap.put(method.getName(), method);
            }
            mExposedMethods.put(moduleName, mMethodsMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String callJava(WebView webView, String url) {
        BridgeCallback callback = null;
        String error = null;
        String methodName = null;
        String apiName = null;
        String param = null;
        String port = null;

        boolean parseSuccess = false;
        while (!parseSuccess) {
            if (url.contains("#")) {
                error = "url不能包涵特殊字符'#'";
                break;
            }
            if (!url.startsWith(HybridConstants.BRIDGE_SCHEME)) {
                error = "scheme错误";
                break;
            }
            if (TextUtils.isEmpty(url)) {
                error = "url不能为空";
                break;
            }
            Uri uri = Uri.parse(url);
            if (uri == null) {
                error = "url解析失败";
                break;
            }
            apiName = uri.getHost();
            if (TextUtils.isEmpty(apiName)) {
                error = "API_Nam为空";
                break;
            }
            port = uri.getPort() + "";
            if (TextUtils.isEmpty(port)) {
                error = "port为空";
                break;
            }
            callback = new BridgeCallback(port, webView);
            methodName = uri.getPath();
            methodName = methodName.replace("/", "");
            if (TextUtils.isEmpty(methodName)) {
                error = "方法名为空";
                break;
            }
            param = uri.getQuery();
            if (TextUtils.isEmpty(param)) {
                param = "{}";
            }
            parseSuccess = true;
        }
        if (!parseSuccess) {
            if (callback == null) {
                new BridgeCallback(BridgeCallback.ERROR_PORT, webView).applyNativeError(url, error);
            } else {
                callback.applyFail(error);
            }
            return error;
        }
        if (mExposedMethods.containsKey(apiName)) {
            HashMap<String, Method> methodHashMap = mExposedMethods.get(apiName);
            if (methodHashMap != null && methodHashMap.containsKey(methodName)) {
                Method method = methodHashMap.get(methodName);
                execute(webView, method, param, callback);
            } else {
                error = apiName + "." + methodName + "未找到";
                callback.applyFail(error);
                return error;
            }
        } else {
            error = apiName + "未注册";
            callback.applyFail(error);
            return error;
        }
        return null;
    }

    private static void execute(final WebView webView, final Method method, final String param, final BridgeCallback callback) {
        HybridUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (method != null) {
                    try {
                        method.invoke(null, webView, JSON.parseObject(param), callback);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.applyFail(e.toString());
                    }
                }
            }
        });
    }

}
