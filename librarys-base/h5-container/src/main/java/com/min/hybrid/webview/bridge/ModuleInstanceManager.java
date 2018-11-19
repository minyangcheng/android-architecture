package com.min.hybrid.webview.bridge;

import android.content.Context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleInstanceManager {

    private static Map<Context, ModuleInstance> instanceMap = new ConcurrentHashMap<>();

    public static void add(Context context, ModuleInstance instance) {
        if (context == null || instance == null) {
            return;
        }
        instanceMap.put(context, instance);
    }

    public static void remove(Context context) {
        if (context == null) {
            return;
        }
        instanceMap.remove(context);
    }

    public static ModuleInstance getModuleInstance(Context context) {
        if (context == null) {
            return null;
        }
        return instanceMap.get(context);
    }

}
