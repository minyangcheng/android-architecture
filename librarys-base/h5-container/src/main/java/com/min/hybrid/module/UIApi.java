package com.min.hybrid.module;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.min.hybrid.webview.bridge.AbstractModule;
import com.min.hybrid.webview.bridge.JSCallback;
import com.min.hybrid.webview.bridge.ModuleInstance;

public class UIApi extends AbstractModule {

    public UIApi(Context context) {
        super(context);
    }

    public void toast(ModuleInstance instance, JSONObject param, JSCallback jsCallback) {
        String message = param.getString("message");
        String duration = param.getString("duration");
        if (!TextUtils.isEmpty(message)) {
            if ("long".equalsIgnoreCase(duration)) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        }
        jsCallback.applySuccess();
    }

    public void payMoney(ModuleInstance instance, JSONObject param, JSCallback jsCallback) {
        JSONObject data = new JSONObject();
        data.put("result", "付款成功");
        data.put("amount", "100");
        jsCallback.applySuccess(data);
    }

}
