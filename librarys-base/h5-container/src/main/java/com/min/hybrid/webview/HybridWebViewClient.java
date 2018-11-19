package com.min.hybrid.webview;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.min.hybrid.util.HybridUtil;
import com.min.hybrid.webview.bridge.ModuleInstance;
import com.min.hybrid.webview.bridge.ModuleInstanceManager;

public class HybridWebViewClient extends WebViewClient {

    public HybridWebViewClient() {
    }

    @Override
    public void onReceivedError(final WebView view, int errorCode, final String description, final String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        HybridUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ModuleInstance moduleInstance = ModuleInstanceManager.getModuleInstance(view.getContext());
                if (moduleInstance != null && moduleInstance.getWebViewHandler() != null) {
                    moduleInstance.getWebViewHandler().handleError(description, failingUrl);
                }
            }
        });
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        ModuleInstance moduleInstance = ModuleInstanceManager.getModuleInstance(view.getContext());
        if (moduleInstance != null && moduleInstance.getWebViewHandler() != null) {
            moduleInstance.getWebViewHandler().handleFinish(url);
        }
    }
}
