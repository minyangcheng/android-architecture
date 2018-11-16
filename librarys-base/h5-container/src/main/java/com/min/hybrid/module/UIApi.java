package com.min.hybrid.module;

import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.min.hybrid.webview.bridge.BridgeCallback;
import com.min.hybrid.webview.bridge.IBridgeImpl;

public class UIApi implements IBridgeImpl {

    /**
     * 消息提示
     * message： 需要提示的消息内容
     * duration：显示时长,long或short
     */
    public static void toast(WebView webView, JSONObject param, BridgeCallback callback) {
        String message = param.getString("message");
        String duration = param.getString("duration");
        if (!TextUtils.isEmpty(message)) {
            if ("long".equalsIgnoreCase(duration)) {
                Toast.makeText(webView.getContext(), message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
        callback.applySuccess();
    }

    public static void payMoney(WebView webView, JSONObject param, BridgeCallback callback) {
        JSONObject data = new JSONObject();
        data.put("result", "付款成功");
        data.put("amount", "100");
        callback.applySuccess(data);
    }

}
