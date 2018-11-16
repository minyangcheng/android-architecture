package com.min.hybrid.webview;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.min.hybrid.H5ContainerActivity;
import com.min.hybrid.util.HybridUtil;

public class HybridWebViewClient extends WebViewClient {

    private H5ContainerActivity mContainer;

    public HybridWebViewClient(H5ContainerActivity container) {
        this.mContainer = container;
    }

    @Override
    public void onReceivedError(final WebView view, int errorCode, final String description, final String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        HybridUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebViewHandlerManager.getWeexHandler(view.getContext()).handleError(description, failingUrl);
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
        WebViewHandlerManager.getWeexHandler(view.getContext()).handleFinish(url);
    }
}
