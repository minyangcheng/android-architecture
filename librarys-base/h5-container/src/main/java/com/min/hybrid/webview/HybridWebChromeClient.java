package com.min.hybrid.webview;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.min.hybrid.HybridConstants;
import com.min.hybrid.util.HybridUtil;
import com.min.hybrid.util.L;
import com.min.hybrid.webview.bridge.JSBridge;
import com.min.hybrid.webview.bridge.ModuleInstance;
import com.min.hybrid.webview.bridge.ModuleInstanceManager;

import java.net.URLDecoder;

public class HybridWebChromeClient extends WebChromeClient {

    private Context mContext;

    public HybridWebChromeClient(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        AlertDialog.Builder b2 = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton(
                        "确认",
                        new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        });

        b2.setCancelable(false);
        b2.create();
        b2.show();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        AlertDialog.Builder b2 = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton(
                        "确认",
                        new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
        b2.setCancelable(false);
        b2.create();
        b2.show();
        return true;
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("是否允许使用你的位置?");
        DialogInterface.OnClickListener dialogButtonOnClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int clickedButton) {
                if (DialogInterface.BUTTON_POSITIVE == clickedButton) {
                    callback.invoke(origin, true, true);
                } else if (DialogInterface.BUTTON_NEGATIVE == clickedButton) {
                    callback.invoke(origin, false, false);
                }
            }
        };
        builder.setPositiveButton("允许", dialogButtonOnClickListener);
        builder.setNegativeButton("拒绝", dialogButtonOnClickListener);
        builder.show();
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        L.d(HybridConstants.TAG, String.format("callNative-->%s", URLDecoder.decode(message)));
        JSBridge.callNative(view, message);
        result.confirm("");
        return true;
    }

    @Override
    public void onProgressChanged(final WebView view, final int newProgress) {
        super.onProgressChanged(view, newProgress);
        HybridUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ModuleInstance moduleInstance = ModuleInstanceManager.getModuleInstance(view.getContext());
                if (moduleInstance != null && moduleInstance.getWebViewHandler() != null) {
                    moduleInstance.getWebViewHandler().handleProgressChanged(newProgress);
                }
            }
        });
    }

}
