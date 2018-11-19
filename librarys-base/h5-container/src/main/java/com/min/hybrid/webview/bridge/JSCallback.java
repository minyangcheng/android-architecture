package com.min.hybrid.webview.bridge;

import android.webkit.WebView;

import com.alibaba.fastjson.JSONObject;

import java.lang.ref.WeakReference;

public class JSCallback {

    private String port;

    private WeakReference<WebView> webViewRef;

    public JSCallback(String port, WebView webView) {
        this.port = port;
        if (webView != null) {
            webViewRef = new WeakReference<>(webView);
        }
    }

    public JSCallback(WebView webView) {
        this.port = "3000";
        if (webView != null) {
            webViewRef = new WeakReference<>(webView);
        }
    }

    /**
     * 成功回调
     */
    public void applySuccess() {
        apply(1, "", new JSONObject());
    }

    /**
     * 成功回调
     *
     * @param result
     */
    public void applySuccess(JSONObject result) {
        apply(1, "", result == null ? (new JSONObject()) : result);
    }

    /**
     * 失败回调
     *
     * @param msg
     */
    public void applyFail(String msg) {
        apply(0, msg, new JSONObject());
    }

    /**
     * 非Api级别的错误回调
     *
     * @param errorUrl
     * @param errorDescription
     */
    public void applyError(String errorUrl, String errorDescription) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorDescription", errorDescription);
        jsonObject.put("errorCode", port);
        jsonObject.put("errorUrl", errorUrl);
        WebView webView = webViewRef.get();
        if (webView != null) {
            JSBridge.executeJsByEvent(webView, port, jsonObject);
        }
    }

    private void apply(int code, String msg, JSONObject result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("result", result);
        WebView webView = webViewRef.get();
        if (webView != null) {
            JSBridge.executeJs(webView, port, jsonObject);
        }
    }

}
