package com.min.hybrid.webview.bridge;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.min.hybrid.HybridConstants;
import com.min.hybrid.util.HybridUtil;
import com.min.hybrid.util.L;

public class JSBridge {

    public static String JS_FUNCTION = "javascript:JSBridge._handleMessageFromNative(%s);";

    public static void callNative(WebView webView, String url) {
        JSCallback callback = null;
        String error = null;
        String moduleName = null;
        String methodName = null;
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
            moduleName = uri.getHost();
            if (TextUtils.isEmpty(moduleName)) {
                error = "moduleName为空";
                break;
            }
            port = uri.getPort() + "";
            if (TextUtils.isEmpty(port)) {
                error = "port为空";
                break;
            }
            callback = new JSCallback(port, webView);
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
                new JSCallback(webView).applyError(url, error);
            } else {
                callback.applyFail(error);
            }
        }
        ModuleInstance moduleInstance = ModuleInstanceManager.getModuleInstance(webView.getContext());
        if (moduleInstance != null && moduleInstance.hasModule(moduleName)) {
            Class[] paramsTypes = new Class[3];
            paramsTypes[0] = ModuleInstance.class;
            paramsTypes[1] = JSONObject.class;
            paramsTypes[2] = JSCallback.class;
            if (moduleInstance.hasMethodInModule(moduleName, methodName, paramsTypes)) {
                callNative(moduleInstance, moduleName, methodName, param, callback);
            } else {
                error = moduleName + "." + methodName + "未找到";
                callback.applyFail(error);
            }
        } else {
            error = moduleName + "未注册";
            callback.applyFail(error);
        }
    }

    private static void callNative(final ModuleInstance moduleInstance, final String moduleName, final String methodName, final String param, final JSCallback jsCallback) {
        HybridUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Object[] args = new Object[3];
                args[0] = moduleInstance;
                args[1] = JSON.parseObject(param);
                args[2] = jsCallback;
                moduleInstance.invokeModule(moduleName, methodName, args);
            }
        });
    }

    public static void executeJs(WebView webView, String port, JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("responseId", port);
        jsonObject.put("responseData", data);
        String execJs = String.format(JS_FUNCTION, jsonObject.toJSONString());
        executeJs(webView, execJs);
    }

    public static void executeJsByEvent(WebView webView, String handlerName, JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("handlerName", handlerName == null ? "" : handlerName);
        jsonObject.put("data", data == null ? (new JSONObject()) : data);
        String execJs = String.format(JS_FUNCTION, jsonObject.toJSONString());
        executeJs(webView, execJs);
    }

    public static void executeJs(final WebView webView, final String js) {
        HybridUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (webView != null && checkContext(webView.getContext()) && webView.getParent() != null) {
                    L.d(HybridConstants.TAG, String.format("executeJs-->%s", js));
                    webView.loadUrl(js);
                }
            }
        });
    }

    private static boolean checkContext(Context context) {
        if (context == null) {
            return false;
        }
        Activity activity = (Activity) context;
        return !activity.isFinishing();
    }

}
