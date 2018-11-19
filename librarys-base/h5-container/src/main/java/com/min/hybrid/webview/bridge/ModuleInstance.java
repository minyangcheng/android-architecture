package com.min.hybrid.webview.bridge;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.min.hybrid.HybridConstants;
import com.min.hybrid.util.AssetsUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleInstance {

    private Context mContext;
    private Map<String, Object> mExposedModuleMap = new ConcurrentHashMap<>();
    private IWebViewHandler mWebViewHandler;

    public ModuleInstance(Context context) {
        this.mContext = context;
        loadModule();
        ModuleInstanceManager.add(context, this);
    }

    private void loadModule() {
        try {
            String[] fileArr = mContext.getAssets().list("");
            String fileName = null;
            for (int i = 0; i < fileArr.length; i++) {
                fileName = fileArr[i];
                if (fileName.endsWith(HybridConstants.MODULE_FILE_SUFFIX)) {
                    String moduleJsonStr = AssetsUtil.getFromAssets(mContext, fileName);
                    if (!TextUtils.isEmpty(moduleJsonStr)) {
                        JSONObject jsonObject = JSON.parseObject(moduleJsonStr);
                        setupModule(jsonObject);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupModule(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.size() == 0) {
            return;
        }
        try {
            String moduleName = null;
            String className = null;
            Class clazz = null;
            Constructor constructor = null;
            Object object = null;
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                moduleName = entry.getKey();
                className = entry.getValue().toString();
                clazz = Class.forName(className);
                constructor = clazz.getConstructor(Context.class);
                object = constructor.newInstance(mContext);
                mExposedModuleMap.put(moduleName, object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeModule(String moduleName, String methodName, Object... args) {
        try {
            Object object = mExposedModuleMap.get(moduleName);
            Class[] paramsTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramsTypes[i] = args[i].getClass();
            }
            Method method = object.getClass().getDeclaredMethod(methodName, paramsTypes);
            method.invoke(object, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasModule(String moduleName) {
        return mExposedModuleMap.containsKey(moduleName);
    }

    public boolean hasMethodInModule(String moduleName, String methodName, Class[] paramsTypes) {
        try {
            if (hasModule(moduleName)) {
                Object object = mExposedModuleMap.get(moduleName);
                Method method = object.getClass().getDeclaredMethod(methodName, paramsTypes);
                return method != null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void destroy() {
        ModuleInstanceManager.remove(mContext);
    }

    public void setWebViewHandler(IWebViewHandler webViewHandler) {
        this.mWebViewHandler = webViewHandler;
    }

    public IWebViewHandler getWebViewHandler() {
        return mWebViewHandler;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Class[] paramsTypes = new Class[3];
            paramsTypes[0] = Integer.class;
            paramsTypes[1] = Integer.class;
            paramsTypes[2] = Intent.class;
            for (Object object : mExposedModuleMap.values()) {
                Method method = object.getClass().getDeclaredMethod("onActivityResult", paramsTypes);
                if (method != null) {
                    method.invoke(object, requestCode, resultCode, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
