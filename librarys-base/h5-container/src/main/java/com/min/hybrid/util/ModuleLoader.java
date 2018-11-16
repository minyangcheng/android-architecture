package com.min.hybrid.util;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.min.hybrid.HybridConstants;
import com.min.hybrid.webview.bridge.BridgeCallback;
import com.min.hybrid.webview.bridge.IBridgeImpl;
import com.min.hybrid.webview.bridge.JSBridge;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Created by minych on 18-4-6.
 */

public class ModuleLoader {

    public static void loadModuleFromAsset(Context context) {
        try {
            String[] fileArr = context.getAssets().list("");
            String fileName = null;
            for (int i = 0; i < fileArr.length; i++) {
                fileName = fileArr[i];
                if (fileName.endsWith(HybridConstants.MODULE_FILE_SUFFIX)) {
                    String moduleJsonStr = AssetsUtil.getFromAssets(context, fileName);
                    if (!TextUtils.isEmpty(moduleJsonStr)) {
                        JSONObject jsonObject = JSON.parseObject(moduleJsonStr);
                        setupModule(context, jsonObject);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setupModule(Context context, JSONObject jsonObject) {
        if (context == null || jsonObject == null || jsonObject.size() == 0) {
            return;
        }
        String moduleName = null;
        String className = null;
        Class clazz = null;
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            moduleName = entry.getKey();
            className = entry.getValue().toString();
            parseModule(moduleName, className);
        }
    }

    private static void parseModule(String moduleName, String className) {
        try {
            Class clazz = Class.forName(className);
            if (IBridgeImpl.class.isAssignableFrom(clazz)) {
                checkModule(clazz);
                L.d(HybridConstants.TAG, "registerModule-->%s", className);
                JSBridge.register(moduleName, clazz);
            }
        } catch (Exception e) {
            L.e(HybridConstants.TAG, e);
        }
    }

    private static void checkModule(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC)) {
                throw new RuntimeException("module " + clazz.getCanonicalName() + " define error , the error method is " + method.toString());
            }
            Class[] parameters = method.getParameterTypes();
            if (parameters == null && parameters.length != 3) {
                throw new RuntimeException("module " + clazz.getCanonicalName() + " define error , the error method is " + method.toString());
            }
            if (parameters[0] != WebView.class || parameters[1] != JSONObject.class || parameters[2] != BridgeCallback.class) {
                throw new RuntimeException("module " + clazz.getCanonicalName() + " define error , the error method is " + method.toString());
            }
        }
    }

}
